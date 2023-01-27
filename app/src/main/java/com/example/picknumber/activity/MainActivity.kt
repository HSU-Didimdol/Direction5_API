package com.example.picknumber.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.picknumber.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }
}