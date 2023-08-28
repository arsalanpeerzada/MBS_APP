package com.mbs.mbsapp.Dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import com.mbs.mbsapp.Adapters.BAAdapter
import com.mbs.mbsapp.Database.Entities.BrandAmbassadorEntity
import com.mbs.mbsapp.Interfaces.iSetBA
import com.mbs.mbsapp.R
import com.mbs.mbsapp.databinding.BaDialogBinding

class BADialog(context: Context, var ba_list: List<BrandAmbassadorEntity>,var iSetBA: iSetBA) : Dialog(context) {

    lateinit var binding: BaDialogBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = BaDialogBinding.inflate(layoutInflater)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(binding.root)
        setCanceledOnTouchOutside(true)
        val window = window
        window!!.setLayout(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )

        binding.Barecyclerview.adapter = BAAdapter(context, ba_list,iSetBA)
    }


}