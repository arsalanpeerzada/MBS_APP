package com.mbs.mbsapp

import android.app.Activity
import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import com.bumptech.glide.Glide
import com.inksy.Database.MBSDatabase
import com.mbs.mbsapp.Database.Entities.MediaEntity
import com.mbs.mbsapp.Utils.Constants
import com.mbs.mbsapp.Utils.TinyDB
import com.mbs.mbsapp.databinding.ActivityStorePictureBinding
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream

class StorePictureActivity : AppCompatActivity() {
    lateinit var binding: ActivityStorePictureBinding
    var count = 1
    lateinit var mbsDatabase: MBSDatabase
    lateinit var tinyDB: TinyDB
    var activityLogId: Int = 0
    var mediaSize = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStorePictureBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mbsDatabase = MBSDatabase.getInstance(this)!!
        tinyDB = TinyDB(this@StorePictureActivity)
        var campaignid = tinyDB.getInt("campaignId")
        var activitylog = mbsDatabase.getMBSData().getactivityLogs(campaignid)
        activityLogId = activitylog[activitylog.size - 1].id!!
        binding.back.setOnClickListener {
            binding.submit.performClick()
        }

        var data = mbsDatabase.getMBSData().getmedia()
        mediaSize = data.size
        count = mediaSize

        var picturesdata =
            mbsDatabase.getMBSData().getmedia(activityLogId, Constants.store_location_pictures_num)

