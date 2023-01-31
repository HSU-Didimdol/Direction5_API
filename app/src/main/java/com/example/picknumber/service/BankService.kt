package com.example.picknumber.service

import com.example.picknumber.model.BankDTO.BankDTO
import retrofit2.Call
import retrofit2.http.GET

interface BankService {
    @GET("v3/51b5315c-34a1-4afc-b835-e961ec6772d5")
    fun getBank(): Call<BankDTO>
}