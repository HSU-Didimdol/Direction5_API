package com.example.picknumber.controller

import android.util.Log
import com.example.picknumber.keyModel.ApiKey
import com.example.picknumber.model.BankModel.BankLatLng
import com.example.picknumber.service.DistanceService
import retrofit2.converter.gson.GsonConverterFactory
import com.example.picknumber.model.ResultModel.Result
import com.example.picknumber.model.SearchModel.Search
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import retrofit2.*

class DistanceController {

    // 1. getLatLngList() 호출해서 은행 (이름, 경도, 위도) 리스트를 뽑아오고 >> bankLatLngList
    // 2. 뽑아온 리스트를 기반으로 거리 구해서 viewModel 에 추가
    // 3. 거리 구하려면 getDistanceToBank() 호출해서 거리 값을 받아와야 하는데..

    fun getDistance(bankLatLngList:ArrayList<BankLatLng>) {
        val start = "127.0103676,37.5825502" // 현재 내 위치 받아오고 (일단 학교 위치로)
        var goal = ""
        var distance = 0

        var searchList: ArrayList<Search> = ArrayList()

        // (경도, 위도) 리스트를 기반으로 각 은행 별까지의 거리 구하기
        for (i in 0 until bankLatLngList!!.size) {
            Log.d("여기 다시 오나?", "1")
            Log.d("값이 어떻게 나오나?", bankLatLngList[i].toString())
            Log.d("경도 >> ", bankLatLngList[i].lat.toString())
            Log.d("위도 >> ", bankLatLngList[i].lng.toString())

            var bankName = bankLatLngList[i].name
            goal = bankLatLngList[i].lng.toString() + "," + bankLatLngList[i].lat.toString()
            distance = getDistanceToBank(start, goal)
            Log.d("getDistance::distance", distance.toString())
        }
    }


    // 현 위치 ~ 각 본점까지의 거리 구하기
    // getSortedDistance() 에서 getDistanceToBank() 호출해서 distance 구하려니까 0으로 뜬다.. >> 코루틴 이용 >> 일단 나중에
    // 여기서 거리 값까지 구해서 리스트 넘겨주기
    fun getDistanceToBank(start:String, goal:String) {

        var distance = 0

        Log.d("여기 오나?", "4")

        val retrofit = Retrofit.Builder().baseUrl(ApiKey.DOMAIN)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(DistanceService::class.java)

        // (은행지점, 거리) 리스트
        var searchList: ArrayList<Search> = ArrayList()

//        CoroutineScope(Dispatchers.Main).launch {
//            val response = service.getDistance(ApiKey.CLIENT_ID, ApiKey.CLIENT_SECRET, start, goal)
//
////            Log.d("response 어떻게 나옴?? ", response.await().toString())
//
//            try {
//                distance = response.await().route.traoptimal[0].summary.distance
//                Log.d("distance >> ", distance.toString())
//            } catch (e:Exception) {
//                distance = 0
//            }

        service.getDistance(ApiKey.CLIENT_ID, ApiKey.CLIENT_SECRET, start, goal)
            .enqueue(object : Callback<Result> {
                var distance = 0
                override fun onResponse(call: Call<Result>, response: Response<Result>) {
//                        Log.d("response >> ", "result: ${response}")
                    if (response.isSuccessful) {
                        if (response.body()?.route != null) {
                            Log.d("여기 오나?", "5")
//                                Log.d("api 연동 성공 >> ", "body : ${response.body().toString()}")
                            Log.d("distance >> ", "distance : ${response.body()!!.route.traoptimal[0].summary.distance}")

                            distance = response.body()!!.route.traoptimal[0].summary.distance

                        }
                    }
                }

                override fun onFailure(call: Call<Result>, t: Throwable) {
                    Log.e("errorMsg : ", t.toString())
                    Log.e("DistanceController : ", "getDistanceToBank retrofit 에러")
                }
            })
    }
}