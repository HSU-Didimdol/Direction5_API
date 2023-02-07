package com.example.picknumber.controller

import android.content.Context
import android.util.Log
import com.example.picknumber.database.BankDatabase
import com.example.picknumber.keyModel.MockyApi
import com.example.picknumber.model.BankDTO.BankDTO
import com.example.picknumber.model.BankModel.BankLatLng
import com.example.picknumber.entity.Bank
import com.example.picknumber.model.SearchModel.Search
import com.example.picknumber.service.BankService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.Exception

class MainController {
    // 각 은행지점의 (이름, 경도, 위도) 리스트로 뽑기
    fun getNameLatLngList(context: Context) : List<Bank> {

        Log.d("여기 오나?", "2")

        val dc = DistanceController()

        var bankLatLngList: ArrayList<BankLatLng> = ArrayList()
        var bankData: List<Bank> = listOf()

        /**
         * 임시 API 만들어서 데이터 불러옴 -> Room 으로 변경
         */

        // db 연결
        val db = BankDatabase.getDatabase(context)

        // db 에 저장된 데이터 불러오기 (코루틴)
        CoroutineScope(Dispatchers.IO).launch {
            bankData = db!!.bankDao().getAllBank()
            Log.d("db 잘 나옴??", bankData.toString())
        }

       /*val retrofit = Retrofit.Builder().baseUrl(MockyApi.DOMAIN)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(BankService::class.java)

        var call: Call<BankDTO> = service.getBank()
        var body = call.execute().body()
        Log.d("mc::body >> ", body.toString())

        try {
            for (i in body!!.items) {
                bankLatLngList.add( BankLatLng(i.name, i.lat, i.lng) )
            }
            return bankLatLngList
        } catch (e : Exception){
            var ecp1 = ArrayList<BankLatLng>()
            ecp1.add(BankLatLng("",0.0, 0.0))
            return ecp1
        }*/
        return bankData

//        service.getBank().enqueue(object : Callback<BankDTO> {
//            override fun onResponse(call: Call<BankDTO>, response: Response<BankDTO>) {
//                if (response.isSuccessful) {
//                    if (response.body()?.items != null) {
//                        Log.d("BankPos >> ", response.body().toString())
//                        Log.d("items size >> ", response.body()!!.items.size.toString())
//
//                        for (i in 0 until response.body()!!.items.size) {
//                            val item = response.body()!!.items[i] // Bank 모델 하나씩 담고
//                            bankLatLngList.add(BankLatLng(item.name, item.lat, item.lng))
//                        }
//
//                        Log.d("은행 경도, 위도 잘 들어오나? ", bankLatLngList.toString()) // 잘 들어오고
//
////                        dc.getDistanceToBank(start, bankLatLngList)
//                    }
//                }
//            }
//            override fun onFailure(call: Call<BankDTO>, t: Throwable) {
//                Log.e("errorMsg : ", t.toString())
//                Log.e("DistanceController : ", "getBankPos retrofit 에러")
//            }
//        })
//        return bankLatLngList
    }

    private fun getBankNameLatLngList(body: BankDTO): ArrayList<BankLatLng> {

        Log.d("여기 오나?", "3")

        var latLngList: ArrayList<BankLatLng> = ArrayList()

        for (i in 0 until body.items.size) {
            val item = body.items[i] // Bank 모델 하나씩 담고
            latLngList.add(BankLatLng(item.name, item.lat, item.lng))
        }
        return latLngList
    }
}