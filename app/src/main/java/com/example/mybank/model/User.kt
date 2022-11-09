package com.example.mybank.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
data class User(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val name: String,
    val contact: String,
    val total_amount: String,
    val rs100: String,
    val rs200: String,
    val rs500: String,
    val rs2000: String
)
