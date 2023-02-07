package com.example.picknumber.model.BankDAO

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.picknumber.entity.Bank

@Dao
interface BankDAO {
    @Query("SELECT * from bank")
    fun getAllBank(): List<Bank>

    @Insert
    fun insertBank(bank: Bank)

    @Update
    fun updateBank(bank: Bank)

    @Delete
    fun deleteBank(bank: Bank)
}