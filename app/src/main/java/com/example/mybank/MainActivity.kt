package com.example.mybank

import android.os.Bundle
import android.view.View
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.mybank.adapter.TransactionAdapter
import com.example.mybank.databinding.ActivityMainBinding
import com.example.mybank.model.Transactions
import com.example.mybank.model.User
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainActivityViewModel

    private lateinit var totalAmount: TextView
    private lateinit var rs100: TextView
    private lateinit var rs200: TextView
    private lateinit var rs500: TextView
    private lateinit var rs2000: TextView
    private lateinit var userId: String

    // Transaction History
    private lateinit var cardTransactions: CardView
    private lateinit var transLisView: ListView
    private lateinit var adapterTrans: TransactionAdapter

    @Suppress("EXPERIMENTAL_IS_NOT_ENABLED")
    @OptIn(DelicateCoroutinesApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        totalAmount = binding.headerTitle.txtTotalAmount
        rs100 = binding.headerTitle.txt100Count
        rs200 = binding.headerTitle.txt200Count
        rs500 = binding.headerTitle.txt500Count
        rs2000 = binding.headerTitle.txt2000Count

        cardTransactions = binding.cardTransactions
        transLisView = binding.listTransaction

        viewModel = ViewModelProvider(this).get(MainActivityViewModel::class.java)

        //get user data
        GlobalScope.launch { viewModel.getAllUser() }
        viewModel.getUserObserver().observe(this, Observer {
            if (it != null && it.isNotEmpty()) {
                userId = it[0].id.toString()
                totalAmount.text = it[0].total_amount
                rs100.text = it[0].rs100
                rs200.text = it[0].rs200
                rs500.text = it[0].rs500
                rs2000.text = it[0].rs2000
            } else {
                GlobalScope.launch {
                    val user = User(0, "Saurabh", "8097963430", "50000", "75", "50", "25", "10")
                    viewModel.insertUserInfo(user)
                }
            }
        })

        //get transaction data
        GlobalScope.launch { viewModel.getAllTrans() }
        viewModel.getTransObserver().observe(this, Observer {
            if (it != null && it.isNotEmpty()) {
                val transData: List<Transactions> = it

                //set last transaction
                val lastTrans = transData.size - 1
                binding.lastTransaction.txtTotalAmount.text =
                    transData[lastTrans].transaction_amount
                binding.lastTransaction.txt100Count.text =
                    transData[lastTrans].rs_100
                binding.lastTransaction.txt2000Count.text =
                    transData[lastTrans].rs_200
                binding.lastTransaction.txt500Count.text =
                    transData[lastTrans].rs_500
                binding.lastTransaction.txt2000Count.text =
                    transData[lastTrans].rs_2000

                cardTransactions.visibility = View.VISIBLE
                adapterTrans = TransactionAdapter(this, transData)
                transLisView.adapter = adapterTrans
            } else {
                cardTransactions.visibility = View.GONE
            }
        })


        binding.btnWithdraw.setOnClickListener {
            val withdrawAmount = binding.withdrawAmount.text.toString()
            val totalAmount = totalAmount.text.toString()
            val isValidAmount = viewModel.checkWithdrawAmount(
                withdrawAmount,
                totalAmount,
                binding.withdrawContainer
            )
            if (isValidAmount) {
                binding.withdrawAmount.text?.clear()
                val totalAmount = totalAmount.toInt()
                val amtWithdraw = withdrawAmount.toInt()
                val rs100Count = rs100.text.toString().toInt()
                val rs200Count = rs200.text.toString().toInt()
                val rs500Count = rs500.text.toString().toInt()
                val rs2000Count = rs2000.text.toString().toInt()

                val availableNotes = intArrayOf(rs2000Count, rs500Count, rs200Count, rs100Count)
                GlobalScope.launch {
                    viewModel.startTransaction(
                        userId, totalAmount,
                        amtWithdraw,
                        availableNotes
                    )
                }
            }
        }

    }
}