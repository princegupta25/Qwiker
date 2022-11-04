package com.hk.socialmediaapp.post

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.hk.socialmediaapp.MainActivity
import com.hk.socialmediaapp.api.ApiClient
import com.hk.socialmediaapp.api.SessionManager
import com.hk.socialmediaapp.databinding.ActivityCreateTextPostBinding
import com.hk.socialmediaapp.loginandsignup.LogInActivity
import com.hk.socialmediaapp.profile.PostResponse
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class CreateTextPost : AppCompatActivity() {
    private lateinit var binding: ActivityCreateTextPostBinding

    private lateinit var sessionManager: SessionManager
    private lateinit var apiClient: ApiClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateTextPostBinding.inflate(layoutInflater)
        setContentView(binding.root)
//        var title: String = binding.titlePostEt.text.toString()

        apiClient = ApiClient()
        sessionManager = SessionManager(this)

        binding.nextBtn.setOnClickListener {
            uploadTextPost()
            val intent= Intent(this, MainActivity::class.java)
            finishAffinity()
            startActivity(intent)

        }

        binding.backBtn.setOnClickListener {
            startActivity(Intent(this,MainActivity::class.java))
        }
    }

    private fun uploadTextPost(){
        val bodyText= binding.bodyPostEt.text.toString()
        val titleText = binding.titleEt.text.toString()
        val userName = sessionManager.fetchUserName().toString()



        val file: File = File("noimage.png")

        val reqFile = RequestBody.create("image/*".toMediaTypeOrNull(), file)
        val part = MultipartBody.Part.createFormData("photo", file.name, reqFile)

        val title: RequestBody = RequestBody.create(
            "text/plain".toMediaTypeOrNull(),
            titleText
        )

        val body: RequestBody = RequestBody.create(
            "text/plain".toMediaTypeOrNull(),
            bodyText
        )

        val postType: RequestBody = RequestBody.create(
            "text/plain".toMediaTypeOrNull(),
            "text"
        )

        val username: RequestBody = RequestBody.create(
            "text/plain".toMediaTypeOrNull(),
            userName
        )



        apiClient.getretrofitService(this)
            .createPost(part,title,body,postType,username)
            .enqueue(object : Callback<PostResponse> {
                override fun onResponse(
                    call: Call<PostResponse>,
                    response: Response<PostResponse>
                ) {
                    val postResponse: PostResponse? = response.body()
                    Log.d("textPostCode",response.code().toString())
                    if (response.isSuccessful) {
                        if (postResponse != null) {
                            Log.d("harsh2text",postResponse.toString())
                            Toast.makeText(this@CreateTextPost,"posted successfully",Toast.LENGTH_SHORT).show()
                            val intent= Intent(this@CreateTextPost, MainActivity::class.java)
                            finishAffinity()
                            startActivity(intent)
                        }
                    }else{
                        Toast.makeText(this@CreateTextPost, "Failed to post", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<PostResponse>, t: Throwable) {
                    Toast.makeText(this@CreateTextPost, "Network Issue", Toast.LENGTH_SHORT).show()
                }
            })
    }
}