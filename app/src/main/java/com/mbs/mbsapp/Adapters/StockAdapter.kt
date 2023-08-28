package com.mbs.mbsapp.Adapters

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.mbs.mbsapp.Database.Entities.ProductEntity
import com.mbs.mbsapp.Database.Entities.ProductStock
import com.mbs.mbsapp.R

class StockAdapter(
    var context: Context,
    var productlist: List<ProductEntity>,
    var getproducts: List<ProductStock>
) :
    RecyclerView.Adapter<StockAdapter.ViewHolder>() {
    class ViewHolder(itemview: View) : RecyclerView.ViewHolder(itemview) {

        lateinit var productName: TextView
        lateinit var productAnswer: EditText
        fun bind() {
            productName = itemView.findViewById(R.id.productName)
            productAnswer = itemView.findViewById(R.id.productanswer)
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
            var mypos = position
            holder.productName.text = productlist[position].productName
            holder.productAnswer.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                }

                override fun afterTextChanged(s: Editable?) {
                    if (!s.isNullOrEmpty()) {
                        productlist[mypos].productAnswer =
                            s.toString().toInt() // Update the data source
                    }
                }
            })

            for (item in getproducts) {
                if (productlist[position].id == item.productId) {
                    holder.productAnswer.setText(item.count.toString())
                }
            }


        } catch (e: NullPointerException) {
            Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show()
        }


    }

    override fun getItemCount(): Int {
        return productlist.size
    }
}