        // count = picturesdata.size
        setdata(picturesdata)

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
            Toast.makeText(this@StorePictureActivity, "Pictures Submitted", Toast.LENGTH_SHORT)
                .show()
            val intent = Intent(this, Dashboard::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            overridePendingTransition(R.anim.left, R.anim.left2);
            this.finish()

        }
        binding.Upload.setOnClickListener {

            if (count > 16) {
                Toast.makeText(
                    this@StorePictureActivity, "Can't Upload More Photos", Toast.LENGTH_SHORT
                ).show()
            }


            when (count) {
                1 -> dispatchTakePictureIntent(1)
                2 -> dispatchTakePictureIntent(2)
                3 -> dispatchTakePictureIntent(3)
                4 -> dispatchTakePictureIntent(4)
                5 -> dispatchTakePictureIntent(5)
                6 -> dispatchTakePictureIntent(6)
                7 -> dispatchTakePictureIntent(7)
                8 -> dispatchTakePictureIntent(8)
                9 -> dispatchTakePictureIntent(9)
                10 -> dispatchTakePictureIntent(10)
                11 -> dispatchTakePictureIntent(11)
                12 -> dispatchTakePictureIntent(12)
                13 -> dispatchTakePictureIntent(13)
                14 -> dispatchTakePictureIntent(14)
                15 -> dispatchTakePictureIntent(15)
                16 -> dispatchTakePictureIntent(16)
            }


        }
    }


    private fun dispatchTakePictureIntent(request: Int) {
//        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
//        intent.putExtra("android.intent.extras.CAMERA_FACING", 1);
//        startActivityForResult(takePictureIntent, request)

        val intent = Intent(this, CameraActivity::class.java)
        startActivityForResult(intent, request)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 1) {

                var imageUri = data?.getStringExtra("imageUri")
                var URI = Uri.parse(imageUri)


                val imageBitmap = uriToBitmap(URI)

                val displayName = "image_${System.currentTimeMillis()}"
                val uri = saveBitmapToGallery(this@StorePictureActivity, imageBitmap!!, displayName)

                insertIntoDB(uri!!, requestCode)


                binding.cardview1.visibility = View.VISIBLE
                binding.imageView1.setImageBitmap(imageBitmap)
                binding.imageView1.isEnabled = false
                saveImageToFolder(imageBitmap!!)
                count++


            }
            if (requestCode == 2) {
                var imageUri = data?.getStringExtra("imageUri")
                var URI = Uri.parse(imageUri)


                val imageBitmap = uriToBitmap(URI)

                val displayName = "image_${System.currentTimeMillis()}"
                val uri = saveBitmapToGallery(this@StorePictureActivity, imageBitmap!!, displayName)


                insertIntoDB(uri!!, requestCode)

                binding.cardview2.visibility = View.VISIBLE
                binding.imageView2.setImageBitmap(imageBitmap)
                binding.imageView2.isEnabled = false
                saveImageToFolder(imageBitmap!!)
                count++


            }

            if (requestCode == 3) {
                var imageUri = data?.getStringExtra("imageUri")
                var URI = Uri.parse(imageUri)


                val imageBitmap = uriToBitmap(URI)

                val displayName = "image_${System.currentTimeMillis()}"
                val uri = saveBitmapToGallery(this@StorePictureActivity, imageBitmap!!, displayName)

                insertIntoDB(uri!!, requestCode)
                binding.cardview3.visibility = View.VISIBLE
                binding.imageView3.setImageBitmap(imageBitmap)
                binding.imageView3.isEnabled = false
                saveImageToFolder(imageBitmap!!)
                count++
            }

            if (requestCode == 4) {
                var imageUri = data?.getStringExtra("imageUri")
                var URI = Uri.parse(imageUri)


                val imageBitmap = uriToBitmap(URI)

                val displayName = "image_${System.currentTimeMillis()}"
                val uri = saveBitmapToGallery(this@StorePictureActivity, imageBitmap!!, displayName)

                insertIntoDB(uri!!, requestCode)
                binding.cardview4.visibility = View.VISIBLE
                binding.imageView4.setImageBitmap(imageBitmap)
                binding.imageView4.isEnabled = false
                saveImageToFolder(imageBitmap!!)
                count++
            }
            if (requestCode == 5) {
                var imageUri = data?.getStringExtra("imageUri")
                var URI = Uri.parse(imageUri)


                val imageBitmap = uriToBitmap(URI)

                val displayName = "image_${System.currentTimeMillis()}"
                val uri = saveBitmapToGallery(this@StorePictureActivity, imageBitmap!!, displayName)
                insertIntoDB(uri!!, requestCode)
                binding.cardview5.visibility = View.VISIBLE
                binding.imageView5.setImageBitmap(imageBitmap)
                binding.imageView5.isEnabled = false
                saveImageToFolder(imageBitmap!!)
                count++
            }
            if (requestCode == 6) {
                var imageUri = data?.getStringExtra("imageUri")
                var URI = Uri.parse(imageUri)


                val imageBitmap = uriToBitmap(URI)

                val displayName = "image_${System.currentTimeMillis()}"
                val uri = saveBitmapToGallery(this@StorePictureActivity, imageBitmap!!, displayName)
                insertIntoDB(uri!!, requestCode)
                binding.cardview6.visibility = View.VISIBLE
                binding.imageView6.setImageBitmap(imageBitmap)
                binding.imageView6.isEnabled = false
                saveImageToFolder(imageBitmap!!)
                count++
            }
            if (requestCode == 7) {
                var imageUri = data?.getStringExtra("imageUri")
                var URI = Uri.parse(imageUri)


                val imageBitmap = uriToBitmap(URI)

                val displayName = "image_${System.currentTimeMillis()}"
                val uri = saveBitmapToGallery(this@StorePictureActivity, imageBitmap!!, displayName)
                insertIntoDB(uri!!, requestCode)
                binding.cardview7.visibility = View.VISIBLE
                binding.imageView7.setImageBitmap(imageBitmap)
                binding.imageView7.isEnabled = false
                saveImageToFolder(imageBitmap!!)
                count++
            }
            if (requestCode == 8) {
                var imageUri = data?.getStringExtra("imageUri")
                var URI = Uri.parse(imageUri)


                val imageBitmap = uriToBitmap(URI)

                val displayName = "image_${System.currentTimeMillis()}"
                val uri = saveBitmapToGallery(this@StorePictureActivity, imageBitmap!!, displayName)
                insertIntoDB(uri!!, requestCode)
                binding.cardview8.visibility = View.VISIBLE
                binding.imageView8.setImageBitmap(imageBitmap)
                binding.imageView8.isEnabled = false
                saveImageToFolder(imageBitmap!!)
                count++
            }
            if (requestCode == 9) {
                var imageUri = data?.getStringExtra("imageUri")
                var URI = Uri.parse(imageUri)


                val imageBitmap = uriToBitmap(URI)

                val displayName = "image_${System.currentTimeMillis()}"
                val uri = saveBitmapToGallery(this@StorePictureActivity, imageBitmap!!, displayName)
                insertIntoDB(uri!!, requestCode)
                binding.cardview9.visibility = View.VISIBLE
                binding.imageView9.setImageBitmap(imageBitmap)
                binding.imageView9.isEnabled = false
                saveImageToFolder(imageBitmap!!)
                count++
            }
            if (requestCode == 10) {
                var imageUri = data?.getStringExtra("imageUri")
                var URI = Uri.parse(imageUri)


                val imageBitmap = uriToBitmap(URI)

                val displayName = "image_${System.currentTimeMillis()}"
                val uri = saveBitmapToGallery(this@StorePictureActivity, imageBitmap!!, displayName)
                insertIntoDB(uri!!, requestCode)
                binding.cardview10.visibility = View.VISIBLE
                binding.imageView10.setImageBitmap(imageBitmap)
                binding.imageView10.isEnabled = false
                saveImageToFolder(imageBitmap!!)
                count++
            }
            if (requestCode == 11) {
                var imageUri = data?.getStringExtra("imageUri")
                var URI = Uri.parse(imageUri)


                val imageBitmap = uriToBitmap(URI)

                val displayName = "image_${System.currentTimeMillis()}"
                val uri = saveBitmapToGallery(this@StorePictureActivity, imageBitmap!!, displayName)
                insertIntoDB(uri!!, requestCode)
                binding.cardview11.visibility = View.VISIBLE
                binding.imageView11.setImageBitmap(imageBitmap)
                binding.imageView11.isEnabled = false
                saveImageToFolder(imageBitmap!!)
                count++
            }
            if (requestCode == 12) {
                var imageUri = data?.getStringExtra("imageUri")
                var URI = Uri.parse(imageUri)


                val imageBitmap = uriToBitmap(URI)

                val displayName = "image_${System.currentTimeMillis()}"
                val uri = saveBitmapToGallery(this@StorePictureActivity, imageBitmap!!, displayName)
                insertIntoDB(uri!!, requestCode)
                binding.cardview12.visibility = View.VISIBLE
                binding.imageView12.setImageBitmap(imageBitmap)
                binding.imageView12.isEnabled = false
                saveImageToFolder(imageBitmap!!)
                count++
            }
            if (requestCode == 13) {
                var imageUri = data?.getStringExtra("imageUri")
                var URI = Uri.parse(imageUri)


                val imageBitmap = uriToBitmap(URI)

                val displayName = "image_${System.currentTimeMillis()}"
                val uri = saveBitmapToGallery(this@StorePictureActivity, imageBitmap!!, displayName)
                insertIntoDB(uri!!, requestCode)
                binding.cardview13.visibility = View.VISIBLE
                binding.imageView13.setImageBitmap(imageBitmap)
                binding.imageView13.isEnabled = false
                saveImageToFolder(imageBitmap!!)
                count++
            }
            if (requestCode == 14) {
                var imageUri = data?.getStringExtra("imageUri")
                var URI = Uri.parse(imageUri)


                val imageBitmap = uriToBitmap(URI)

                val displayName = "image_${System.currentTimeMillis()}"
                val uri = saveBitmapToGallery(this@StorePictureActivity, imageBitmap!!, displayName)
                insertIntoDB(uri!!, requestCode)
                binding.cardview14.visibility = View.VISIBLE
                binding.imageView14.setImageBitmap(imageBitmap)
                binding.imageView14.isEnabled = false
                saveImageToFolder(imageBitmap!!)
                count++
            }
            if (requestCode == 15) {
                var imageUri = data?.getStringExtra("imageUri")
                var URI = Uri.parse(imageUri)


                val imageBitmap = uriToBitmap(URI)

                val displayName = "image_${System.currentTimeMillis()}"
                val uri = saveBitmapToGallery(this@StorePictureActivity, imageBitmap!!, displayName)
                insertIntoDB(uri!!, requestCode)
                binding.cardview15.visibility = View.VISIBLE
                binding.imageView15.setImageBitmap(imageBitmap)
                binding.imageView15.isEnabled = false
                saveImageToFolder(imageBitmap!!)
                count++
            }
            if (requestCode == 16) {
                var imageUri = data?.getStringExtra("imageUri")
                var URI = Uri.parse(imageUri)


                val imageBitmap = uriToBitmap(URI)

                val displayName = "image_${System.currentTimeMillis()}"
                val uri = saveBitmapToGallery(this@StorePictureActivity, imageBitmap!!, displayName)
                insertIntoDB(uri!!, requestCode)
                binding.cardview16.visibility = View.VISIBLE
                binding.imageView16.setImageBitmap(imageBitmap)
                binding.imageView16.isEnabled = false
                saveImageToFolder(imageBitmap!!)
                count++
            }

        }

    }

    private fun saveImageToFolder(bitmap: Bitmap) {
        val folderName = "YourFolderName"
        val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val folder = File(storageDir, folderName)
        if (!folder.exists()) {
            folder.mkdirs()
        }

        val fileName = "y_picture.jpg"
        val pictureFile = File(folder, fileName)


        val bos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, bos) // YOU can also save it in JPEG

        val bitmapdata = bos.toByteArray()
        val fos = FileOutputStream(pictureFile)
        fos.write(bitmapdata)
        fos.flush()
        fos.close()
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

    fun insertIntoDB(uri: Uri, requestCode: Int) {

        var count = mediaSize + requestCode
        GlobalScope.launch {
            var mediaEntity = MediaEntity(
                count,
                Constants.store_location_pictures_num,
                Constants.store_location_pictures_name,
                activityLogId,
                0,
                "",
                "picture",
                "",
                uri.toString(),
                "",
                "",
                "",
                "",
                ""
            )

            var data = mbsDatabase.getMBSData().insertMedia(mediaEntity)
        }
    }

    private fun setdata(picturesdata: List<MediaEntity>) {

        for (item in picturesdata) {
            when (item.mid?.minus(3)) {
                1 -> {
                    var uri = Uri.parse(item.media_name)
                    Glide.with(this@StorePictureActivity).load(uri).into(binding.imageView1)
                    binding.cardview1.visibility = View.VISIBLE
                }

                2 -> {
                    var uri = Uri.parse(item.media_name)
                    Glide.with(this@StorePictureActivity).load(uri).into(binding.imageView2)
                    binding.cardview2.visibility = View.VISIBLE
                }

                3 -> {
                    var uri = Uri.parse(item.media_name)
                    Glide.with(this@StorePictureActivity).load(uri).into(binding.imageView3)
                    binding.cardview3.visibility = View.VISIBLE
                }

                4 -> {
                    var uri = Uri.parse(item.media_name)
                    Glide.with(this@StorePictureActivity).load(uri).into(binding.imageView4)
                    binding.cardview4.visibility = View.VISIBLE
                }

                5 -> {
                    var uri = Uri.parse(item.media_name)
                    Glide.with(this@StorePictureActivity).load(uri).into(binding.imageView5)
                    binding.cardview5.visibility = View.VISIBLE
                }

                6 -> {
                    var uri = Uri.parse(item.media_name)
                    Glide.with(this@StorePictureActivity).load(uri).into(binding.imageView6)
                    binding.cardview6.visibility = View.VISIBLE
                }

                7 -> {
                    var uri = Uri.parse(item.media_name)
                    Glide.with(this@StorePictureActivity).load(uri).into(binding.imageView7)
                    binding.cardview7.visibility = View.VISIBLE
                }

                8 -> {
                    var uri = Uri.parse(item.media_name)
                    Glide.with(this@StorePictureActivity).load(uri).into(binding.imageView8)
                    binding.cardview8.visibility = View.VISIBLE
                }

                9 -> {
                    var uri = Uri.parse(item.media_name)
                    Glide.with(this@StorePictureActivity).load(uri).into(binding.imageView9)
                    binding.cardview9.visibility = View.VISIBLE
                }

                10 -> {
                    var uri = Uri.parse(item.media_name)
                    Glide.with(this@StorePictureActivity).load(uri).into(binding.imageView10)
                    binding.cardview10.visibility = View.VISIBLE
                }

                11 -> {
                    var uri = Uri.parse(item.media_name)
                    Glide.with(this@StorePictureActivity).load(uri).into(binding.imageView11)
                    binding.cardview11.visibility = View.VISIBLE
                }

                12 -> {
                    var uri = Uri.parse(item.media_name)
                    Glide.with(this@StorePictureActivity).load(uri).into(binding.imageView12)
                    binding.cardview12.visibility = View.VISIBLE
                }

                13 -> {
                    var uri = Uri.parse(item.media_name)
                    Glide.with(this@StorePictureActivity).load(uri).into(binding.imageView13)
                    binding.cardview13.visibility = View.VISIBLE
                }

                14 -> {
                    var uri = Uri.parse(item.media_name)
                    Glide.with(this@StorePictureActivity).load(uri).into(binding.imageView14)
                    binding.cardview14.visibility = View.VISIBLE
                }

                15 -> {
                    var uri = Uri.parse(item.media_name)
                    Glide.with(this@StorePictureActivity).load(uri).into(binding.imageView15)
                    binding.cardview15.visibility = View.VISIBLE
                }

                16 -> {
                    var uri = Uri.parse(item.media_name)
                    Glide.with(this@StorePictureActivity).load(uri).into(binding.imageView16)
                    binding.cardview16.visibility = View.VISIBLE
                }
            }
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