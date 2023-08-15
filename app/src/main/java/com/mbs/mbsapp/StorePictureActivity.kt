package com.mbs.mbsapp

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import com.mbs.mbsapp.databinding.ActivityStockGiftCountBinding
import com.mbs.mbsapp.databinding.ActivityStorePictureBinding
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream

class StorePictureActivity : AppCompatActivity() {
    lateinit var binding: ActivityStorePictureBinding
    var count = 1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStorePictureBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.back.setOnClickListener {
            this@StorePictureActivity.finish()
        }

        binding.logout.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            overridePendingTransition(R.anim.left, R.anim.left2);
            this.finish()
        }

        binding.close.setOnClickListener {
            this@StorePictureActivity.finish()
        }

        binding.submit.setOnClickListener {


                Toast.makeText(this@StorePictureActivity, "Pictures Submitted", Toast.LENGTH_SHORT).show()

                val intent = Intent(this, Dashboard::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                overridePendingTransition(R.anim.left, R.anim.left2);
                this.finish()

        }
        binding.Upload.setOnClickListener {

            if (count > 16){
                Toast.makeText(this@StorePictureActivity, "Can't Upload More Photos", Toast.LENGTH_SHORT).show()
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
                11-> dispatchTakePictureIntent(11)
                12-> dispatchTakePictureIntent(12)
                13 -> dispatchTakePictureIntent(13)
                14 -> dispatchTakePictureIntent(14)
                15 -> dispatchTakePictureIntent(15)
                16 -> dispatchTakePictureIntent(16)
            }
        }
    }

    private fun dispatchTakePictureIntent(request: Int) {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.putExtra("android.intent.extras.CAMERA_FACING", 1);
        startActivityForResult(takePictureIntent, request)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 1) {
                val imageBitmap = data?.extras?.get("data") as Bitmap?

                binding.cardview1.visibility = View.VISIBLE
                binding.imageView1.setImageBitmap(imageBitmap)
                saveImageToFolder(imageBitmap!!)
                count++


            }
            if (requestCode == 2) {
                val imageBitmap = data?.extras?.get("data") as Bitmap?

                binding.cardview2.visibility = View.VISIBLE
                binding.imageView2.setImageBitmap(imageBitmap)
                saveImageToFolder(imageBitmap!!)
                count++
            }

            if (requestCode == 3) {
                val imageBitmap = data?.extras?.get("data") as Bitmap?

                binding.cardview3.visibility = View.VISIBLE
                binding.imageView3.setImageBitmap(imageBitmap)
                saveImageToFolder(imageBitmap!!)
                count++
            }

            if (requestCode == 4) {
                val imageBitmap = data?.extras?.get("data") as Bitmap?

                binding.cardview4.visibility = View.VISIBLE
                binding.imageView4.setImageBitmap(imageBitmap)
                saveImageToFolder(imageBitmap!!)
                count++
            }
            if (requestCode == 5) {
                val imageBitmap = data?.extras?.get("data") as Bitmap?

                binding.cardview5.visibility = View.VISIBLE
                binding.imageView5.setImageBitmap(imageBitmap)
                saveImageToFolder(imageBitmap!!)
                count++
            }
            if (requestCode == 6) {
                val imageBitmap = data?.extras?.get("data") as Bitmap?

                binding.cardview6.visibility = View.VISIBLE
                binding.imageView6.setImageBitmap(imageBitmap)
                saveImageToFolder(imageBitmap!!)
                count++
            }
            if (requestCode == 7) {
                val imageBitmap = data?.extras?.get("data") as Bitmap?

                binding.cardview7.visibility = View.VISIBLE
                binding.imageView7.setImageBitmap(imageBitmap)
                saveImageToFolder(imageBitmap!!)
                count++
            }
            if (requestCode == 8) {
                val imageBitmap = data?.extras?.get("data") as Bitmap?

                binding.cardview8.visibility = View.VISIBLE
                binding.imageView8.setImageBitmap(imageBitmap)
                saveImageToFolder(imageBitmap!!)
                count++
            }
            if (requestCode == 9) {
                val imageBitmap = data?.extras?.get("data") as Bitmap?

                binding.cardview9.visibility = View.VISIBLE
                binding.imageView9.setImageBitmap(imageBitmap)
                saveImageToFolder(imageBitmap!!)
                count++
            }
            if (requestCode == 10) {
                val imageBitmap = data?.extras?.get("data") as Bitmap?

                binding.cardview10.visibility = View.VISIBLE
                binding.imageView10.setImageBitmap(imageBitmap)
                saveImageToFolder(imageBitmap!!)
                count++
            }
            if (requestCode == 11) {
                val imageBitmap = data?.extras?.get("data") as Bitmap?

                binding.cardview11.visibility = View.VISIBLE
                binding.imageView11.setImageBitmap(imageBitmap)
                saveImageToFolder(imageBitmap!!)
                count++
            }
            if (requestCode == 12) {
                val imageBitmap = data?.extras?.get("data") as Bitmap?

                binding.cardview12.visibility = View.VISIBLE
                binding.imageView12.setImageBitmap(imageBitmap)
                saveImageToFolder(imageBitmap!!)
                count++
            }
            if (requestCode == 13) {
                val imageBitmap = data?.extras?.get("data") as Bitmap?

                binding.cardview13.visibility = View.VISIBLE
                binding.imageView13.setImageBitmap(imageBitmap)
                saveImageToFolder(imageBitmap!!)
                count++
            }
            if (requestCode == 14) {
                val imageBitmap = data?.extras?.get("data") as Bitmap?

                binding.cardview14.visibility = View.VISIBLE
                binding.imageView14.setImageBitmap(imageBitmap)
                saveImageToFolder(imageBitmap!!)
                count++
            }
            if (requestCode == 15) {
                val imageBitmap = data?.extras?.get("data") as Bitmap?

                binding.cardview15.visibility = View.VISIBLE
                binding.imageView15.setImageBitmap(imageBitmap)
                saveImageToFolder(imageBitmap!!)
                count++
            }
            if (requestCode == 16) {
                val imageBitmap = data?.extras?.get("data") as Bitmap?

                binding.cardview16.visibility = View.VISIBLE
                binding.imageView16.setImageBitmap(imageBitmap)
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


}