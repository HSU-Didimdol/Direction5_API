package com.example.picknumber.activity

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.picknumber.controller.DistanceController
import com.example.picknumber.databinding.ActivityDistanceBinding
import com.example.picknumber.service.getDistance
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class DistanceActivity : AppCompatActivity() {
    lateinit var binding: ActivityDistanceBinding
    lateinit var dc: DistanceController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDistanceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dc = DistanceController()
        dc.getDistanceToBank("127.12345,37.12345", "129.084454,35.228982")
    }
}