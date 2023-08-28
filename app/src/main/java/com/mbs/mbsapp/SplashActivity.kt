package com.mbs.mbsapp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.inksy.Database.MBSDatabase
import com.mbs.mbsapp.Utils.DbHandler
import com.mbs.mbsapp.Utils.DbScript
import com.mbs.mbsapp.Utils.TinyDB
import com.mbs.mbsapp.databinding.ActivitySplashBinding


class SplashActivity : AppCompatActivity() {

    lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Glide.with(this).asGif().load(R.drawable.loader).into(binding.imageView3)

        val intent = Intent(this, MainActivity::class.java)
        val timer: Thread = object : Thread() {
            override fun run() {
                try {
                    try {
                        sleep(2000)
                    } catch (e: InterruptedException) {
                        e.printStackTrace()
                    }
                } finally {

                    var tinyDB = TinyDB(this@SplashActivity)
                    var token = tinyDB.getString("token")
                    var activityTime = tinyDB.getString("time")

                    if (token.isNullOrBlank()) {
                        startActivity(intent)
                        overridePendingTransition(R.anim.left, R.anim.left2);
                        finish()
                    } else {
                        if (activityTime.isNullOrEmpty()) {

                            val intent = Intent(this@SplashActivity, SelectActivity::class.java)
                            startActivity(intent)
                            overridePendingTransition(R.anim.left, R.anim.left2);
                            finish()
                        } else {
                            val intent = Intent(this@SplashActivity, Dashboard::class.java)
                            startActivity(intent)
                            overridePendingTransition(R.anim.left, R.anim.left2);
                            finish()


                        }
                    }
                }
            }
        }
        timer.start()
    }

//    class DBHelper : SQLiteOpenHelper(context, DATABASE_NAME, null, 1) {
//        override fun onCreate(db: SQLiteDatabase) {}
//        override fun onUpgrade(database: SQLiteDatabase, oldVersion: Int, newVersion: Int) {}
//    }
}