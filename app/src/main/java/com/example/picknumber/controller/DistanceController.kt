package com.example.picknumber.controller

import android.util.Log
import com.example.picknumber.keyModel.ApiKey
import com.example.picknumber.service.getDistance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.example.picknumber.model.ResultModel.Result
import okhttp3.Interceptor
import okhttp3.OkHttpClient

class DistanceController {
    // 현 위치 ~ 새마을금고 본점까지의 거리 가져오기
    // 일단은 임의 값들로 테스트
    fun getDistanceToBank(start:String, goal:String) {
        val retrofit = Retrofit.Builder().baseUrl(ApiKey.DOMAIN)
//            .client(provideOkHttpClient(AppInterceptor()))
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(getDistance::class.java)
        service.getDistance(ApiKey.CLIENT_ID, ApiKey.CLIENT_SECRET, start, goal).enqueue(object : Callback<Result> {
            override fun onResponse(call: Call<Result>, response: Response<Result>) {
                Log.d("response 값 나옴?", "result: ${response}")
                if (response.isSuccessful) {
                    if (response.body()?.route != null) {
                        Log.d("api 로 데이터 잘 불러와짐?", "body : ${response.body().toString()}")
                        Log.d("distance 나옴?", "distance : ${response.body()!!.route.traoptimal[0].summary.distance}")
                    }
                }
            }

            override fun onFailure(call: Call<Result>, t: Throwable) {
                Log.e("DistanceController : ", "getDistanceToBank retrofit 에러")
            }
        })
    }

//    private fun provideOkHttpClient(interceptor: AppInterceptor): OkHttpClient = OkHttpClient.Builder()
//        .run {
//            addInterceptor(interceptor)
//            build()
//        }
//
//    class AppInterceptor : Interceptor {
//        override fun intercept(chain: Interceptor.Chain): okhttp3.Response = with(chain) {
//            val newRequest = request().newBuilder()
//                .addHeader("X-NCP-APIGW-API-KEY-ID", ApiKey.CLIENT_ID)
//                .addHeader("X-NCP-APIGW-API-KEY", ApiKey.CLIENT_SECRET)
//                .build()
//
//            proceed(newRequest)
//        }
//    }
}