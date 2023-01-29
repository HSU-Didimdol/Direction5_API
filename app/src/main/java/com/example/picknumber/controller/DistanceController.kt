package com.example.picknumber.controller

import android.content.Context
import android.content.Intent
import android.util.Log
import com.example.picknumber.activity.DistanceActivity
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
    fun getDistanceToBank(start:String, goal:String, context: Context) {

        val retrofit = Retrofit.Builder().baseUrl(ApiKey.DOMAIN)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(getDistance::class.java)
        service.getDistance(ApiKey.CLIENT_ID, ApiKey.CLIENT_SECRET, start, goal).enqueue(object : Callback<Result> {
            override fun onResponse(call: Call<Result>, response: Response<Result>) {
                Log.d("response >> ", "result: ${response}")
                if (response.isSuccessful) {
                    if (response.body()?.route != null) {
                        Log.d("api 연동 성공 >> ", "body : ${response.body().toString()}")
                        Log.d("distance >> ", "distance : ${response.body()!!.route.traoptimal[0].summary.distance}")

                        // 맨 처음 실행해서 검색 버튼 누르면 distance 가 0으로 넘어가고,
                        // 그 다음부터는 검색 버튼 누르면 정상적인 distance 값이 넘어감.. 이거 해결해야 할 듯
                        var intent = Intent(context, DistanceActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        intent.putExtra("distance", response.body()!!.route.traoptimal[0].summary.distance)

                        context.startActivity(intent)
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