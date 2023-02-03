package com.example.picknumber.controller

import android.content.Context
import android.content.Intent
import android.util.Log
import com.example.picknumber.activity.MainActivity
import com.example.picknumber.keyModel.MockyApi
import com.example.picknumber.model.BankDTO.BankDTO
import com.example.picknumber.model.BankModel.BankLatLng
import com.example.picknumber.service.BankService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainController {
    // 각 은행지점의 (이름, 경도, 위도) 리스트로 뽑기
    fun getNameLatLngList(context: Context) {

        Log.d("여기 오나?", "2")

        var bankLatLngList: ArrayList<BankLatLng> = ArrayList()

        val retrofit = Retrofit.Builder().baseUrl(MockyApi.DOMAIN)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(BankService::class.java)

        service.getBank().enqueue(object : Callback<BankDTO> {
            override fun onResponse(call: Call<BankDTO>, response: Response<BankDTO>) {
                if(response.isSuccessful) {
                    if (response.body()?.items != null) {
                        Log.d("BankPos >> ", response.body().toString())
                        Log.d("items size >> ", response.body()!!.items.size.toString())
                        bankLatLngList = getBankNameLatLngList(response.body()!!)
                        Log.d("은행 경도, 위도 잘 들어오나? ", bankLatLngList.toString()) // 잘 들어오고

                        var intent = Intent(context, MainActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        intent.putExtra("bankLatLngList", bankLatLngList)
                        context.startActivity(intent)
                    }
                }
            }

            override fun onFailure(call: Call<BankDTO>, t: Throwable) {
                Log.e("errorMsg : ", t.toString())
                Log.e("DistanceController : ", "getBankPos retrofit 에러")
            }
        })
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