package com.mbs.mbsapp

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.mbs.mbsapp.Utils.Permissions
import com.mbs.mbsapp.databinding.ActivityClusterStartBinding
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream


class ClusterStartActivity : AppCompatActivity() {

    lateinit var binding: ActivityClusterStartBinding
    private val PICK_REQUEST = 53
    private lateinit var cameraUri: Uri

    private val contract = registerForActivityResult(ActivityResultContracts.TakePicture()) {
        binding.imageView5.setImageURI(null)
        binding.imageView5.setImageURI(cameraUri)
        var bitmap = uriToBitmap(cameraUri)
        saveImageToFolder(bitmap!!)

    }

    private val contract1 = registerForActivityResult(ActivityResultContracts.TakePicture()) {
        binding.imageView6.setImageURI(null)
        binding.imageView6.setImageURI(cameraUri)
        var bitmap = uriToBitmap(cameraUri)
        saveImageToFolder(bitmap!!)

    }

    private val contract2 = registerForActivityResult(ActivityResultContracts.TakePicture()) {
        binding.imageView8.setImageURI(null)
        binding.imageView8.setImageURI(cameraUri)
        var bitmap = uriToBitmap(cameraUri)
        saveImageToFolder(bitmap!!)

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityClusterStartBinding.inflate(layoutInflater)
        setContentView(binding.root)

        cameraUri = createImageUri()!!
        binding.startActivity.setOnClickListener {
            val intent = Intent(this, Dashboard::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            overridePendingTransition(R.anim.left, R.anim.left2);
            finish()
        }

        binding.back.setOnClickListener {
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
                contract.launch(cameraUri)
            }
        }

        binding.cardview2.setOnClickListener {

            if (!Permissions.Check_CAMERA(this@ClusterStartActivity) || !Permissions.Check_STORAGE(
                    this@ClusterStartActivity
                )
            ) {
                Permissions.Request_CAMERA_STORAGE(this@ClusterStartActivity, 11)
            } else {
                contract1.launch(cameraUri)
            }
        }

        binding.cardview3.setOnClickListener {

            if (!Permissions.Check_CAMERA(this@ClusterStartActivity) || !Permissions.Check_STORAGE(
                    this@ClusterStartActivity
                )
            ) {
                Permissions.Request_CAMERA_STORAGE(this@ClusterStartActivity, 11)
            } else {
                contract2.launch(cameraUri)
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

    private fun uriToBitmap(uri: Uri): Bitmap? {
        return try {
            val inputStream = contentResolver.openInputStream(uri)
            BitmapFactory.decodeStream(inputStream)
        } catch (e: Exception) {
            e.printStackTrace()
            null
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

//        val fos = FileOutputStream(pictureFile)
//        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos)
//        fos.write();
//        fos.flush();
//        fos.close()



    }
}