package com.mbs.mbsapp

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.inksy.Database.MBSDatabase
import com.mbs.mbsapp.Utils.Permissions
import com.mbs.mbsapp.databinding.ActivityClusterStartBinding
import java.io.File

class ClusterStartActivity : AppCompatActivity() {

    lateinit var binding: ActivityClusterStartBinding
    private val PICK_REQUEST = 53
    private lateinit var cameraUri: Uri

    private val contract = registerForActivityResult(ActivityResultContracts.TakePicture()) {
        binding.imageView5.setImageURI(null)
        binding.imageView5.setImageURI(cameraUri)

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


    }

    private fun createImageUri(): Uri? {
        val image = File(applicationContext.filesDir, "camera_photo.png")
        return FileProvider.getUriForFile(
            applicationContext,
            "com.mbs.mbsapp.fileprovider",
            image
        )
    }
}