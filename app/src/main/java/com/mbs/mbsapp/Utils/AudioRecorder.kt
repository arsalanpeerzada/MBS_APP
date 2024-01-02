package com.mbs.mbsapp.Utils

import android.media.MediaRecorder
import java.io.IOException


class AudioRecorder {
    private var mediaRecorder: MediaRecorder? = null
    private fun initMediaRecorder() {
        mediaRecorder = MediaRecorder()
        mediaRecorder!!.setAudioSource(MediaRecorder.AudioSource.MIC)
        mediaRecorder!!.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
        mediaRecorder!!.setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
    }

    @Throws(IOException::class)
    fun start(filePath: String?) {
        if (mediaRecorder == null) {
            initMediaRecorder()
        }
        mediaRecorder!!.setOutputFile(filePath)
        try {
            mediaRecorder!!.prepare()
            mediaRecorder!!.start()
        }catch (e:Exception){
            
        }

    }

    fun stop() {
        try {
            mediaRecorder!!.stop()
            destroyMediaRecorder()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun destroyMediaRecorder() {
        mediaRecorder!!.release()
        mediaRecorder = null
    }
}