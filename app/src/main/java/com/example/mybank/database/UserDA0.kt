package com.example.mybank.database

import androidx.room.*
import com.example.mybank.model.User

@Dao
interface UserDA0 {

    @Insert
    suspend fun insertUser(user: User)

    @Update
    suspend fun updateUser(user: User)

    @Query("UPDATE user SET total_amount = :amount,rs100=:rs1,rs200=:rs2,rs500=:rs3,rs2000=:rs4 WHERE id = :userId")
    suspend fun updateUserData(
        userId: String,
        amount: String,
        rs1: String,
        rs2: String,
        rs3: String,
        rs4: String
    )

    @Delete
    suspend fun deleteUser(user: User)

    @Query("SELECT * FROM user")
    fun getAllUser(): List<User>

    @Query("SELECT * FROM user WHERE id like :userId")
    fun getUser(userId: String): List<User>
}