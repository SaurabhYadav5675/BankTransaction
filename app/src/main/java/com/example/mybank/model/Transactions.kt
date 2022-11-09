package com.example.mybank.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "transaction_details")
data class Transactions(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val user_id: String,
    val transaction_amount: String,
    val rs_100: String,
    val rs_200: String,
    val rs_500: String,
    val rs_2000: String,
    val transaction_date: String
)
