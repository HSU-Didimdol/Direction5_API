package com.example.picknumber.model.SearchModel

import java.io.Serializable

data class Search(
    var distance: Int,
    val bankName: String,
) : Serializable
