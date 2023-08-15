package com.mbs.mbsapp.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.mbs.mbsapp.Database.Entities.QuestionEntity
import com.mbs.mbsapp.Database.Entities.QuestionSectionEntity
import com.mbs.mbsapp.R

class SectionAdapter(
    var context: Context,
    var sectionHeaders: List<QuestionSectionEntity>,
    var superList: ArrayList<ArrayList<QuestionEntity>>
) : RecyclerView.Adapter<SectionAdapter.ViewHolder>() {
    class ViewHolder(itemview: View) : RecyclerView.ViewHolder(itemview) {

        lateinit var sectionHeader: TextView
        lateinit var recyclerView: RecyclerView


        fun bind() {
            sectionHeader = itemView.findViewById(R.id.sectionHeader)
            recyclerView = itemView.findViewById(R.id.recyclerview)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_section, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        try {
            holder.bind()

            holder.sectionHeader.setText(sectionHeaders[position].sectionName)
            holder.recyclerView.adapter = QuestionAdapter(context, superList[position])


        } catch (e: NullPointerException) {
            Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show()
        }


    }

    override fun getItemCount(): Int {
        return sectionHeaders.size
    }
}