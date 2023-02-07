package com.example.picknumber.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bank")
data class Bank (
    @PrimaryKey(autoGenerate = true)
    val companyID: Int,

    @ColumnInfo(name = "divisionName")
    val divisionName: String,

    @ColumnInfo(name = "latitude")
    val latitude: Double,

    @ColumnInfo(name = "longitude")
    val longitude: Double
)