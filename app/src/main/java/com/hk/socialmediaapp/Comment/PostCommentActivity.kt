package com.hk.socialmediaapp.Comment

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.hk.socialmediaapp.R
import com.hk.socialmediaapp.api.ApiClient
import com.hk.socialmediaapp.api.SessionManager
import com.hk.socialmediaapp.databinding.ActivityPostCommentBinding
import com.hk.socialmediaapp.likeresponse.LikeResponse
import com.hk.socialmediaapp.post.Adapter.CommentAdapter
import com.hk.socialmediaapp.profile.CommentItem
import com.hk.socialmediaapp.profile.UserResponse
import com.hk.socialmediaapp.utils.CommentList
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.Exception

class PostCommentActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPostCommentBinding
    private lateinit var adapter: CommentAdapter

    private lateinit var sessionManager: SessionManager
    private lateinit var apiClient: ApiClient
    private lateinit var commentList: MutableList<CommentItem>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPostCommentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val name = intent.getStringExtra("UserName")
        val imgUrl = intent.getStringExtra("UserProfileImage")
        val caption = intent.getStringExtra("Caption")
        val postId = intent.getStringExtra("postId")

        apiClient = ApiClient()
        sessionManager = SessionManager(this)

        binding.userName.text = name.toString()

        Glide.with(this).load(imgUrl).error(R.drawable.person_user)
            .into(binding.userpostImage)

        binding.caption.text = caption

        commentList=CommentList.commentList!!

        adapter = CommentAdapter(this, commentList)
        binding.commentRV.layoutManager = LinearLayoutManager(this)
        binding.commentRV.adapter = adapter

        binding.btn.setOnClickListener {
            val comment = binding.etComment.text.toString()
            postComment(comment,this,postId.toString())
            val commentItem: CommentItem = CommentItem("1",comment,postId!!,comment,sessionManager.fetchUserId().toString())
            commentList.add(commentItem)
            adapter = CommentAdapter(this,commentList)
            binding.commentRV.layoutManager = LinearLayoutManager(this)
            binding.commentRV.adapter = adapter
        }

    }

    fun postComment(comment: String, context: Context, postId: String) {
        try {
            apiClient.getretrofitService(context)
                .postComment(comment, comment, postId)
                .enqueue(object : Callback<LikeResponse>{
                    override fun onResponse(call: Call<LikeResponse>, response: Response<LikeResponse>) {
                        val postResponse = response.body()
                        if(response.isSuccessful){
                            Toast.makeText(context,"Successfully posted",Toast.LENGTH_SHORT).show()
                        }else{
                            Toast.makeText(context,"Unable to post",Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<LikeResponse>, t: Throwable) {
                       Toast.makeText(context,"Network Error",Toast.LENGTH_SHORT).show()
                    }

                })

        }catch (e: Exception){
            Toast.makeText(context,e.message,Toast.LENGTH_SHORT).show()
        }
    }
}