package com.example.picknumber.activity

import android.os.Bundle
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

class DistanceActivity : AppCompatActivity() {
    lateinit var binding: ActivityDistanceBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDistanceBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}