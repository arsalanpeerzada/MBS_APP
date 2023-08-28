package com.mbs.mbsapp.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatSeekBar
import androidx.recyclerview.widget.RecyclerView
import com.mbs.mbsapp.Model.AudioModel
import com.mbs.mbsapp.R
import com.mbs.mbsapp.Interfaces.iAudioPlay

class AudioAdapter(
    var context: Context,
    var audioList: List<AudioModel>,
    var iAudioPlay: iAudioPlay
) :
    RecyclerView.Adapter<AudioAdapter.ViewHolder>() {
    class ViewHolder(itemview: View) : RecyclerView.ViewHolder(itemview) {

        lateinit var audioname: TextView
        lateinit var play: ImageView
        lateinit var seekBar: AppCompatSeekBar
        fun bind() {
            audioname = itemView.findViewById(R.id.audioname)
            play = itemView.findViewById(R.id.Play)
            seekBar = itemView.findViewById(R.id.appCompatSeekBar)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_audio, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        try {
            holder.bind()

            holder.audioname.text = audioList[position].audioName

            holder.play.setOnClickListener {
                iAudioPlay.playAudio(audioList[position].audioFilePath!!)
            }


        } catch (e: NullPointerException) {
            Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show()
        }


    }

    override fun getItemCount(): Int {
        return audioList.size
    }
}