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

class DistanceController {
    // 거리 추출하여 리스트로 뽑기
    fun getSortedDistance() {
        val start = "127.0103676,37.5825502" // 현재 내 위치 (학교 위치로)
        val bankPos: ArrayList<BankLatLng> = getBankPos()

        // 뽑아온 리스트를 기반으로 거리 구하기
        // (은행지점, 거리) 리스트
        var searchList: ArrayList<Search> = ArrayList()

        for (i in 0 until bankPos.size) {
            Log.d("값이 어떻게 나오나?", bankPos.get(i).toString())
            var goal = bankPos[i].lat.toString() + bankPos[i].lng.toString()
            var bankName = bankPos[i].name
            var distance = getDistanceToBank(start, goal) // 거리 값 구하기 위해서 api 호출
            searchList.add(Search(distance, bankName))
        }

        Log.d("searchList >> ", searchList.toString())
    }

    // 현 위치 ~ 각 본점까지의 거리 구하기
    private fun getDistanceToBank(start:String, goal:String): Int {

        var distance = 0

        val retrofit = Retrofit.Builder().baseUrl(ApiKey.DOMAIN)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(DistanceService::class.java)
        service.getDistance(ApiKey.CLIENT_ID, ApiKey.CLIENT_SECRET, start, goal).enqueue(object : Callback<Result> {
            override fun onResponse(call: Call<Result>, response: Response<Result>) {
                Log.d("response >> ", "result: ${response}")
                if (response.isSuccessful) {
                    if (response.body()?.route != null) {
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

    // 각 은행의 경도와 위도 값 리스트로 만들기
    private fun getBankPos(): ArrayList<BankLatLng> {

        var bankLatLngList: ArrayList<BankLatLng> = ArrayList()

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
                }
            }

            override fun onFailure(call: Call<BankDTO>, t: Throwable) {
                Log.e("errorMsg : ", t.toString())
                Log.e("DistanceController : ", "getBankPos retrofit 에러")
            }
        })
        return bankLatLngList
    }

    fun getBankLatLngList(body: BankDTO): ArrayList<BankLatLng> {
        var latLngList: ArrayList<BankLatLng> = ArrayList()

        for (i in 0 until body.items.size) {
            val item = body.items[i] // Bank 모델 하나씩 담고
            latLngList.add(BankLatLng(item.name, item.lat, item.lng))
        }
        return latLngList
    }
}