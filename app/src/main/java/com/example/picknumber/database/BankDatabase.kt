package com.example.picknumber.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.picknumber.entity.Bank
import com.example.picknumber.model.BankDAO.BankDAO

@Database(entities = [Bank::class], version = 1)
abstract class BankDatabase: RoomDatabase() {
    abstract fun bankDao(): BankDAO

    companion object {
        private var INSTANCE: BankDatabase? = null

        fun getDatabase(context: Context): BankDatabase? {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(
                    context.applicationContext,
                    BankDatabase::class.java,
                    "bank_database")
                    .allowMainThreadQueries()
                    .build()
            }
            return INSTANCE
        }
    }
}