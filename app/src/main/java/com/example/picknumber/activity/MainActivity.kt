package com.example.picknumber.activity

import android.os.Build
import android.os.Bundle
import android.os.StrictMode
import android.util.Log
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import com.example.picknumber.adapter.DistanceCustomAdapter
import com.example.picknumber.controller.DistanceController
import com.example.picknumber.controller.MainController
import com.example.picknumber.databinding.ActivityDistanceBinding
import com.example.picknumber.databinding.ActivityMainBinding
import com.example.picknumber.model.SearchModel.Search
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class MainActivity : AppCompatActivity(), CoroutineScope {
    lateinit var binding: ActivityMainBinding
    lateinit var mc: MainController
    lateinit var dc: DistanceController
    lateinit var dca: DistanceCustomAdapter

    // 코루틴
    lateinit var job: Job
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job
    // 코루틴

    val listData = arrayListOf<Search>()
//    val dca = DistanceCustomAdapter(listData)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        job = Job()
        mc = MainController()
        dc = DistanceController()
//        dca = DistanceCustomAdapter()

        if (Build.VERSION.SDK_INT > 9) {
            val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
            StrictMode.setThreadPolicy(policy)
        }

        val start = "127.0103676,37.5825502" // 현재 내 위치 (학교 위치로)

        // (은행지점, 거리) 리스트
        var searchList:ArrayList<Search> = ArrayList()

        binding.searchBank.isSubmitButtonEnabled = true

        binding.searchBank.requestFocus()

        binding.searchBank.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(bank: String?): Boolean {

                // 검색버튼을 눌렀을 때 거리 가까운 순으로 리스트

                launch {

                    Log.d("시작??", "시작??")

                    searchList.clear()

                    var bankLatLngList = mc.getNameLatLngList() // 각 은행 별 (이름, 경도, 위도) 리스트 가져오기

                    Log.d("ma::bankLatLngList >> ", bankLatLngList.toString())

                    // 받아온 bankLatLngList 로 거리 구하기
                    for (i in 0 until bankLatLngList.size) {
                        var bankName = bankLatLngList[i].name
                        var goal = bankLatLngList[i].lng.toString() + "," + bankLatLngList[i].lat.toString()
                        var distance = dc.getDistanceToBank(start, goal)

                        distance /= 1000
                        searchList.add(Search(distance, bankName))
                    }
                    Log.d("ma::searchList >> ", searchList.toString())

                    searchList.sortBy(Search::distance)
                    Log.d("sortedDistance >> ", searchList.toString())

                    dca = DistanceCustomAdapter(searchList, object : DistanceCustomAdapter.OnRouteClickedListener {
                        override fun onRouteClicked(model: Search) {
                            Log.d("item 눌림??", "item 눌림")
                        }

                    })
                    binding.distanceRecyclerView.adapter = dca
                    dca.notifyDataSetChanged()
                }

                return true
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                // 입력 중인 상태
                return true
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }
}