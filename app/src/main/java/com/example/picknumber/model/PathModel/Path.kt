package com.example.picknumber.model.PathModel

import com.example.picknumber.model.DistanceModel.Distance

data class Path(
    val summary: Distance,
    val path: List<List<Double>>
)
