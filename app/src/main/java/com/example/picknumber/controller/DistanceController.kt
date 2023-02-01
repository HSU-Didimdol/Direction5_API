package com.example.picknumber.controller

import android.util.Log
import com.example.picknumber.keyModel.ApiKey
import com.example.picknumber.keyModel.MockyApi
import com.example.picknumber.model.BankDTO.BankDTO
import com.example.picknumber.model.BankModel.BankLatLng
import com.example.picknumber.service.DistanceService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.example.picknumber.model.ResultModel.Result
import com.example.picknumber.model.SearchModel.Search
import com.example.picknumber.service.BankService
import kotlin.properties.Delegates

class DistanceController {

    // 각 은행의 경도와 위도 값 리스트로 만들기
    fun getSortedDistance(): MutableList<Search> {

        Log.d("여기 오나?", "2")

        val start = "127.0103676,37.5825502" // 현재 내 위치 (학교 위치로)
        var bankLatLngList: ArrayList<BankLatLng> = ArrayList()

        // (은행지점, 거리) 리스트
        var searchList: MutableList<Search> = ArrayList()

        val retrofit = Retrofit.Builder().baseUrl(MockyApi.DOMAIN)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(BankService::class.java)
        service.getBank().enqueue(object : Callback<BankDTO> {
            override fun onResponse(call: Call<BankDTO>, response: Response<BankDTO>) {
                if(response.isSuccessful) {
                    Log.d("BankPos >> ", response.body().toString())
                    Log.d("items size >> ", response.body()!!.items.size.toString())
                    bankLatLngList = getBankLatLngList(response.body()!!)
                    Log.d("은행 경도, 위도 잘 들어오나? ", bankLatLngList.toString()) // 잘 들어오고

                    // 뽑아온 리스트를 기반으로 거리 구하기
                    for (i in 0 until bankLatLngList.size) {
                        Log.d("값이 어떻게 나오나?", bankLatLngList[i].toString())
                        Log.d("경도 >> ", bankLatLngList[i].lat.toString())
                        Log.d("위도 >> ", bankLatLngList[i].lng.toString())
                        var goal = bankLatLngList[i].lng.toString()+ "," + bankLatLngList[i].lat.toString()
                        var bankName = bankLatLngList[i].name
                        var distance = getDistanceToBank(start, goal) // 거리 값 구하기 위해서 api 호출
                        Log.d("goal >> ", goal)
                        Log.d("bankName >> ", bankName)
                        Log.d("distance >> ", distance.toString()) // 여기서는 distance = 0 임..
                        searchList.add(Search(distance, bankName))
                    }

                    Log.d("searchList >> ", searchList.toString())
                }
            }

            override fun onFailure(call: Call<BankDTO>, t: Throwable) {
                Log.e("errorMsg : ", t.toString())
                Log.e("DistanceController : ", "getBankPos retrofit 에러")
            }
        })
        return searchList
    }

    // 현 위치 ~ 각 본점까지의 거리 구하기
    // getSortedDistance() 에서 getDistanceToBank() 호출해서 distance 구하려니까 0으로 뜬다.. >> 코루틴 이용
    private fun getDistanceToBank(start:String, goal:String): Int {

        var distance = 0

        Log.d("여기 오나?", "4")

        val retrofit = Retrofit.Builder().baseUrl(ApiKey.DOMAIN)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(DistanceService::class.java)
        service.getDistance(ApiKey.CLIENT_ID, ApiKey.CLIENT_SECRET, start, goal).enqueue(object : Callback<Result> {
            override fun onResponse(call: Call<Result>, response: Response<Result>) {
                Log.d("response >> ", "result: ${response}")
                Log.d("여기 오나?", "5")
                if (response.isSuccessful) {

                    if (response.body()?.route != null) {
                        Log.d("여기 오나?", "6")
                        Log.d("api 연동 성공 >> ", "body : ${response.body().toString()}")
                        Log.d("distance >> ", "distance : ${response.body()!!.route.traoptimal[0].summary.distance}")


//                        // 맨 처음 실행해서 검색 버튼 누르면 distance 가 0으로 넘어가고,
//                        // 그 다음부터는 검색 버튼 누르면 정상적인 distance 값이 넘어감.. 이거 해결해야 할 듯
//                        var intent = Intent(context, DistanceActivity::class.java)
//                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//                        intent.putExtra("distance", response.body()!!.route.traoptimal[0].summary.distance)

//                        context.startActivity(intent)

                        distance = response.body()!!.route.traoptimal[0].summary.distance
                    }
                }
            }

            override fun onFailure(call: Call<Result>, t: Throwable) {
                Log.e("errorMsg : ", t.toString())
                Log.e("DistanceController : ", "getDistanceToBank retrofit 에러")
            }
        })
        return distance
    }

    fun getBankLatLngList(body: BankDTO): ArrayList<BankLatLng> {

        Log.d("여기 오나?", "3")

        var latLngList: ArrayList<BankLatLng> = ArrayList()

        for (i in 0 until body.items.size) {
            val item = body.items[i] // Bank 모델 하나씩 담고
            latLngList.add(BankLatLng(item.name, item.lat, item.lng))
        }
        return latLngList
    }
}