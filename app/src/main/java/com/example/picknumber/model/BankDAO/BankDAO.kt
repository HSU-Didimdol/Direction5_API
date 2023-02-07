package com.example.picknumber.model.BankDAO

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Update
import com.example.picknumber.model.BankModel.Bank
import retrofit2.http.Query

@Dao
interface BankDAO {
    @Query("SELECT * FROM bank")
    fun getAllBank(): List<Bank>

    @Insert
    fun insertBank(bank: Bank)

    @Update
    fun updateBank(bank: Bank)

    @Delete
    fun deleteBank(bank: Bank)
}