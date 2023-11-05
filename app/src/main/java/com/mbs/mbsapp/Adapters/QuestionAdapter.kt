package com.mbs.mbsapp.Adapters

import android.content.Context
import android.net.Uri
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
import com.bumptech.glide.Glide
import com.mbs.mbsapp.Database.Entities.AnswerDetailEntity
import com.mbs.mbsapp.Database.Entities.QuestionEntity
import com.mbs.mbsapp.R
import com.mbs.mbsapp.Interfaces.iTakePicture
import java.util.ArrayList

class QuestionAdapter(
    var context: Context,
    var questionEntity: List<QuestionEntity>,
    var list: ArrayList<AnswerDetailEntity>,
    var sectionid: Int,
    var iTakePicture: iTakePicture
) : RecyclerView.Adapter<QuestionAdapter.ViewHolder>() {
    class ViewHolder(itemview: View) : RecyclerView.ViewHolder(itemview) {

        lateinit var question: TextView
        lateinit var marksOutof: TextView
        lateinit var marks: EditText
        lateinit var upload: Button
        lateinit var pictureLayout: LinearLayout
        var textWatcher: MaxInputTextWatcher? = null
        lateinit var comments: EditText
        lateinit var imageView1: ImageView
        lateinit var imageView2: ImageView
        lateinit var imageView3: ImageView
        lateinit var imageView4: ImageView
        fun bind() {
            question = itemView.findViewById(R.id.question)
            marksOutof = itemView.findViewById(R.id.marksOutOf)
            marks = itemView.findViewById(R.id.marks)
            upload = itemView.findViewById(R.id.upload)
            pictureLayout = itemView.findViewById(R.id.pictureLayout)
            comments = itemView.findViewById(R.id.comments)
            imageView1 = itemView.findViewById(R.id.imageView1)
            imageView2 = itemView.findViewById(R.id.imageView2)
            imageView3 = itemView.findViewById(R.id.imageView3)
            imageView4 = itemView.findViewById(R.id.imageView4)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v =
            LayoutInflater.from(parent.context).inflate(R.layout.item_questionnaire, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        try {
            holder.bind()
            var mypos = position
            holder.question.text = questionEntity[position].question
            var marks = questionEntity[position].marks!!.toString()
            holder.marksOutof.text = marks




            holder.marks.removeTextChangedListener(holder.textWatcher) // Remove existing TextWatcher if any
            holder.marks.hint = marks

            holder.textWatcher = MaxInputTextWatcher(holder.marks, questionEntity[position].marks!!)
            holder.marks.addTextChangedListener(holder.textWatcher)

            holder.marks.setText(questionEntity[position].marksRecieved!!.toString())

            holder.marks.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?, start: Int, count: Int, after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    var maxInput = questionEntity[mypos].marks!!
                    if (!s.isNullOrEmpty()) {
                        var enteredValue = s.toString()
                        if (!enteredValue.isEmpty()) {
                            var value = enteredValue.toIntOrNull()
                            if (value != null && value > maxInput) {
                                enteredValue = maxInput.toString()
                            }
                        }
                        questionEntity[mypos].marksRecieved =
                            enteredValue.toInt() // Update the data source

                        if (list.size > 0) list[position].answer = enteredValue
                    }
                }

                override fun afterTextChanged(s: Editable?) {
                    var maxInput = questionEntity[mypos].marks!!
                    if (!s.isNullOrEmpty()) {
                        var enteredValue = s.toString()
                        if (!enteredValue.isEmpty()) {
                            var value = enteredValue.toIntOrNull()
                            if (value != null && value > maxInput) {
                                enteredValue = maxInput.toString()
                            }
                        }
                        questionEntity[mypos].marksRecieved =
                            enteredValue.toInt() // Update the data source

                        if (list.size > 0) list[position].answer = enteredValue
                    }
                }
            })

            holder.comments.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?, start: Int, count: Int, after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    if (!s.isNullOrEmpty()) {
                        questionEntity[mypos].answerComment = s.toString()
                    }// Update the data source

                    if (list.size > 0) list[position].answerComment = s.toString()
                }

                override fun afterTextChanged(s: Editable?) {
                    if (!s.isNullOrEmpty()) {
                        questionEntity[mypos].answerComment = s.toString()
                    }// Update the data source

                    if (list.size > 0) list[position].answerComment = s.toString()
                }
            })

            val maxLength = holder.marksOutof.text.toString().length
            val inputFilter = InputFilter.LengthFilter(maxLength)
            holder.marks.filters = arrayOf(inputFilter)

            if (questionEntity[position].isMediaAllowed == 0) {
                holder.pictureLayout.visibility = View.GONE
                holder.upload.visibility = View.GONE
            } else {
                holder.pictureLayout.visibility = View.VISIBLE
                holder.upload.visibility = View.VISIBLE
            }

            if (list.size > 0) {
                holder.marks.setText(list[position].answer)
                holder.comments.setText(list[position].answerComment)

                if (list[position].media1 != "") {
                    var uri = Uri.parse(list[position].media1!!)
                    questionEntity[position].media1 = list[position].media1!!
                    Glide.with(context).load(uri).into(holder.imageView1)
                }

                if (list[position].media2 != "") {
                    var uri = Uri.parse(list[position].media2!!)
                    questionEntity[position].media2 = list[position].media2!!
                    Glide.with(context).load(uri).into(holder.imageView2)
                }
                if (list[position].media3 != "") {
                    var uri = Uri.parse(list[position].media3!!)
                    questionEntity[position].media3 = list[position].media3!!
                    Glide.with(context).load(uri).into(holder.imageView3)
                }
                if (list[position].media4 != "") {
                    var uri = Uri.parse(list[position].media4!!)
                    questionEntity[position].media4 = list[position].media4!!
                    Glide.with(context).load(uri).into(holder.imageView4)
                }
            }

            if (!questionEntity[position].media1.isNullOrEmpty()) {
                var uri = Uri.parse(questionEntity[position].media1!!)

                Glide.with(context).load(uri).into(holder.imageView1)

            }

            if (!questionEntity[position].media2.isNullOrEmpty()) {
                var uri = Uri.parse(questionEntity[position].media2!!)

                Glide.with(context).load(uri).into(holder.imageView2)

            }

            if (!questionEntity[position].media3.isNullOrEmpty()) {
                var uri = Uri.parse(questionEntity[position].media3!!)

                Glide.with(context).load(uri).into(holder.imageView3)

            }

            if (!questionEntity[position].media4.isNullOrEmpty()) {
                var uri = Uri.parse(questionEntity[position].media4!!)

                Glide.with(context).load(uri).into(holder.imageView4)

            }

            holder.upload.setOnClickListener {

                if (questionEntity[position].media1 == "") {
                    iTakePicture.picture(position, 1, questionEntity[position].mid!!, sectionid)
                } else if (questionEntity[position].media2 == "") iTakePicture.picture(
                    position,
                    2,
                    questionEntity[position].mid!!,
                    sectionid
                )
                else if (questionEntity[position].media3 == "") iTakePicture.picture(
                    position,
                    3,
                    questionEntity[position].mid!!,
                    sectionid
                )
                else if (questionEntity[position].media4 == "") iTakePicture.picture(
                    position,
                    4,
                    questionEntity[position].mid!!,
                    sectionid
                )
            }


            holder.imageView1.setOnClickListener() {
                Glide.with(context).load(R.drawable.image).into(holder.imageView1)
                questionEntity[position].media1 = ""
                if (list.size > 0) list[position].media1 = ""
            }

            holder.imageView2.setOnClickListener() {
                Glide.with(context).load(R.drawable.image).into(holder.imageView2)
                questionEntity[position].media2 = ""
                if (list.size > 0) list[position].media2 = ""
            }

            holder.imageView3.setOnClickListener() {
                Glide.with(context).load(R.drawable.image).into(holder.imageView3)
                questionEntity[position].media3 = ""
                if (list.size > 0) list[position].media3 = ""
            }

            holder.imageView4.setOnClickListener() {
                Glide.with(context).load(R.drawable.image).into(holder.imageView4)
                questionEntity[position].media4 = ""
                if (list.size > 0) list[position].media4 = ""
            }


        } catch (e: NullPointerException) {
            Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show()
        }

        if (holder.marks.text.toString() == "0") {
            holder.marks.text.clear();
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