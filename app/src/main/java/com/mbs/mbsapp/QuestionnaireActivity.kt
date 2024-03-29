package com.mbs.mbsapp

import android.app.Activity
import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
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
import com.mbs.mbsapp.Database.Entities.QuestionnaireEntity
import com.mbs.mbsapp.Interfaces.iTakePicture
import com.mbs.mbsapp.Utils.TinyDB
import com.mbs.mbsapp.databinding.ActivityQuestionnaireBinding
import java.io.OutputStream
import java.util.ArrayList


class QuestionnaireActivity : AppCompatActivity(), iTakePicture {
    lateinit var binding: ActivityQuestionnaireBinding
    lateinit var mbsDatabase: MBSDatabase
    lateinit var tinydb: TinyDB
    lateinit var questionnaireList: List<QuestionnaireEntity>
    lateinit var questionList: List<QuestionEntity>
    lateinit var section: List<QuestionEntity>
    lateinit var superList: ArrayList<ArrayList<QuestionEntity>>
    var answerlist: List<AnswerDetailEntity> = ArrayList()

    var itemNumber: Int = 0
    var questionId: Int = 0
    var SectionId: Int = 0
    var Position: Int = 0
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
        section = mbsDatabase.getMBSData().getQuestionSection(questionnaireList[0].id!!)
        superList = ArrayList<ArrayList<QuestionEntity>>()

        var answers =
            mbsDatabase.getMBSData()
                .getanswersbyID(
                    activitydetailid,
                    questionnaireList[questionnaireList.size - 1].id!!,
                    activityLogID
                )
        answerlist = answers
//        for (questions in questionList) {
//            for (answer in answers) {
//                if (questions.mid == answer.mid) {
//                    if (!answer.answer.isNullOrEmpty() && !answer.answer.equals("0")) {
//                        questions.marksRecieved = answer.answer?.toInt()
//                        questions.answerComment = answer.answerComment.toString()
//                        questions.media1 = answer.media1
//                        questions.media2 = answer.media2
//                        questions.media3 = answer.media3
//                        questions.media4 = answer.media4
//                    }
//                }
//            }
//        }


        for (item in section.indices) {
            var list = ArrayList<QuestionEntity>()
            for (qitem in questionList.indices) {
                if (section[item].questionSectionId == questionList[qitem].questionSectionId) {
                    list.add(questionList[qitem])
                }
            }
            superList.add(list)
        }

        sectionadapter =
            SectionAdapter(this@QuestionnaireActivity, section, superList, answers, this)




        binding.recyclerview.adapter = sectionadapter
        //binding.recyclerview.scrollToPosition(sectionadapter.itemCount - 1)
        // binding.recyclerview.scrollToPosition(0)

        //scrollToChildRecyclerViewEnd()

        binding.back.setOnClickListener {
            binding.submit.performClick()
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


            backfromQuestionActivity(
                questionnaireList[0].id!!, campaignId, activityId, activitydetailid
            )

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


        var datasecond = answerlist

        if (datasecond.size == 0) {

            var data = superList
            var count = 0
            for (section in data) {
                for (question in section) {

                    var isMediaAttached = 0
                    var attachedMediaCount = 0

                    if (question.media1 != "") {
                        attachedMediaCount++
                    }
                    if (question.media2 != "") {
                        attachedMediaCount++
                    }
                    if (question.media3 != "") {
                        attachedMediaCount++
                    }
                    if (question.media4 != "") {
                        attachedMediaCount++
                    }

                    if (attachedMediaCount > 0) {
                        isMediaAttached = 1
                    }


                    var answerDetailEntity = AnswerDetailEntity(
                        count,
                        question.mid,
                        0,
                        activityId,
                        question.questionSectionId,
                        activitydetailid,
                        activityLogID,
                        questionnaireId,
                        question.id,
                        question.marksRecieved.toString(),
                        question.answerComment,
                        isMediaAttached,
                        attachedMediaCount,
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
        } else {
            var count = 0
            for (question in datasecond) {
//                for (question in section) {

                var isMediaAttached = 0
                var attachedMediaCount = 0

                if (question.media1 != "") {
                    attachedMediaCount++
                }
                if (question.media2 != "") {
                    attachedMediaCount++
                }
                if (question.media3 != "") {
                    attachedMediaCount++
                }
                if (question.media4 != "") {
                    attachedMediaCount++
                }

                if (attachedMediaCount > 0) {
                    isMediaAttached = 1
                }


                var answerDetailEntity = AnswerDetailEntity(
                    count,
                    questionList[count].mid,
                    0,
                    activityId,
                    question.section_id,
                    activitydetailid,
                    activityLogID,
                    questionnaireId,
                    question.question_id,
                    question.answer.toString(),
                    question.answerComment,
                    isMediaAttached,
                    attachedMediaCount,
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

        //Toast.makeText(this@QuestionnaireActivity, "$count", Toast.LENGTH_SHORT).show()
    }

    override fun picture(position: Int, _itemNumber: Int, _questionId: Int, _SectionId: Int) {
//        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
//        intent.putExtra("android.intent.extras.CAMERA_FACING", 1);
//        startActivityForResult(takePictureIntent, _itemNumber)

        val intent = Intent(this, CameraActivity::class.java)
        startActivityForResult(intent, _itemNumber)

        itemNumber = _itemNumber
        questionId = _questionId
        SectionId = _SectionId
        Position = position
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            var imageUri = data?.getStringExtra("imageUri")
            var URI = Uri.parse(imageUri)

            val imageBitmap  = uriToBitmap(URI)
            val displayName = "image_${System.currentTimeMillis()}"
            val uri =
                saveBitmapToGallery(this@QuestionnaireActivity, imageBitmap!!, displayName)


            if (requestCode == 1) {
                superList[SectionId][Position].media1 = uri.toString()
                for (item in answerlist) {
                    if (item.id == questionId) {
                        item.media1 = uri.toString()
                    }
                }

                sectionadapter.notifyItemChanged(SectionId)
            }
            if (requestCode == 2) {
                superList[SectionId][Position].media2 = uri.toString()
                for (item in answerlist) {
                    if (item.id == questionId) {
                        item.media2 = uri.toString()
                    }
                }
                sectionadapter.notifyItemChanged(SectionId)
            }
            if (requestCode == 3) {
                superList[SectionId][Position].media3 = uri.toString()
                for (item in answerlist) {
                    if (item.id == questionId) {
                        item.media3 = uri.toString()
                    }
                }
                sectionadapter.notifyItemChanged(SectionId)
            }
            if (requestCode == 4) {
                superList[SectionId][Position].media4 = uri.toString()
                for (item in answerlist) {
                    if (item.id == questionId) {
                        item.media4 = uri.toString()
                    }
                }
                sectionadapter.notifyItemChanged(SectionId)
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

    private fun scrollToChildRecyclerViewEnd() {
        // Replace 0 with the position of the parent item whose child RecyclerView you want to scroll
        val parentViewHolder: SectionAdapter.ViewHolder? =
            binding.recyclerview.findViewHolderForAdapterPosition(sectionadapter.itemCount - 1) as? SectionAdapter.ViewHolder

        if (parentViewHolder != null) {
            parentViewHolder.scrollChildRecyclerViewToEnd()
        } else {
            // Handle the case when the ViewHolder is not available yet
        }
    }

    fun uriToBitmap(uri: Uri): Bitmap? {
        try {
            // Use content resolver to open the input stream from the URI
            val inputStream = contentResolver.openInputStream(uri)
            // Decode the input stream into a Bitmap
            return BitmapFactory.decodeStream(inputStream)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    override fun onBackPressed() {

    }
}