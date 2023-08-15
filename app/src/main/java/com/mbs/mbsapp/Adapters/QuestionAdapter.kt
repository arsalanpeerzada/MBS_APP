package com.mbs.mbsapp.Adapters

import android.content.Context
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
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
import com.mbs.mbsapp.R

class QuestionAdapter(
    var context: Context,
    var questionEntity: List<QuestionEntity>
) : RecyclerView.Adapter<QuestionAdapter.ViewHolder>() {
    class ViewHolder(itemview: View) : RecyclerView.ViewHolder(itemview) {

        lateinit var question: TextView
        lateinit var marksOutof: TextView
        lateinit var marks: EditText
        lateinit var upload: Button
        lateinit var pictureLayout: LinearLayout
        var textWatcher: MaxInputTextWatcher? = null

        fun bind() {
            question = itemView.findViewById(R.id.question)
            marksOutof = itemView.findViewById(R.id.marksOutOf)
            marks = itemView.findViewById(R.id.marks)
            upload = itemView.findViewById(R.id.upload)
            pictureLayout = itemView.findViewById(R.id.pictureLayout)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_questionnaire, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        try {
            holder.bind()

            holder.question.text = questionEntity[position].question
            var marks = questionEntity[position].marks!!.toString()
            // holder.marks.setHint("0")
            holder.marksOutof.text = marks


            holder.marks.removeTextChangedListener(holder.textWatcher) // Remove existing TextWatcher if any
            holder.marks.hint = marks
            holder.textWatcher = MaxInputTextWatcher(holder.marks, questionEntity[position].marks!!)
            holder.marks.addTextChangedListener(holder.textWatcher)

            val maxLength = holder.marksOutof.text.toString().length
            val inputFilter = InputFilter.LengthFilter(maxLength)
            holder.marks.filters = arrayOf(inputFilter)

            if (questionEntity[position].isMediaAllowed == 0) {
                holder.pictureLayout.visibility = View.GONE
                holder.upload.visibility = View.GONE
            } else {
                holder.pictureLayout.visibility = View.GONE
                holder.upload.visibility = View.GONE
            }

            holder.upload.setOnClickListener {
                Toast.makeText(context, "Unable to access gallery", Toast.LENGTH_SHORT)
                    .show()
            }


        } catch (e: NullPointerException) {
            Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show()
        }


    }

    override fun getItemCount(): Int {
        return questionEntity.size
    }

    class MaxInputTextWatcher(private val editText: EditText, private val maxInput: Int) :
        TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

        override fun afterTextChanged(s: Editable?) {
            val enteredValue = s.toString()
            if (!enteredValue.isEmpty()) {
                val value = enteredValue.toIntOrNull()
                if (value != null && value > maxInput) {
                    editText.setText(maxInput.toString())
                    editText.setSelection(editText.length())
                }
            }
        }
    }
}