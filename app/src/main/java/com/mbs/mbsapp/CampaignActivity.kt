package com.mbs.mbsapp

import ImageAdapter
import android.app.AlertDialog
import android.content.Intent
import android.media.Image
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.widget.ImageView
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
        if (!data[0].campaignDescription.isNullOrBlank())
            binding.textview.text = Html.fromHtml(data[0].campaignDescription)

        getCampaignMedia(token, data[0].id!!)

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
                        showImagePopup(image)
                    }
            }

        })
    }

    private fun showImagePopup(image: CampaignMediaModel) {
        val dialogView = layoutInflater.inflate(R.layout.dialog_image, null)
        val imageViewFull: ImageView = dialogView.findViewById(R.id.imageViewFull)
        var data = Constants.baseURL + image.activityMediaPath + image.activityMediaName
        //imageViewFull.setImageResource(data)

        val dialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .setPositiveButton("Close") { _, _ -> }
            .create()

        dialog.show()
    }
}