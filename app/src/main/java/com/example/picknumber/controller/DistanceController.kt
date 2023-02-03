package com.example.picknumber.controller

import android.util.Log
import com.example.picknumber.keyModel.ApiKey
import com.example.picknumber.keyModel.MockyApi
import com.example.picknumber.model.BankDTO.BankDTO
import com.example.picknumber.model.BankModel.Bank
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

    // 1. getLatLngList() 호출해서 은행 (이름, 경도, 위도) 리스트를 뽑아오고 >> bankLatLngList
    // 2. 뽑아온 리스트를 기반으로 거리 구해서 viewModel 에 추가
    // 3. 거리 구하려면 getDistanceToBank() 호출해서 거리 값을 받아와야 하는데..


    // 현 위치 ~ 각 본점까지의 거리 구하기
    // getSortedDistance() 에서 getDistanceToBank() 호출해서 distance 구하려니까 0으로 뜬다.. >> 코루틴 이용 >> 일단 나중에
    // 여기서 거리 값까지 구해서 리스트 넘겨주기
    fun getDistanceToBank(start:String, goal:String): Int {

        var distance = 0

        Log.d("여기 오나?", "4")

        val retrofit = Retrofit.Builder().baseUrl(ApiKey.DOMAIN)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(DistanceService::class.java)

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
//                            searchList[i].distance = distance
//                            Log.d("현재 searchList >> ", searchList.toString())
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
}