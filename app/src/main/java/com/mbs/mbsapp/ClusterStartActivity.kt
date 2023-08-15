package com.mbs.mbsapp

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.airbnb.lottie.utils.Utils
import com.mbs.mbsapp.Utils.Permissions
import com.mbs.mbsapp.Utils.TinyDB
import com.mbs.mbsapp.databinding.ActivityClusterStartBinding
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class ClusterStartActivity : AppCompatActivity() {

    lateinit var binding: ActivityClusterStartBinding
    private val Selfie = 10
    private val Team = 20
    private val Location = 30

    private lateinit var cameraUri: Uri
    lateinit var tinyDB: TinyDB
    var selfiecount = 0
    var teamcount = 0
    var locationcount = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityClusterStartBinding.inflate(layoutInflater)
        setContentView(binding.root)

        tinyDB = TinyDB(this)
        cameraUri = createImageUri()!!
        binding.startActivity.setOnClickListener {
            if (selfiecount == 1 && teamcount == 1 && locationcount == 1) {
                Toast.makeText(this, "Activity Started", Toast.LENGTH_SHORT).show()
                val currentTime: String =
                    SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date())
                tinyDB.putString("time", currentTime.toString())
                val intent = Intent(this, Dashboard::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                overridePendingTransition(R.anim.left, R.anim.left2);
                finish()
            } else {
                Toast.makeText(
                    this@ClusterStartActivity,
                    "Please Select All Picture",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        binding.back.setOnClickListener {
            selfiecount = 0
            teamcount = 0
            locationcount = 0
            val intent = Intent(this, SelectActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.right2, R.anim.right);
        }

        binding.logout.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            overridePendingTransition(R.anim.left, R.anim.left2);
            this.finish()
        }

        binding.button.setOnClickListener {
            binding.cardview1.performClick()
        }

        binding.button2.setOnClickListener {
            binding.cardview2.performClick()
        }

        binding.button3.setOnClickListener {
            binding.cardview3.performClick()
        }

        binding.cardview1.setOnClickListener {

            if (!Permissions.Check_CAMERA(this@ClusterStartActivity) || !Permissions.Check_STORAGE(
                    this@ClusterStartActivity
                )
            ) {
                Permissions.Request_CAMERA_STORAGE(this@ClusterStartActivity, 11)
            } else {
                selfiecount = 0
                dispatchTakePictureIntent(Selfie)
            }


        }

        binding.cardview2.setOnClickListener {

            if (!Permissions.Check_CAMERA(this@ClusterStartActivity) || !Permissions.Check_STORAGE(
                    this@ClusterStartActivity
                )
            ) {
                Permissions.Request_CAMERA_STORAGE(this@ClusterStartActivity, 11)
            } else {
                teamcount = 0
                dispatchTakePictureIntent(Team)
            }

        }

        binding.cardview3.setOnClickListener {

            if (!Permissions.Check_CAMERA(this@ClusterStartActivity) || !Permissions.Check_STORAGE(
                    this@ClusterStartActivity
                )
            ) {
                Permissions.Request_CAMERA_STORAGE(this@ClusterStartActivity, 11)
            } else {
                locationcount = 0
                dispatchTakePictureIntent(Location)
            }

        }


    }

    private fun createImageUri(): Uri? {
        val image = File(applicationContext.filesDir, "camera_photo.png")
        return FileProvider.getUriForFile(
            applicationContext,
            "com.mbs.mbsapp.fileprovider",
            image
        )
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == Selfie) {
                val imageBitmap = data?.extras?.get("data") as Bitmap?

                binding.imageView5.setImageBitmap(imageBitmap)
                saveImageToFolder(imageBitmap!!)
                selfiecount = 1


            }
            if (requestCode == Team) {
                val imageBitmap = data?.extras?.get("data") as Bitmap?

                binding.imageView6.setImageBitmap(imageBitmap)
                saveImageToFolder(imageBitmap!!)
                teamcount = 1
            }

            if (requestCode == Location) {
                val imageBitmap = data?.extras?.get("data") as Bitmap?

                binding.imageView8.setImageBitmap(imageBitmap)
                saveImageToFolder(imageBitmap!!)
                locationcount = 1
            }
        }

    }

    private fun dispatchTakePictureIntent(request: Int) {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.putExtra("android.intent.extras.CAMERA_FACING", 1);
        startActivityForResult(takePictureIntent, request)

    }
}