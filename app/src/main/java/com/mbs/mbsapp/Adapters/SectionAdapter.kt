package com.mbs.mbsapp.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mbs.mbsapp.Database.Entities.AnswerDetailEntity
import com.mbs.mbsapp.Database.Entities.QuestionEntity
import com.mbs.mbsapp.Interfaces.iTakePicture
import com.mbs.mbsapp.R


class SectionAdapter(
    var context: Context,
    var sectionHeaders: List<QuestionEntity>,
    var superList: ArrayList<ArrayList<QuestionEntity>>,
    var answers: List<AnswerDetailEntity>,
    var iTakePicture: iTakePicture
) : RecyclerView.Adapter<SectionAdapter.ViewHolder>() {
    class ViewHolder(itemview: View) : RecyclerView.ViewHolder(itemview) {

        lateinit var sectionHeader: TextView
        lateinit var recyclerView: RecyclerView


        fun bind() {
            sectionHeader = itemView.findViewById(R.id.sectionHeader)
            recyclerView = itemView.findViewById(R.id.recyclerview)
        }

        fun scrollChildRecyclerViewToEnd() {
            val layoutManager = recyclerView.layoutManager as LinearLayoutManager?
            if (layoutManager != null) {
                val itemCount = layoutManager.itemCount
                if (itemCount > 0) {
                    layoutManager.scrollToPositionWithOffset(itemCount - 1, 0)
                }
            }
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


            var list = ArrayList<AnswerDetailEntity>()
            var data = answers
            holder.sectionHeader.text = sectionHeaders[position].questionSectionName
            for (item in data) {
                if (sectionHeaders[position].questionSectionId == item.section_id) {
                    list.add(item)
                }
            }

//            for (item in superList){
//                for (item2 in item){
//
//                }
//            }





            holder.recyclerView.adapter =
                QuestionAdapter(
                    context,
                    superList[position],
                    list,
                    position,
                    object : iTakePicture {
                        override fun picture(
                            position: Int,
                            itemNumber: Int,
                            questionId: Int,
                            SectionId: Int
                        ) {
                            iTakePicture.picture(
                                position,
                                itemNumber,
                                questionId,
                                SectionId
                            )
                        }

                    })


        } catch (e: NullPointerException) {
            Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show()
        }


    }

    override fun getItemCount(): Int {
        return sectionHeaders.size
    }

}