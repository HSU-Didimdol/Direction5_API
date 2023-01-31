package com.example.picknumber.service

import com.example.picknumber.model.ResultModel.Result
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface DistanceService {
    // "https://naveropenapi.apigw.ntruss.com/map-direction/v1/driving?start={출발지}&goal={목적지}&option={탐색옵션}" \
    //	-H "X-NCP-APIGW-API-KEY-ID: {애플리케이션 등록 시 발급받은 client id 값}" \
    //	-H "X-NCP-APIGW-API-KEY: {애플리케이션 등록 시 발급받은 client secret값}" -v
    @GET("v1/driving")
    fun getDistance(
        @Header("X-NCP-APIGW-API-KEY-ID") clientID: String,
        @Header("X-NCP-APIGW-API-KEY") clientSecret: String,
        @Query("start", encoded = true) start: String,
        @Query("goal", encoded = true) goal: String,
//        @Query("option") option: String, // 필수여부 N
    ): Call<Result>
}