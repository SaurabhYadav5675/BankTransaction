package com.example.mybank.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.mybank.R
import com.example.mybank.model.Transactions

class TransactionAdapter(private val context: Context, private val transList: List<Transactions>) :
    BaseAdapter() {
    private val inflater: LayoutInflater =
        context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun getCount(): Int {
        return transList.size
    }

    override fun getItem(p0: Int): Any {
        return transList[p0]
    }

    override fun getItemId(p0: Int): Long {
        return p0.toLong()
    }

    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
        val transDetails = getItem(p0) as Transactions
        val itemView = inflater.inflate(R.layout.transaction_adapter, p2, false)
        val tAmount = itemView.findViewById<TextView>(R.id.txtTransAmount)
        val rs100 = itemView.findViewById<TextView>(R.id.txtTrans100)
        val rs200 = itemView.findViewById<TextView>(R.id.txtTrans200)
        val rs500 = itemView.findViewById<TextView>(R.id.txtTrans500)
        val rs2000 = itemView.findViewById<TextView>(R.id.txtTrans2000)

        tAmount.text = transDetails.transaction_amount
        rs100.text = transDetails.rs_100
        rs200.text = transDetails.rs_200
        rs500.text = transDetails.rs_500
        rs2000.text = transDetails.rs_2000
        return itemView
    }
}