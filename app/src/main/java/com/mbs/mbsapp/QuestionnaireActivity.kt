package com.mbs.mbsapp

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.devlomi.record_view.OnRecordListener
import com.devlomi.record_view.RecordPermissionHandler
import com.inksy.Database.MBSDatabase
import com.mbs.mbsapp.Adapters.SectionAdapter
import com.mbs.mbsapp.Database.Entities.QuestionEntity
import com.mbs.mbsapp.Utils.AudioRecorder
import com.mbs.mbsapp.Utils.TinyDB
import com.mbs.mbsapp.databinding.ActivityQuestionnaireBinding
import java.io.File
import java.io.IOException
import java.util.UUID
import java.util.concurrent.TimeUnit


class QuestionnaireActivity : AppCompatActivity() {
    lateinit var binding: ActivityQuestionnaireBinding
    lateinit var mbsDatabase: MBSDatabase
    lateinit var tinydb: TinyDB

    private var audioRecorder: AudioRecorder? = null
    private var recordFile: File? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQuestionnaireBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mbsDatabase = MBSDatabase.getInstance(this)!!
        tinydb = TinyDB(this)
        audioRecorder = AudioRecorder()

        val recordView = binding.recordView
        val recordButton = binding.recordButton

        recordButton.setRecordView(recordView)
        recordView.setCustomSounds(R.raw.record_start, R.raw.record_finished, 0);

        var campaignId = tinydb.getInt("campaignId")




        var questionnaireList = mbsDatabase.getMBSData().getQuestionnaire(campaignId)
        var questionList = mbsDatabase.getMBSData().getQuestion(questionnaireList[0].id!!)
        var section = mbsDatabase.getMBSData().getQuestionSection()
        var superList = ArrayList<ArrayList<QuestionEntity>>()

        for (item in section.indices) {
            var list = ArrayList<QuestionEntity>()
            for (qitem in questionList.indices) {
                if (section[item].id == questionList[qitem].questionSectionId) {
                    list.add(questionList[qitem])
                }
            }
            superList.add(list)
        }

        binding.recyclerview.adapter = SectionAdapter(this@QuestionnaireActivity,section, superList)


        binding.back.setOnClickListener {
            this@QuestionnaireActivity.finish()
        }


        binding.logout.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            overridePendingTransition(R.anim.left, R.anim.left2);
            this.finish()
        }

        binding.close.setOnClickListener {
            this@QuestionnaireActivity.finish()
        }

        binding.submit.setOnClickListener {
            Toast.makeText(this, "Questionnaire Submitted", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, Dashboard::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            overridePendingTransition(R.anim.left, R.anim.left2);
            this.finish()
        }

        recordView.setOnRecordListener(object : OnRecordListener {
            override fun onStart() {
                //Start Recording..
                Log.d("RecordView", "onStart")
                recordFile = File(filesDir, UUID.randomUUID().toString() + ".3gp")
                try {
                    audioRecorder!!.start(recordFile!!.path)
                } catch (e: IOException) {
                    e.printStackTrace()
                }
                Log.d("RecordView", "onStart")
                Toast.makeText(this@QuestionnaireActivity, "OnStartRecord", Toast.LENGTH_SHORT).show()
            }

            override fun onCancel() {
                //On Swipe To Cancel
                stopRecording(true);

                Toast.makeText(this@QuestionnaireActivity, "onCancel", Toast.LENGTH_SHORT).show();
                Log.d("RecordView", "onCancel")
            }

            override fun onFinish(recordTime: Long, limitReached: Boolean) {
                //Stop Recording..
                //limitReached to determine if the Record was finished when time limit reached.

                stopRecording(false);
                val time: String = getHumanTimeText(recordTime)
                Log.d("RecordView", "onFinish")
                Log.d("RecordTime", time)
            }

            override fun onLessThanSecond() {
                //When the record time is less than One Second
                Log.d("RecordView", "onLessThanSecond")
            }

            override fun onLock() {
                //When Lock gets activated
                Log.d("RecordView", "onLock")
            }
        })

        recordView.setOnBasketAnimationEndListener {
            Log.d(
                "RecordView",
                "Basket Animation Finished"
            )
        }

        recordView.setRecordPermissionHandler(RecordPermissionHandler {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                return@RecordPermissionHandler true
            }
            val recordPermissionAvailable = ContextCompat.checkSelfPermission(
                this@QuestionnaireActivity,
                Manifest.permission.RECORD_AUDIO
            ) == PERMISSION_GRANTED
            if (recordPermissionAvailable) {
                return@RecordPermissionHandler true
            }
            ActivityCompat.requestPermissions(
                this@QuestionnaireActivity, arrayOf<String>(Manifest.permission.RECORD_AUDIO),
                0
            )
            false
        })
    }



    private fun getHumanTimeText(milliseconds: Long): String {
        return java.lang.String.format(
            "%02d:%02d",
            TimeUnit.MILLISECONDS.toMinutes(milliseconds),
            TimeUnit.MILLISECONDS.toSeconds(milliseconds) -
                    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(milliseconds))
        )
    }
    private fun stopRecording(deleteFile: Boolean) {
        audioRecorder!!.stop()
        if (recordFile != null && deleteFile) {
            recordFile!!.delete()
        }
    }



}