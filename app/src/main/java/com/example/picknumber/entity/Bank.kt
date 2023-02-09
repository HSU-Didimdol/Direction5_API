package com.example.picknumber.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bank")
data class Bank (
    @PrimaryKey(autoGenerate = false)
    val companyID: Int,

    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "divisionName")
    val divisionName: String,

    @ColumnInfo(name = "latitude")
    val longitude: Double,

    @ColumnInfo(name = "longitude")
    val latitude: Double
)