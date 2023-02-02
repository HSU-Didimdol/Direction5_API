package com.example.picknumber.controller

import android.util.Log
import com.example.picknumber.keyModel.ApiKey
import com.example.picknumber.keyModel.MockyApi
import com.example.picknumber.model.BankDTO.BankDTO
import com.example.picknumber.model.BankModel.BankLatLng
import com.example.picknumber.service.DistanceService
import retrofit2.converter.gson.GsonConverterFactory
import com.example.picknumber.model.ResultModel.Result
import com.example.picknumber.model.SearchModel.Search
import com.example.picknumber.service.BankService
import kotlinx.coroutines.*
import retrofit2.*
import kotlin.coroutines.CoroutineContext
import kotlin.properties.Delegates

class DistanceController {

    // 1. getLatLngList() 호출해서 은행 (경도, 위도) 리스트를 뽑아오고 >> bankLatLngList
    // 2. 뽑아온 리스트를 기반으로 거리 구해서 viewModel 에 추가
    // 3. 거리 구하려면 getDistanceToBank() 호출해서 거리 값을 받아와야 하는데..

    fun getSortedDistance() {
        getLatLngList()
    }

    private fun getLatLngList() {

        Log.d("여기 오나?", "2")

        val start = "127.0103676,37.5825502" // 현재 내 위치 받아오고 (일단 학교 위치로)
        var bankLatLngList: ArrayList<BankLatLng> = ArrayList()

        // (은행지점, 거리) 리스트
        var searchList: MutableList<Search> = mutableListOf()

        val retrofit = Retrofit.Builder().baseUrl(MockyApi.DOMAIN)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(BankService::class.java)
        service.getBank().enqueue(object : Callback<BankDTO> {
            override fun onResponse(call: Call<BankDTO>, response: Response<BankDTO>) {
                if(response.isSuccessful) {
                    Log.d("BankPos >> ", response.body().toString())
                    Log.d("items size >> ", response.body()!!.items.size.toString())
                    bankLatLngList = getBankNameLatLngList(response.body()!!)
                    Log.d("은행 경도, 위도 잘 들어오나? ", bankLatLngList.toString()) // 잘 들어오고

                    getDistanceToBank(start, bankLatLngList) // bankLatLngList 넘겨서 거리 구해서 리스트로 뽑기
                }
            }

            override fun onFailure(call: Call<BankDTO>, t: Throwable) {
                Log.e("errorMsg : ", t.toString())
                Log.e("DistanceController : ", "getBankPos retrofit 에러")
            }
        })
    }

    // 현 위치 ~ 각 본점까지의 거리 구하기
    // getSortedDistance() 에서 getDistanceToBank() 호출해서 distance 구하려니까 0으로 뜬다.. >> 코루틴 이용 >> 일단 나중에
    // 여기서 거리 값까지 구해서 리스트 넘겨주기
    private fun getDistanceToBank(start:String, bankLatLngList:ArrayList<BankLatLng>) {

        var distance = 0
        var goal = ""

        // (은행지점, 거리) 리스트
        val searchList: MutableList<Search> = mutableListOf()

        Log.d("여기 오나?", "4")

        val retrofit = Retrofit.Builder().baseUrl(ApiKey.DOMAIN)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(DistanceService::class.java)

        // 이거 로직 다시 설계해야 할 듯.. 프로그램 느려질 확률 높음
        // (경도, 위도) 리스트를 기반으로 거리 구하기
        for (i in 0 until bankLatLngList.size) {
            Log.d("값이 어떻게 나오나?", bankLatLngList[i].toString())
            Log.d("경도 >> ", bankLatLngList[i].lat.toString())
            Log.d("위도 >> ", bankLatLngList[i].lng.toString())
            goal = bankLatLngList[i].lng.toString()+ "," + bankLatLngList[i].lat.toString()
            var bankName = bankLatLngList[i].name
            Log.d("goal >> ", goal)
            Log.d("bankName >> ", bankName)
            searchList.add(Search(0, bankName))

            retrofit.getDistance(ApiKey.CLIENT_ID, ApiKey.CLIENT_SECRET, start, goal).enqueue(object : Callback<Result> {
                override fun onResponse(call: Call<Result>, response: Response<Result>) {
                    Log.d("response >> ", "result: ${response}")
                    Log.d("여기 오나?", "5")
                    if (response.isSuccessful) {

                        if (response.body()?.route != null) {
                            Log.d("여기 오나?", "6")
                            Log.d("api 연동 성공 >> ", "body : ${response.body().toString()}")
                            Log.d("distance >> ", "distance : ${response.body()!!.route.traoptimal[0].summary.distance}")

                            distance = response.body()!!.route.traoptimal[0].summary.distance
                            searchList[i].distance = distance
                            Log.d("현재 searchList >> ", searchList.toString())
                        }
                    }
                }

                override fun onFailure(call: Call<Result>, t: Throwable) {
                    Log.e("errorMsg : ", t.toString())
                    Log.e("DistanceController : ", "getDistanceToBank retrofit 에러")
                }
            })
        }

        sendViewModel(searchList)
    }

    private fun sendViewModel(searchList:MutableList<Search>) {

    }

    fun getBankNameLatLngList(body: BankDTO): ArrayList<BankLatLng> {

        Log.d("여기 오나?", "3")

        var latLngList: ArrayList<BankLatLng> = ArrayList()

        for (i in 0 until body.items.size) {
            val item = body.items[i] // Bank 모델 하나씩 담고
            latLngList.add(BankLatLng(item.name, item.lat, item.lng))
        }
        return latLngList
    }
}