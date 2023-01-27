package com.example.picknumber.model.ResultModel

import com.example.picknumber.model.TrackModel.Track

data class Result(
    val code: Int,
    val message: String,
    val currentDateTime: String,
    val route: Track,
)
