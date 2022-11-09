package com.example.mybank.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.mybank.model.Transactions
import com.example.mybank.model.User

@Database(entities = [User::class, Transactions::class], version = 1)
abstract class BankDatabase : RoomDatabase() {

    abstract fun userDao(): UserDA0
    abstract fun transactionDao(): TransactionDAO

    companion object {
        @Volatile
        private var Instance: BankDatabase? = null


        fun getDatabase(context: Context): BankDatabase {
            if (Instance == null) {
                synchronized(this)
                {
                    Instance =
                        Room.databaseBuilder(
                            context.applicationContext,
                            BankDatabase::class.java,
                            "bankDB"
                        )
                            .build()
                }
            }
            return Instance!!
        }
    }
}