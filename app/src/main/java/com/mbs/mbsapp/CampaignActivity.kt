package com.mbs.mbsapp

import ImageAdapter
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.media.Image
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.view.Window
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.inksy.Database.MBSDatabase
import com.inksy.Remote.APIClient
import com.inksy.Remote.APIInterface
import com.mbs.mbsapp.Model.CampaignMediaModel
import com.mbs.mbsapp.Utils.Constants
import com.mbs.mbsapp.Utils.TinyDB
import com.mbs.mbsapp.databinding.ActivityCampaignBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CampaignActivity : AppCompatActivity() {

    lateinit var binding: ActivityCampaignBinding
    lateinit var mbsDatabase: MBSDatabase
    lateinit var tinydb: TinyDB
    var brandId: Int = 0
    var token = ""
    var apiInterface: APIInterface = APIClient.createService(APIInterface::class.java)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCampaignBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mbsDatabase = MBSDatabase.getInstance(this)!!
        tinydb = TinyDB(this)
        brandId = tinydb.getInt("brandId")
        var stoken = tinydb?.getString("token")!!
        token = "Bearer $stoken"
        var data = mbsDatabase.getMBSData().getCampaignbyBrand(brandId)
        if (!data[1].campaignDescription.isNullOrBlank())
            binding.textview.text = Html.fromHtml(data[1].campaignDescription)

        getCampaignMedia(token, data[1].id!!)

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
            val intent = Intent(this, Dashboard::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            overridePendingTransition(R.anim.left, R.anim.left2);
            this.finish()
        }

    }

    fun getCampaignMedia(_token: String, data: Int) {
        apiInterface.getcampaign_media(_token, data).enqueue(object :
            Callback<APIInterface.ApiResponse<List<CampaignMediaModel>>> {

            override fun onFailure(
                call: Call<APIInterface.ApiResponse<List<CampaignMediaModel>>>,
                t: Throwable
            ) {

            }

            override fun onResponse(
                call: Call<APIInterface.ApiResponse<List<CampaignMediaModel>>>,
                response: Response<APIInterface.ApiResponse<List<CampaignMediaModel>>>
            ) {
                var list = response.body()?.data
                binding.imagerecycler.adapter =
                    ImageAdapter(this@CampaignActivity, list!!) { image ->
                        showImagePopup(this@CampaignActivity, image)
                    }
            }

        })
    }

    private fun showImagePopup(context: Context, image: CampaignMediaModel) {
//        val dialog = Dialog(context)
////        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
////        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
//        dialog.setContentView(R.layout.dialog_image)
//
//        // Get the ImageView from the layout
//        val imageView: ImageView = dialog.findViewById(R.id.imageViewFull)
//
//        var image =
//            Constants.baseURLforPicture + "/storage" + image.activityMediaPath + image.activityMediaName
//        // Load the image using a library like Glide
//        Glide.with(context)
//            .load(image)
//            .placeholder(ColorDrawable(android.graphics.Color.TRANSPARENT)) // Optional placeholder while loading
//            //.error(R.drawable.ic_error) // Optional error placeholder
//            .into(imageView)
//
//        // Show the dialog
//        dialog.show()
    }
}