package com.hk.socialmediaapp.post

import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.view.isInvisible
import com.hk.socialmediaapp.MainActivity
import com.hk.socialmediaapp.api.ApiClient
import com.hk.socialmediaapp.api.SessionManager
import com.hk.socialmediaapp.databinding.ActivityImagePostReviewBinding
import com.hk.socialmediaapp.profile.PostResponse
import com.hk.socialmediaapp.utils.Constants
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class ImagePostReviewActivity : AppCompatActivity() {
    private lateinit var binding: ActivityImagePostReviewBinding
    var path=""
    var outputUri: Uri?=null
    private lateinit var progressDialog: ProgressDialog

    private lateinit var sessionManager: SessionManager
    private lateinit var apiClient: ApiClient


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityImagePostReviewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val path=intent.getStringExtra(Constants.FILE_PATH)
        var imgfile: File? = null



        apiClient = ApiClient()
        sessionManager = SessionManager(this)

        imgfile= File(path)
        val mybitmap: Bitmap = BitmapFactory.decodeFile(imgfile.path);
        binding.galleryImageView.setImageBitmap(mybitmap)
        binding.progressBar.visibility=View.GONE
        outputUri= Uri.fromFile(File(path))

        binding.doneBtn.setOnClickListener{

           uploadImagePost()
//
//            val intent= Intent(this, MainActivity::class.java)
//            finishAffinity()
//            startActivity(intent)
        }


    }


    private fun uploadImagePost(){
        val bodyText= binding.bodyPostEt.text.toString()
        val userName = sessionManager.fetchUserName().toString()
        Log.d("imagePost",bodyText)
        Log.d("imagePost",userName)
        Log.d("imagePost",apiClient.toString())
        Log.d("imagePost",outputUri.toString())


        val file: File = File(path)

        val reqFile = RequestBody.create("image/*".toMediaTypeOrNull(), file)
        val part = MultipartBody.Part.createFormData("photo", file.name, reqFile)

        val title: RequestBody = RequestBody.create(
            "text/plain".toMediaTypeOrNull(),
            "title"
        )

        val body: RequestBody = RequestBody.create(
            "text/plain".toMediaTypeOrNull(),
            bodyText
        )

        val postType: RequestBody = RequestBody.create(
            "text/plain".toMediaTypeOrNull(),
            "image"
        )

        val username: RequestBody = RequestBody.create(
            "text/plain".toMediaTypeOrNull(),
            userName
        )

        apiClient.getretrofitService(this)
            .createPost(part,title,body,postType,username)
            .enqueue(object : Callback<PostResponse>{
                override fun onResponse(
                    call: Call<PostResponse>,
                    response: Response<PostResponse>
                ) {
                    val postResponse: PostResponse? = response.body()
                    Log.d("imagePostCode",response.code().toString())
                    if (response.isSuccessful) {
                        if (postResponse != null) {
                            Log.d("harsh2image",postResponse.toString())
                            Toast.makeText(this@ImagePostReviewActivity,"posted",Toast.LENGTH_SHORT).show()
                            val intent= Intent(this@ImagePostReviewActivity, MainActivity::class.java)
                            finishAffinity()
                            startActivity(intent)
                        }
                    }else{
                        Toast.makeText(this@ImagePostReviewActivity, "Failed to post", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<PostResponse>, t: Throwable) {
                    Toast.makeText(this@ImagePostReviewActivity, "Network Issue", Toast.LENGTH_SHORT).show()
                }

            })
    }


}