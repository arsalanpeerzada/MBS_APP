package com.mbs.mbsapp

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.devlomi.record_view.OnRecordListener
import com.devlomi.record_view.RecordButton
import com.devlomi.record_view.RecordPermissionHandler
import com.devlomi.record_view.RecordView
import com.inksy.Database.MBSDatabase
import com.mbs.mbsapp.Adapters.AudioAdapter
import com.mbs.mbsapp.Database.Entities.BaPitchEntity
import com.mbs.mbsapp.Database.Entities.BrandAmbassadorEntity
import com.mbs.mbsapp.Dialog.BADialog
import com.mbs.mbsapp.Interfaces.iAudioPlay
import com.mbs.mbsapp.Interfaces.iSetBA
import com.mbs.mbsapp.Model.AudioModel
import com.mbs.mbsapp.Utils.AudioRecorder
import com.mbs.mbsapp.Utils.TinyDB
import com.mbs.mbsapp.databinding.ActivityBapitchBinding
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.File
import java.io.IOException
import java.util.UUID
import java.util.concurrent.TimeUnit


class BAPitch : AppCompatActivity(), iSetBA {

    lateinit var binding: ActivityBapitchBinding
    private var audioRecorder: AudioRecorder? = null
    private var recordFile: File? = null
    var list = ArrayList<AudioModel>()
    lateinit var audioAdapter: AudioAdapter
    var isBASelected: Boolean = false
    lateinit var tinydb: TinyDB
    lateinit var mbsDatabase: MBSDatabase
    lateinit var baentity: BrandAmbassadorEntity
    lateinit var baDialog: BADialog
    lateinit var recordView: RecordView
    lateinit var recordButton: RecordButton
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBapitchBinding.inflate(layoutInflater)
        setContentView(binding.root)
        tinydb = TinyDB(this)
        mbsDatabase = MBSDatabase.getInstance(this)!!
        audioRecorder = AudioRecorder()


        recordView = binding.recordView
        recordButton = binding.recordButton

        var campaignid = tinydb.getInt("campaignId")
        var activitymasterid = tinydb.getInt("activitymasterid")
        var activitydetailid = tinydb.getInt("activitydetailid")



        binding.back.setOnClickListener {
            binding.submit.performClick()
        }

        audioAdapter = AudioAdapter(this@BAPitch, list, object : iAudioPlay {
            override fun playAudio(_filepath: String) {
                val mediaPlayer = MediaPlayer()
                if (!mediaPlayer.isPlaying) {

                    var file = _filepath
                    mediaPlayer.setDataSource(file) // Set the data source to the recorded audio file

                    mediaPlayer.prepare()
                    mediaPlayer.start()
                    Toast.makeText(this@BAPitch, "Audio Started", Toast.LENGTH_SHORT).show()
                } else {
                    mediaPlayer.release();
                    Toast.makeText(this@BAPitch, "Audio Stopped", Toast.LENGTH_SHORT).show()
                }
            }

        })
        binding.blocker.setOnClickListener {
            Toast.makeText(this@BAPitch, "Select BA first", Toast.LENGTH_SHORT).show()
        }

        var getbapitchs = mbsDatabase.getMBSData().getBApitches(activitydetailid)

        for (item in getbapitchs) {
            var audioModel = AudioModel(item.bapMediaName, item.bapPath)
            list.add(audioModel)
            audioAdapter.notifyDataSetChanged()
        }




        binding.logout.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            overridePendingTransition(R.anim.left, R.anim.left2);
            this.finish()
        }

        binding.close.setOnClickListener {
            binding.submit.performClick()
        }

        binding.submit.setOnClickListener {
            Toast.makeText(this@BAPitch, "BA Pitch Submitted", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, Dashboard::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            overridePendingTransition(R.anim.left, R.anim.left2);
            this.finish()
        }

        binding.BASelect.setOnClickListener {

            var ba_list = mbsDatabase.getMBSData().getBA(activitydetailid, campaignid, activitymasterid)


            baDialog = BADialog(this@BAPitch, ba_list, this)

            baDialog.window?.setBackgroundDrawableResource(R.color.transparent)
            baDialog.show()
        }




        binding.audioRecycler.adapter = audioAdapter
        recordButton.isEnabled = false
        recordView.isEnabled = false
        binding.blocker.visibility = View.VISIBLE
        recordButton.setRecordView(recordView)
        recordView.setCustomSounds(R.raw.record_start, R.raw.record_finished, 0);

        recordView.setOnRecordListener(object : OnRecordListener {
            override fun onStart() {
                //Start Recording..
                if (isBASelected) {
                    Log.d("RecordView", "onStart")
                    recordFile = File(filesDir, "${baentity.baName}.mp3")
                    try {
                        audioRecorder!!.start(recordFile!!.path)
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                    Log.d("RecordView", "onStart")
                    Toast.makeText(this@BAPitch, "OnStartRecord", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    stopRecording(true);
                    Toast.makeText(this@BAPitch, "Select Brand Ambassador", Toast.LENGTH_SHORT)
                        .show()
                }
            }

            override fun onCancel() {
                //On Swipe To Cancel
                stopRecording(true);

                Toast.makeText(this@BAPitch, "onCancel", Toast.LENGTH_SHORT).show();
                Log.d("RecordView", "onCancel")
            }

            override fun onFinish(recordTime: Long, limitReached: Boolean) {
                //Stop Recording..
                //limitReached to determine if the Record was finished when time limit reached.


                stopRecording(false);
                val time: String = getHumanTimeText(recordTime)
                Toast.makeText(
                    this@BAPitch,
                    "File saved at " + recordFile?.path,
                    Toast.LENGTH_SHORT
                ).show();

                var audioModel = AudioModel(recordFile?.name, recordFile?.path)
                list.add(audioModel)

                var baPitch = BaPitchEntity(
                    baentity.id!!,
                    baentity.id!!,
                    baentity.id,
                    recordFile.toString(),
                    recordFile?.path.toString(),
                    recordFile?.name.toString(),
                    "",
                    1,
                    "",
                    "",

                    )

                mbsDatabase.getMBSData().insertBAPitch(baPitch)
                isBASelected = false
                binding.BASelect.setText("Click here to select BA")
                recordButton.isEnabled = false
                recordView.isEnabled = false
                binding.blocker.visibility = View.VISIBLE
                mbsDatabase.getMBSData().updatePitchCompleted(baentity.id!!, 1)
                audioAdapter.notifyDataSetChanged()
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
                this@BAPitch,
                Manifest.permission.RECORD_AUDIO
            ) == PERMISSION_GRANTED
            if (recordPermissionAvailable) {
                return@RecordPermissionHandler true
            }
            ActivityCompat.requestPermissions(
                this@BAPitch, arrayOf<String>(Manifest.permission.RECORD_AUDIO),
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

    override fun setBA(ba: BrandAmbassadorEntity) {
        isBASelected = true
        baentity = ba
        binding.BASelect.setText("You are recording for ${ba.baName}")
        recordButton.isEnabled = true
        recordView.isEnabled = true
        binding.blocker.visibility = View.GONE
        baDialog.dismiss()

    }
}