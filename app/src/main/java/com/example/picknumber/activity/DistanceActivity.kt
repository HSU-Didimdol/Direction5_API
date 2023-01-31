package com.example.picknumber.activity

import android.os.Bundle
import android.util.Log
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import com.example.picknumber.controller.DistanceController
import com.example.picknumber.databinding.ActivityDistanceBinding
import com.example.picknumber.model.SearchModel.Search
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class DistanceActivity : AppCompatActivity() {
    lateinit var binding: ActivityDistanceBinding
    lateinit var dc: DistanceController
//    lateinit var dcc: DistanceCustomAdapter

    val listData = arrayListOf<Search>()
//    val dca = DistanceCustomAdapter(listData)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDistanceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dc = DistanceController()

        val start = "127.0103676,37.5825502" // 현재 내 위치 (학교 위치로)
        val goal = "127.1373933,37.4428502" // 성남수정 본점 (경도, 위도)


    }
}