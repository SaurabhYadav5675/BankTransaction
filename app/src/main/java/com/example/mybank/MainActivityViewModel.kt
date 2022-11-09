package com.example.mybank

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.mybank.database.BankDatabase
import com.example.mybank.model.Transactions
import com.example.mybank.model.User
import com.google.android.material.textfield.TextInputLayout

class MainActivityViewModel(app: Application) : AndroidViewModel(app) {
    var allUser = MutableLiveData<List<User>>()
    val products: LiveData<List<User>>
        get() = allUser

    var allTrans = MutableLiveData<List<Transactions>>()

    val trans: LiveData<List<Transactions>>
        get() = allTrans

    fun getUserObserver(): MutableLiveData<List<User>> {
        return allUser
    }

    fun getAllUser() {
        val userDao = BankDatabase.getDatabase((getApplication())).userDao()
        val users = userDao.getAllUser()
        allUser.postValue(users)

    }

    //User data Dao Methods
    suspend fun insertUserInfo(entity: User) {
        val userDao = BankDatabase.getDatabase((getApplication())).userDao()
        userDao.insertUser(entity)
        getAllUser()
    }

    suspend fun updateUserInfo(
        userid: String,
        amount: String,
        rs100: String,
        rs200: String,
        rs500: String,
        rs2000: String
    ) {
        val userDao = BankDatabase.getDatabase((getApplication())).userDao()
        userDao.updateUserData(userid, amount, rs100, rs200, rs500, rs2000)
        getAllUser()
    }


    //Transaction data Dao Methods
    fun getTransObserver(): MutableLiveData<List<Transactions>> {
        return allTrans
    }

    fun getAllTrans() {
        val transDao = BankDatabase.getDatabase((getApplication())).transactionDao()
        val trans = transDao.getAllTransaction()
        allTrans.postValue(trans)

    }

    suspend fun insertTransInfo(entity: Transactions) {
        val transDao = BankDatabase.getDatabase((getApplication())).transactionDao()
        transDao.insertTransaction(entity)
        getAllTrans()
    }

    //validations

    fun checkWithdrawAmount(
        withdrawAmount: String,
        totalAmount: String,
        withdrawContainer: TextInputLayout
    ): Boolean {
        var validationStatus = false;
        var textError = withdrawContainer
        val digitsOnly = withdrawAmount?.toIntOrNull()?.let { true } ?: false

        if (!digitsOnly) {
            textError.error = "Please Enter Valid Amount"
        } else {
            val amountInt = withdrawAmount.toInt()
            if (amountInt <= 0) {
                textError.error = "Please Enter Valid Amount"
            } else if (amountInt > totalAmount.toInt()) {
                textError.error = "Insufficient Amount"
            } else {
                if ((amountInt % 100 == 0 || amountInt % 200 == 0) || (amountInt % 500 == 0 || amountInt % 2000 == 0)) {
                    textError.error = ""
                    validationStatus = true
                } else {
                    textError.error = "Amount must be multiple of 100,200,500 or 2000"
                }
            }
        }
        return validationStatus;
    }

    //start transaction
    suspend fun startTransaction(
        userId: String,
        totalAmount: Int,
        amount: Int,
        availableNotes: IntArray
    ) {
        val transAmount = amount
        var amount = amount
        val notes = intArrayOf(2000, 500, 200, 100)
        val noteCounter = IntArray(4)

        for (i in 0..3) {
            if (amount >= notes[i]) {
                var totalNotes = amount / notes[i]
                if (totalNotes <= availableNotes[i]) {
                    noteCounter[i] = amount / notes[i]
                    amount %= notes[i]
                } else {
                    noteCounter[i] = availableNotes[i]
                    amount -= (availableNotes[i] * notes[i])
                }
            }
        }

        //add transaction data
        val tranDetails = Transactions(
            0, userId, transAmount.toString(), noteCounter[3].toString(), noteCounter[2].toString(),
            noteCounter[1].toString(), noteCounter[0].toString(), ""
        )
        insertTransInfo(tranDetails)


        //update user data
        var userAmount = (totalAmount - transAmount).toString()
        var userRs100 = (availableNotes[3] - noteCounter[3]).toString()
        var userRs200 = (availableNotes[2] - noteCounter[2]).toString()
        var userRs500 = (availableNotes[1] - noteCounter[1]).toString()
        var userRs2000 = (availableNotes[0] - noteCounter[0]).toString()

        updateUserInfo(
            userId,
            userAmount,
            userRs100,
            userRs200,
            userRs500,
            userRs2000
        )
    }

}
