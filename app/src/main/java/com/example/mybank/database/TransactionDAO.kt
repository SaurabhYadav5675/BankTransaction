package com.example.mybank.database

import androidx.room.*
import com.example.mybank.model.Transactions

@Dao
interface TransactionDAO {

    @Insert
    suspend fun insertTransaction(transactions: Transactions)

    @Update
    suspend fun updateTransaction(transaction: Transactions)

    @Delete
    suspend fun deleteTransaction(transaction: Transactions)

    @Query("SELECT * FROM transaction_details")
    fun getAllTransaction(): List<Transactions>

    @Query("SELECT * FROM transaction_details WHERE id like :transactionId")
    fun getTransaction(transactionId: String): List<Transactions>

    @Query("SELECT * FROM transaction_details ORDER BY id DESC LIMIT 1")
    fun getLatestTransaction(): List<Transactions>
}