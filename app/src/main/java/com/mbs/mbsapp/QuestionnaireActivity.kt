package com.mbs.mbsapp

import android.app.Activity
import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.inksy.Database.MBSDatabase
import com.mbs.mbsapp.Adapters.SectionAdapter
import com.mbs.mbsapp.Database.Entities.AnswerDetailEntity
import com.mbs.mbsapp.Database.Entities.AnswerMasterEntity
import com.mbs.mbsapp.Database.Entities.QuestionEntity
import com.mbs.mbsapp.Database.Entities.QuestionSectionEntity
import com.mbs.mbsapp.Database.Entities.QuestionnaireEntity
import com.mbs.mbsapp.Interfaces.iTakePicture
import com.mbs.mbsapp.Utils.TinyDB
import com.mbs.mbsapp.databinding.ActivityQuestionnaireBinding
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.OutputStream


class QuestionnaireActivity : AppCompatActivity(), iTakePicture {
    lateinit var binding: ActivityQuestionnaireBinding
    lateinit var mbsDatabase: MBSDatabase
    lateinit var tinydb: TinyDB
    lateinit var questionnaireList: List<QuestionnaireEntity>
    lateinit var questionList: List<QuestionEntity>
    lateinit var section: List<QuestionSectionEntity>
    lateinit var superList: ArrayList<ArrayList<QuestionEntity>>
    lateinit var answerlist: ArrayList<AnswerDetailEntity>

    var itemNumber: Int = 0
    var questionId: Int = 0
    var SectionId: Int = 0
    var activityLogID: Int = 0
    lateinit var sectionadapter: SectionAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQuestionnaireBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mbsDatabase = MBSDatabase.getInstance(this)!!
        tinydb = TinyDB(this)
        binding.logout.visibility = View.GONE
        var campaignId = tinydb.getInt("campaignId")
        var activityId = tinydb.getInt("activitymasterid")
        var activitydetailid = tinydb.getInt("activitydetailid")
        activityLogID = tinydb.getInt("activityLogID")

        questionnaireList = mbsDatabase.getMBSData().getQuestionnaire(campaignId)
        questionList = mbsDatabase.getMBSData().getQuestion(questionnaireList[0].id!!)
        section = mbsDatabase.getMBSData().getQuestionSection()
        superList = ArrayList<ArrayList<QuestionEntity>>()

        var answers =
            mbsDatabase.getMBSData()
                .getanswersbyID(activitydetailid, questionnaireList[0].id!!, activityLogID)

        for (item in section.indices) {
            var list = ArrayList<QuestionEntity>()
            for (qitem in questionList.indices) {
                if (section[item].id == questionList[qitem].questionSectionId) {
                    list.add(questionList[qitem])
                }
            }
            superList.add(list)
        }

        sectionadapter =
            SectionAdapter(this@QuestionnaireActivity, section, superList, answers, this)


        binding.recyclerview.adapter = sectionadapter


        binding.back.setOnClickListener {
            backfromQuestionActivity(
                questionnaireList[0].id!!, campaignId, activityId, activitydetailid
            )
            startActivity(Intent(this@QuestionnaireActivity, Dashboard::class.java))
        }
        binding.logout.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            overridePendingTransition(R.anim.left, R.anim.left2);
            this.finish()
        }

        binding.close.setOnClickListener {
            binding.back.performClick()
            startActivity(Intent(this@QuestionnaireActivity, Dashboard::class.java))
        }

        binding.submit.setOnClickListener {
            Toast.makeText(this, "Questionnaire Submitted", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, Dashboard::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            overridePendingTransition(R.anim.left, R.anim.left2);
            this.finish()
        }
    }

    fun backfromQuestionActivity(
        questionnaireId: Int, campaignId: Int, activityId: Int, activitydetailid: Int
    ) {


        var answermaster = AnswerMasterEntity(
            0, questionnaireId, campaignId, activityLogID, activityId, 0, "", "", "", ""
        )
        mbsDatabase.getMBSData().insertAnswerMaster(answermaster)


        var data = superList
        var count = 0
        for (section in data) {
            for (question in section) {

                var answerDetailEntity = AnswerDetailEntity(
                    count,
                    0,
                    0,
                    activityId,
                    question.questionSectionId,
                    activitydetailid,
                    activityLogID,
                    questionnaireId,
                    question.id,
                    question.marksRecieved.toString(),
                    question.answerComment,
                    0,
                    0,
                    0,
                    "",
                    "",
                    "",
                    question.media1,
                    question.media2,
                    question.media3,
                    question.media4

                )
                count++
                mbsDatabase.getMBSData().insertAnswerDetail(answerDetailEntity)
            }
        }

        Toast.makeText(this@QuestionnaireActivity, "$count", Toast.LENGTH_SHORT).show()
    }

    override fun picture(position: Int, _itemNumber: Int, _questionId: Int, _SectionId: Int) {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.putExtra("android.intent.extras.CAMERA_FACING", 1);
        startActivityForResult(takePictureIntent, _itemNumber)

        itemNumber = _itemNumber
        questionId = _questionId
        SectionId = _SectionId
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as Bitmap?
            val displayName = "image_${System.currentTimeMillis()}"
            val uri =
                saveBitmapToGallery(this@QuestionnaireActivity, imageBitmap!!, displayName)
            if (requestCode == 1) {
                superList[SectionId][questionId].media1 = uri.toString()

                sectionadapter.notifyDataSetChanged()
            }
            if (requestCode == 2) {
                superList[SectionId][questionId].media2 = uri.toString()

                sectionadapter.notifyDataSetChanged()
            }
            if (requestCode == 3) {
                superList[SectionId][questionId].media3 = uri.toString()

                sectionadapter.notifyDataSetChanged()
            }
            if (requestCode == 4) {
                superList[SectionId][questionId].media4 = uri.toString()

                sectionadapter.notifyDataSetChanged()
            }

        }
    }

    fun saveBitmapToGallery(context: Context, bitmap: Bitmap, displayName: String): Uri? {
        val contentResolver: ContentResolver = context.contentResolver
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, "$displayName.jpg")
            put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
            put(MediaStore.MediaColumns.DATE_ADDED, System.currentTimeMillis() / 1000)
            put(MediaStore.MediaColumns.DATE_TAKEN, System.currentTimeMillis())
        }

        val imageUri =
            contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
        imageUri?.let {
            val outputStream: OutputStream? = contentResolver.openOutputStream(it)
            outputStream?.use { stream ->
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
            }
        }

        return imageUri
    }
}