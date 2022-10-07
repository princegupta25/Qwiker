package com.hk.socialmediaapp.post

import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.view.isInvisible
import com.hk.socialmediaapp.MainActivity
import com.hk.socialmediaapp.api.ApiClient
import com.hk.socialmediaapp.api.SessionManager
import com.hk.socialmediaapp.databinding.ActivityImagePostReviewBinding
import com.hk.socialmediaapp.profile.PostResponse
import com.hk.socialmediaapp.utils.Constants
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
        outputUri= Uri.fromFile(File(path))
//        editPhoto()
        binding.doneBtn.setOnClickListener{
//            var post= Post(sessionManager.fetchUserName().toString()+System.currentTimeMillis(),
//                null,path,
//                "ImagePost",System.currentTimeMillis().toString(),
//                sessionManager.fetchUserName().toString(),
//                sessionManager.fetchAuthToken().toString())
//            PostList.addItem(post)

           uploadImagePost()

            //function call to post this image post
//            Toast.makeText(this,"posted",Toast.LENGTH_SHORT).show()
            val intent= Intent(this, MainActivity::class.java)
            finishAffinity()
            startActivity(intent)
        }


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(resultCode== RESULT_OK)
        {
            outputUri= data?.data!!
            binding.galleryImageView.setImageURI(outputUri)
            binding.progressBar.isInvisible
        }
    }

    private fun uploadImagePost(){
        val body= binding.bodyPostEt.toString()
        val userName = sessionManager.fetchUserName().toString()
        apiClient.getretrofitService(this)
            .createPost(path,null,body,"image",userName)
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

//    private fun editPhoto()
//    {
//        val dsPhotoEditorIntent = Intent(this, DsPhotoEditorActivity::class.java)
//        dsPhotoEditorIntent.data = outputUri
//
//        dsPhotoEditorIntent.putExtra(DsPhotoEditorConstants.DS_PHOTO_EDITOR_OUTPUT_DIRECTORY, "Vasukam Images")
//
//        val toolsToHide =
//            intArrayOf(DsPhotoEditorActivity.TOOL_ORIENTATION, DsPhotoEditorActivity.TOOL_CROP)
//
//        dsPhotoEditorIntent.putExtra(
//            DsPhotoEditorConstants.DS_PHOTO_EDITOR_TOOLS_TO_HIDE,
//            toolsToHide
//        )
//        startActivityForResult(dsPhotoEditorIntent, 200)
//    }
}