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
import com.example.picknumber.model.BankModel.BankLatLng
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

        val start = "127.0103676,37.5825502" // 현재 내 위치 (학교 위치로)
//        val goal = "127.1373933,37.4428502" // 성남수정 본점 (경도, 위도)

//        binding.searchBank.isSubmitButtonEnabled = true

        binding.searchBank.requestFocus()

        binding.searchBank.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(bank: String?): Boolean {

                // 검색한 은행 지점에 해당하는 경도, 위도 값을 받아오는 메서드 구현해야 할 듯

                // 검색버튼을 눌렀을 때 거리 가까운 순으로 리스트

                launch {

                    Log.d("시작??", "시작??")
                    mc.getNameLatLngList(applicationContext) // 각 은행 별 (이름, 경도, 위도) 리스트 가져오기

                    val bankLatLngList: ArrayList<BankLatLng>? = intent.getSerializableExtra("bankLatLngList") as ArrayList<BankLatLng>?
                    Log.d("bankLatLngList 잘 나옴?? ", bankLatLngList.toString()) // 처음에는 null 뜨고 다시 검색하면 제대로 나옴..

                    // (은행지점, 거리) 리스트
                    val searchList: MutableList<Search> = mutableListOf()

                    val start = "127.0103676,37.5825502" // 현재 내 위치 받아오고 (일단 학교 위치로)
                    var goal = ""
                    var distance = 0

                    if (bankLatLngList != null) {
                        // (경도, 위도) 리스트를 기반으로 각 은행 별까지의 거리 구하기
                        for (i in 0 until bankLatLngList!!.size) {
                            Log.d("여기 다시 오나?", "1")
                            Log.d("값이 어떻게 나오나?", bankLatLngList[i].toString())
                            Log.d("경도 >> ", bankLatLngList[i].lat.toString())
                            Log.d("위도 >> ", bankLatLngList[i].lng.toString())

                            goal = bankLatLngList[i].lng.toString() + "," + bankLatLngList[i].lat.toString()
                            var bankName = bankLatLngList[i].name
                            distance = dc.getDistanceToBank(start, goal)

                            Log.d("goal >> ", goal)
                            Log.d("bankName >> ", bankName)

                            searchList.add(Search(distance, bankName))
                            Log.d("searchList 잘 나오는 중?? ", searchList.toString())
                        }
                    }


                    dca = DistanceCustomAdapter(searchList as ArrayList<Search>, object : DistanceCustomAdapter.OnRouteClickedListener {
                        override fun onRouteClicked(model: Search) {
                            Log.d("item 눌림??", "item 눌림")
                        }

                    })
                    binding.distanceRecyclerView.adapter = dca
                    dca.notifyDataSetChanged()
//
//                    Log.d("searchList 잘 나옴?? ", searchList.toString())
////                    dca = DistanceCustomAdapter(searchList)
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