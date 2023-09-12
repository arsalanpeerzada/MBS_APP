package com.mbs.mbsapp.Dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.TextView
import com.mbs.mbsapp.Interfaces.OnDialogClickListener
import com.mbs.mbsapp.R

class TwoButtonDialog(
    var tvcancel: Boolean,
    context: Context,
    var title: String,
    var description: String,
    var positive: String,
    var negative: String,
    var listener: OnDialogClickListener
) : Dialog(context) {


    lateinit var tvCancel: TextView
    lateinit var tvConfirm: TextView
    lateinit var tvTitle: TextView
    lateinit var tvDescription: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)

        setContentView(R.layout.two_button_dialog)
        setCanceledOnTouchOutside(true)
        val window = window
        window!!.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
        tvTitle = findViewById<TextView>(R.id.tvTitle)
        tvDescription = findViewById<TextView>(R.id.tvDescription)
        tvCancel = findViewById<TextView>(R.id.tvCancel)
        tvConfirm = findViewById<TextView>(R.id.tvConfirm)
        tvTitle.text = title
        tvDescription.text = description
        tvConfirm.text = positive
        tvCancel.text = negative
        if (tvcancel) {
            tvCancel.visibility = View.VISIBLE
        } else {
            tvCancel.visibility = View.GONE
        }
        tvCancel.setOnClickListener(View.OnClickListener {
            listener.onDialogClick("No")
            dismiss()
        })
        tvConfirm.setOnClickListener(View.OnClickListener {
            listener.onDialogClick("Yes")
            dismiss()
        })
    }
}