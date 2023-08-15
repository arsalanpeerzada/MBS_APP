package com.mbs.mbsapp.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.mbs.mbsapp.Database.Entities.ProductEntity
import com.mbs.mbsapp.R

class StockAdapter(var context: Context, var productlist: List<ProductEntity>) :
    RecyclerView.Adapter<StockAdapter.ViewHolder>() {
    class ViewHolder(itemview: View) : RecyclerView.ViewHolder(itemview) {

        lateinit var productName: TextView
        fun bind() {
            productName = itemView.findViewById(R.id.productName)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_stock, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        try {
            holder.bind()

            holder.productName.text = productlist[position].productName


        } catch (e: NullPointerException) {
            Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show()
        }


    }

    override fun getItemCount(): Int {
        return productlist.size
    }
}