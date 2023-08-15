package com.mbs.mbsapp

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import com.inksy.Database.MBSDatabase
import com.inksy.Remote.APIClient
import com.inksy.Remote.APIInterface
import com.mbs.mbsapp.Utils.Permissions
import com.mbs.mbsapp.Utils.TinyDB
import com.mbs.mbsapp.databinding.ActivityEndBinding
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream

class EndActivity : AppCompatActivity() {

    lateinit var binding: ActivityEndBinding
    lateinit var mbsDatabase: MBSDatabase
    lateinit var tinyDB: TinyDB
    private val Selfie = 10
    private val Team = 20
    private val Location = 30

    private lateinit var cameraUri: Uri
    var selfiecount = 0
    var teamcount = 0
    var locationcount = 0
    var apiInterface: APIInterface = APIClient.createService(APIInterface::class.java)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEndBinding.inflate(layoutInflater)
        setContentView(binding.root)

        tinyDB = TinyDB(this@EndActivity)
        mbsDatabase = MBSDatabase.getInstance(this@EndActivity)!!
        var token = tinyDB.getString("token")
        binding.back.setOnClickListener {
            this@EndActivity.finish()
        }

        binding.back.setOnClickListener {
            selfiecount = 0
            teamcount = 0
            locationcount = 0
            val intent = Intent(this, Dashboard::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.right2, R.anim.right);
            this@EndActivity.finish()
        }

        binding.logout.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.left, R.anim.left2);
            this.finish()
        }

        binding.EndActivity.setOnClickListener {

            if (selfiecount == 1 && teamcount == 1 && locationcount == 1)
                getLogout(token)
            else {
                Toast.makeText(
                    this@EndActivity,
                    "Please Select All Picture",
                    Toast.LENGTH_SHORT
                ).show()
            }

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

            if (!Permissions.Check_CAMERA(this@EndActivity) || !Permissions.Check_STORAGE(
                    this@EndActivity
                )
            ) {
                Permissions.Request_CAMERA_STORAGE(this@EndActivity, 11)
            } else {
                selfiecount = 0
                dispatchTakePictureIntent(Selfie)
            }


        }

        binding.cardview2.setOnClickListener {

            if (!Permissions.Check_CAMERA(this@EndActivity) || !Permissions.Check_STORAGE(
                    this@EndActivity
                )
            ) {
                Permissions.Request_CAMERA_STORAGE(this@EndActivity, 11)
            } else {
                teamcount = 0
                dispatchTakePictureIntent(Team)
            }

        }

        binding.cardview3.setOnClickListener {

            if (!Permissions.Check_CAMERA(this@EndActivity) || !Permissions.Check_STORAGE(
                    this@EndActivity
                )
            ) {
                Permissions.Request_CAMERA_STORAGE(this@EndActivity, 11)
            } else {
                locationcount = 0
                dispatchTakePictureIntent(Location)
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

    fun getLogout(token: String?) {
        var finaltoken = "Bearer $token"
        apiInterface.LogOut(finaltoken)
            .enqueue(object : Callback<APIInterface.ApiResponse<String>> {
                override fun onResponse(
                    call: Call<APIInterface.ApiResponse<String>>,
                    response: Response<APIInterface.ApiResponse<String>>
                ) {
                    if (response.isSuccessful) {
                        tinyDB.clear()
                        Toast.makeText(this@EndActivity, "Logout Successfully", Toast.LENGTH_SHORT)
                            .show()
                        val intent = Intent(this@EndActivity, SelectActivity::class.java)
                        startActivity(intent)
                        overridePendingTransition(R.anim.left, R.anim.left2);
                        this@EndActivity.finish()
                    }
                }

                override fun onFailure(call: Call<APIInterface.ApiResponse<String>>, t: Throwable) {
                    Toast.makeText(this@EndActivity, "Error Occured", Toast.LENGTH_SHORT)
                        .show()
                }

            })

    }
}