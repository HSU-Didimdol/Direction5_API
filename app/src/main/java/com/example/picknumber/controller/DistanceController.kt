package com.example.picknumber.controller

import android.content.Context
import android.util.Log
import com.example.picknumber.keyModel.ApiKey
import com.example.picknumber.model.BankModel.BankLatLng
import com.example.picknumber.service.DistanceService
import com.example.picknumber.model.ResultModel.Result
import retrofit2.converter.gson.GsonConverterFactory
import com.example.picknumber.model.SearchModel.Search
import kotlinx.coroutines.*
import retrofit2.*

class DistanceController {

    // 1. getLatLngList() 호출해서 은행 (이름, 경도, 위도) 리스트를 뽑아오고 >> bankLatLngList
    // 2. 뽑아온 리스트를 기반으로 거리 구해서 viewModel 에 추가
    // 3. 거리 구하려면 getDistanceToBank() 호출해서 거리 값을 받아와야 하는데..

    // 현 위치 ~ 각 본점까지의 거리 구하기
    // getSortedDistance() 에서 getDistanceToBank() 호출해서 distance 구하려니까 0으로 뜬다.. >> 코루틴 이용 >> 일단 나중에
    // 여기서 거리 값까지 구해서 리스트 넘겨주기
    fun getDistanceToBank(start: String, goal: String): Int {

        Log.d("여기 오나?", "4")

        val retrofit = Retrofit.Builder().baseUrl(ApiKey.DOMAIN)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(DistanceService::class.java)

        var call: Call<Result> = service.getDistance(ApiKey.CLIENT_ID, ApiKey.CLIENT_SECRET, start, goal)
        var body = call.execute().body()

        try {
            var distance = body!!.route.traoptimal[0].summary.distance
            Log.d("distance >> ", distance.toString())
            return distance
        } catch (e: Exception) {
            return 0
        }

//        CoroutineScope(Dispatchers.Main).launch {
//

        // (경도, 위도) 리스트를 기반으로 각 은행 별까지의 거리 구하기
//        for (i in 0 until bankLatLngList!!.size) {
//            Log.d("여기 다시 오나?", "1")
//            Log.d("값이 어떻게 나오나?", bankLatLngList[i].toString())
//            Log.d("경도 >> ", bankLatLngList[i].lat.toString())
//            Log.d("위도 >> ", bankLatLngList[i].lng.toString())
//
//            var bankName = bankLatLngList[i].name
//            goal = bankLatLngList[i].lng.toString() + "," + bankLatLngList[i].lat.toString()
//
//            service.getDistance(ApiKey.CLIENT_ID, ApiKey.CLIENT_SECRET, start, goal)
//                .enqueue(object : Callback<Result> {
//                    override fun onResponse(call: Call<Result>, response: Response<Result>) {
//                        if (response.isSuccessful) {
//                            if (response.body()?.route != null) {
//                                Log.d("여기 오나?", "5")
////                                Log.d("api 연동 성공 >> ", "body : ${response.body().toString()}")
//                                Log.d("distance >> ", "distance : ${response.body()!!.route.traoptimal[0].summary.distance}")
//
//                                distance = response.body()!!.route.traoptimal[0].summary.distance
//
//                                searchList.add(Search(distance, bankName))
//                                Log.d("searchList 잘 나오는 중??", searchList.toString())
//                            }
//                        }
//                    }
//
//                    override fun onFailure(call: Call<Result>, t: Throwable) {
//                        Log.e("errorMsg : ", t.toString())
//                        Log.e("DistanceController : ", "getDistanceToBank retrofit 에러")
//                    }
//                })
//        }
//    }
//}
    }

    private fun getShortSortedDistance(searchList:ArrayList<Search>, context:Context) {
        Log.d("여기서도 잘 나오나??", searchList.toString())


    }
}