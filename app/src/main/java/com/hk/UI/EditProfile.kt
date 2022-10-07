package com.hk.UI

import android.R
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.hk.socialmediaapp.api.ApiClient
import com.hk.socialmediaapp.api.SessionManager
import com.hk.socialmediaapp.databinding.ActivityEditProfileBinding
import com.hk.socialmediaapp.loginandsignup.RetrofitInterface
import com.hk.socialmediaapp.profile.UserResponse
import com.hk.socialmediaapp.profile.UserUpdateResponse
import com.hk.socialmediaapp.utils.FileSearch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import java.io.File


class EditProfile : AppCompatActivity() {
    private lateinit var binding: ActivityEditProfileBinding
    private lateinit var img_url: String
    private lateinit var userName: String
    private lateinit var userBio: String

    private var mSelectedImage = ""
    private lateinit var uri: Uri

    private val GALLERY_REQUEST_CODE=6
    private val CAMERA_REQUEST_CODE=6

    private var retrofit: Retrofit? = null
    private var retrofitInterface: RetrofitInterface? = null
    private lateinit var apiClient: ApiClient
    private lateinit var sessionManager: SessionManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userName = intent.getStringExtra("USER_NAME").toString()
        img_url = intent.getStringExtra("IMG_URL").toString()
        userBio = intent.getStringExtra("BIO").toString()
        binding.editUserName.hint=userName
        binding.editBio.hint = userBio

        apiClient = ApiClient()
        sessionManager = SessionManager(this)

        if(img_url!=null) {
            val options: RequestOptions = RequestOptions()
                .centerCrop()
                .placeholder(com.hk.socialmediaapp.R.mipmap.ic_launcher_round)
                .error(com.hk.socialmediaapp.R.mipmap.ic_launcher_round)
            Glide.with(this).load(img_url).apply(options).into(binding.profileImage);
        }


        binding.changePhoto.setOnClickListener {
            openGalleryForImage()
        }
        binding.doneBtn.setOnClickListener {
            editProfile()
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK && requestCode == GALLERY_REQUEST_CODE) {
            with(binding) {
                profileImage.setImageURI(data?.data)

            } // handle chosen image
//            selected_image_uri = data?.data
            mSelectedImage = FileSearch.getRealPathFromURI(this, data?.data!!)

            uri = Uri.fromFile(File(mSelectedImage))
//            Toast.makeText(this,uri.toString(), Toast.LENGTH_SHORT).show()

        } else if (resultCode == Activity.RESULT_OK && requestCode == CAMERA_REQUEST_CODE) {

            with(binding) {

                profileImage.setImageBitmap(data?.extras?.get("data") as Bitmap)
            }

//            selected_image_uri = data?.data
//            mSelectedImage = FileSearch.getRealPathFromURI(this, data?.data!!)

            uri = Uri.fromFile(File(mSelectedImage))
//            Toast.makeText(this,uri.toString(), Toast.LENGTH_SHORT).show()


            img_url=uri.toString()
        }
    }

    private fun editProfile() {
        userName = binding.editUserName.text.toString()
        userBio= binding.editBio.text.toString()
        Log.d("editProifile",userName)
        Log.d("editProifile",userBio)
        Log.d("editProifile",img_url)


//
//        sessionManager.saveProfileImgUrl(img_url)
//
        if(userName!= "" && userBio != "" && img_url!=null){
             //username,editbio,img_url to update
            //api call for updating user data
            try {
                apiClient.getretrofitService(this)
                    .updateUser(userName,userBio,img_url)
                    .enqueue(object : Callback<UserUpdateResponse> {
                        override fun onFailure(call: Call<UserUpdateResponse>, t: Throwable) {
                            Toast.makeText(applicationContext, "error1", Toast.LENGTH_SHORT)
                                .show()
                        }

                        override fun onResponse(
                            call: Call<UserUpdateResponse>,
                            response: Response<UserUpdateResponse>
                        ) {
                            val userUpdateResponse: UserUpdateResponse? = response.body()
                            Log.d("editProfile",userUpdateResponse.toString())
                            Log.d("editProfile",response.toString())
                            Log.d("editProfile",response.body().toString())
                            if (response.isSuccessful) {
                                if (userUpdateResponse != null) {
                                    if(userUpdateResponse.success == true){
                                        Toast.makeText(this@EditProfile,userUpdateResponse.message,Toast.LENGTH_SHORT).show()
                                    }else{
                                        Toast.makeText(this@EditProfile,"Couldn't update",Toast.LENGTH_SHORT).show()
                                    }
//                                    Toast.makeText(applicationContext,userResponse.username,Toast.LENGTH_SHORT).show()
//                                    Toast.makeText(applicationContext,userResponse.about,Toast.LENGTH_SHORT).show()
                                }
                                startActivity(Intent(this@EditProfile, ProfileFragment::class.java))
                            } else {
                                Toast.makeText(applicationContext, "Error4", Toast.LENGTH_SHORT)
                                    .show()
                            }
                        }
                    })
            } catch (e: Exception) {
                Toast.makeText(applicationContext, e.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun openGalleryForImage() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/+"
        startActivityForResult(intent,GALLERY_REQUEST_CODE)
    }

}