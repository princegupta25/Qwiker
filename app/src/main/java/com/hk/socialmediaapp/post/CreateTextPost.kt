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
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

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
//            var desc: String = binding.descPostEt.text.toString()
//            var post= Post(sessionManager.fetchUserName().toString()+System.currentTimeMillis(),
//                desc,null,
//                "TextPost",System.currentTimeMillis().toString(),
//                sessionManager.fetchUserName().toString(),
//                sessionManager.fetchAuthToken().toString())
            //upload text post
//            PostList.addItem(post)
//            Toast.makeText(this,"posted",Toast.LENGTH_SHORT).show()
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
        val body= binding.bodyPostEt.toString()
        val title = binding.titleEt.toString()
        val userName = sessionManager.fetchUserName().toString()
        apiClient.getretrofitService(this)
            .createPost(null,title,body,"text",userName)
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