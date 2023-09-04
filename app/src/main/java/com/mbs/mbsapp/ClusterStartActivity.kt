package com.mbs.mbsapp

import android.Manifest
import android.app.Activity
import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.FileProvider
import com.airbnb.lottie.utils.Utils
import com.inksy.Database.MBSDatabase
import com.mbs.mbsapp.Database.Entities.ActivityLog
import com.mbs.mbsapp.Database.Entities.MediaEntity
import com.mbs.mbsapp.Utils.Constants
import com.mbs.mbsapp.Utils.Permissions
import com.mbs.mbsapp.Utils.TinyDB
import com.mbs.mbsapp.databinding.ActivityClusterStartBinding
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import java.net.URI
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
    var latitude: Double = 0.0
    var longitude: Double = 0.0
    var activitydetailID: String = ""
    var activityCount = 0
    lateinit var URI_Selfie: Uri
    lateinit var URI_TEAM: Uri
    lateinit var URI_LOCATION: Uri
    lateinit var mbsDatabase: MBSDatabase
    var mediacount: Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityClusterStartBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mbsDatabase = MBSDatabase.getInstance(this)!!
        tinyDB = TinyDB(this)
        cameraUri = createImageUri()!!
        getlocation()
        binding.startActivity.setOnClickListener {
            if (selfiecount == 1 && teamcount == 1 && locationcount == 1) {

                for (i in 1..3) {
                    when (i) {
                        1 -> {
                            insertIntoDB(URI_Selfie, mediacount)
                            mediacount++
                        }

                        2 -> {
                            insertIntoDB(URI_TEAM, mediacount)
                            mediacount++
                        }

                        3 -> {
                            insertIntoDB(URI_LOCATION, mediacount)
                            mediacount++

                            mbsDatabase.getMBSData().updateStartActivity(1, activityCount)
                        }
                    }
                }

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
        getlocation()
        var campaignid = tinyDB.getInt("campaignId")
        var brandId = tinyDB.getInt("brandId")
        var locationId = tinyDB.getInt("locationid")
        var cityId = tinyDB.getInt("cityId")
        var storeId = tinyDB.getInt("storeId")
        var time = tinyDB.getString("time")
        var user = mbsDatabase.getMBSData().getUser()

        if (storeId > 0) {
            var getStore = mbsDatabase.getMBSData().getStoresByID(storeId)
            tinyDB.putString("storeName", getStore.storeName)
            activitydetailID = "B$brandId-C$campaignid-ci$cityId-l$locationId-s$storeId"
        } else {

            activitydetailID = "B$brandId-C$campaignid-ci$cityId-l$locationId"
        }

        var getmasterid = mbsDatabase.getMBSData().getMasterId(activitydetailID)
        tinyDB.putInt("activitymasterid", getmasterid[0].activityMasterId!!)
        tinyDB.putInt("activitydetailid", getmasterid[0].id!!)

        val currentTime: String = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date())
        val currentDate: String = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

        activityCount = mbsDatabase.getMBSData().getactivitylogs().size

        tinyDB.putInt("activityLogID", activityCount)
        GlobalScope.launch {
            var activitylog = ActivityLog(
                activityCount,
                activityCount,
                getmasterid[0].activityMasterId,
                campaignid,
                brandId,
                user.id!!,
                0,
                activitydetailID,
                currentDate,
                currentTime,
                latitude.toString(),
                longitude.toString(),
                1,
                0,
                0,
                0,
                0,
                0,
                0,
                "",
                "",
                "",
                ""
            )
            var activitymaster = mbsDatabase.getMBSData().insertActivityLogs(activitylog)


        }

        mediacount = mbsDatabase.getMBSData().getmedia().size

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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            val displayName = "image_${System.currentTimeMillis()}"
            if (requestCode == Selfie) {
                val imageBitmap = data?.extras?.get("data") as Bitmap?

                binding.imageView5.setImageBitmap(imageBitmap)
                URI_Selfie = saveBitmapToGallery(this, imageBitmap!!, displayName)!!
                selfiecount = 1


            }
            if (requestCode == Team) {
                val imageBitmap = data?.extras?.get("data") as Bitmap?

                binding.imageView6.setImageBitmap(imageBitmap)
                URI_TEAM = saveBitmapToGallery(this, imageBitmap!!, displayName)!!
                teamcount = 1

            }

            if (requestCode == Location) {
                val imageBitmap = data?.extras?.get("data") as Bitmap?

                binding.imageView8.setImageBitmap(imageBitmap)
                URI_LOCATION = saveBitmapToGallery(this, imageBitmap!!, displayName)!!
                locationcount = 1

            }
        }

    }

    private fun dispatchTakePictureIntent(request: Int) {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.putExtra("android.intent.extras.CAMERA_FACING", 1);
        startActivityForResult(takePictureIntent, request)

    }

    fun getlocation() {
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

        // Check if the location provider is enabled
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            // Get the last known location
            val lastKnownLocation: Location?
            if (ActivityCompat.checkSelfPermission(
                    this, Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    this, Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return
            }
            lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)

            // Check if the location is available
            if (lastKnownLocation != null) {

                latitude = lastKnownLocation.latitude

                longitude = lastKnownLocation.longitude
                // Now you have the latitude and longitude
            } else {
                // Location data not available
            }
        }
    }

    fun insertIntoDB(uri: Uri, mediacount: Int) {
        GlobalScope.launch {
            var mediaEntity = MediaEntity(
                mediacount,
                Constants.activity_start_num,
                Constants.activity_start_name,
                activityCount,
                0,
                "",
                "picture",
                "",
                uri.toString(),
                "",
                "",
                "",
                "",
            )
            var data = mbsDatabase.getMBSData().insertMedia(mediaEntity)
        }
    }
}