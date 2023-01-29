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

class DistanceActivity : AppCompatActivity(), CoroutineScope {
    lateinit var binding: ActivityDistanceBinding
    lateinit var dc: DistanceController
//    lateinit var dcc: DistanceCustomAdapter

    // 코루틴
    lateinit var job: Job
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job
    // 코루틴

    val listData = arrayListOf<Search>()
//    val dca = DistanceCustomAdapter(listData)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDistanceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        job = Job()
        dc = DistanceController()

        val start = "127.0103676,37.5825502" // 현재 내 위치 (학교 위치로)
        val goal = "127.1373933,37.4428502" // 성남수정 본점 (경도, 위도)

//        binding.searchBank.isSubmitButtonEnabled = true

        binding.searchBank.requestFocus()

        binding.searchBank.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(bank: String?): Boolean {
                var goalBank = bank
                // 검색한 은행 지점에 해당하는 경도, 위도 값을 받아오는 메서드 구현해야 할 듯

                launch {
                    dc.getDistanceToBank(start, goal, applicationContext) // 거리 값 구하기 위해서 api 호출
                    val distance = intent.getIntExtra("distance", 0)
                    Log.d("intent 로 넘어온 >> ", "distance: ${distance}")
                }

                return true
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                // 입력 중인 상태
                return true
            }
        })


    }
}