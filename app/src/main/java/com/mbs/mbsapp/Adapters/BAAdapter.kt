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
import com.mbs.mbsapp.Database.Entities.BrandAmbassadorEntity
import com.mbs.mbsapp.Database.Entities.ProductEntity
import com.mbs.mbsapp.Database.Entities.ProductStock
import com.mbs.mbsapp.Interfaces.iSetBA
import com.mbs.mbsapp.Model.BrandAmbassadorModel
import com.mbs.mbsapp.R

class BAAdapter(
    var context: Context,
    var balist: List<BrandAmbassadorEntity>,
    var iSetBA: iSetBA
) :
    RecyclerView.Adapter<BAAdapter.ViewHolder>() {
    class ViewHolder(itemview: View) : RecyclerView.ViewHolder(itemview) {

        lateinit var baName: TextView
        fun bind() {
            baName = itemView.findViewById(R.id.brandName)

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_ba, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        try {
            holder.bind()
            var mypos = position
            holder.baName.text = balist[position].baName

            holder.itemView.setOnClickListener {
                iSetBA.setBA(balist[position])
            }

        } catch (e: NullPointerException) {
            Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show()
        }


    }

    override fun getItemCount(): Int {
        return balist.size
    }
}