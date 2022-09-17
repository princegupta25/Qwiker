package com.hk.UI


import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.hk.socialmediaapp.R
import com.hk.socialmediaapp.api.ApiClient
import com.hk.socialmediaapp.api.SessionManager
import com.hk.socialmediaapp.databinding.DialogSettingsBinding
import com.hk.socialmediaapp.databinding.FragmentProfileBinding
import com.hk.socialmediaapp.loginandsignup.LogInActivity
import com.hk.socialmediaapp.loginandsignup.RetrofitInterface
import com.hk.socialmediaapp.profile.UserResponse
import com.hk.socialmediaapp.utils.FileSearch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import java.io.File
import java.lang.Exception


class ProfileFragment : Fragment() {

    lateinit var  binding :FragmentProfileBinding

    private var retrofit: Retrofit? = null
    private var retrofitInterface: RetrofitInterface? = null
    private lateinit var sessionManager: SessionManager
    private lateinit var apiClient: ApiClient
    private lateinit var userId: String
    private var mSelectedImage = ""
    private lateinit var uri: Uri

    private lateinit var sheetBinding: DialogSettingsBinding
    lateinit var bottomSheetDialog: BottomSheetDialog

    val url ="https://cdn.pixabay.com/photo/2018/04/26/16/31/marine-3352341_960_720.jpg"

    private lateinit var img_url: String
    private lateinit var userName: String
    private lateinit var userBio: String







    private val GALLERY_REQUEST_CODE=6
    private val CAMERA_REQUEST_CODE=6

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(layoutInflater)
        return  binding.root


        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_profile, container, false)
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        apiClient = ApiClient()
        sessionManager = SessionManager(requireContext())
        binding.userName.text = sessionManager.fetchUserName()

        sheetBinding = DialogSettingsBinding.bind(
            LayoutInflater.from(context).inflate(R.layout.dialog_settings,null)
        )

        val options: RequestOptions = RequestOptions()
            .centerCrop()
            .placeholder(R.mipmap.ic_launcher_round)
            .error(R.mipmap.ic_launcher_round)

        Glide.with(this).load(url).apply(options).into(binding.profileImage);


        bottomSheetDialog = BottomSheetDialog(
            requireContext(),R.style.BottomSheetDialogTheme
        )

        binding.settings.setOnClickListener {
          showBottomSheet()
        }

//        binding.profileImage.setOnClickListener {
//            openGalleryForImage()
//        }
//        updateBio()

        getUserDetails()

    }
//    override fun OnBackPressedCallback(){
//        startActivity(Intent(context,FeedFragment::class.java))
////        activity?.finish()
//    }



    private fun showBottomSheet() {
        val bottomSheetView = LayoutInflater.from(context).inflate(
            R.layout.dialog_settings,
            bottomSheetDialog.findViewById<ConstraintLayout>(R.id.bottomSheetProfile)
        )
        bottomSheetDialog.setContentView(bottomSheetView)
        bottomSheetDialog.show()

        bottomSheetDialog.findViewById<TextView>(R.id.tvEditProfile)!!.setOnClickListener {
            //edit name
            Toast.makeText(context, "edit profile", Toast.LENGTH_SHORT).show()
            val intent = Intent(context, EditProfile::class.java)
                .putExtra("USER_NAME",sessionManager.fetchUserName().toString())
                .putExtra("IMG_URL","https://cdn.pixabay.com/photo/2018/04/26/16/31/marine-3352341_960_720.jpg")
                .putExtra("BIO","hi i am harshit")
            startActivity(intent)
        }
        bottomSheetDialog.findViewById<TextView>(R.id.tvEditPassword)!!.setOnClickListener {
            //edit password
        }

        bottomSheetDialog.findViewById<TextView>(R.id.SignOut)!!.setOnClickListener {
            //edit signout
            Toast.makeText(context, sessionManager.fetchAuthToken(),Toast.LENGTH_SHORT).show()
            sessionManager.removeAuthToken()
            Toast.makeText(context, sessionManager.fetchUserName(), Toast.LENGTH_SHORT).show()
            sessionManager.removeUsername()
            startActivity(Intent(context,LogInActivity::class.java))
        }


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK && requestCode == GALLERY_REQUEST_CODE) {
            with(binding) {
                profileImage.setImageURI(data?.data)

            } // handle chosen image
//            selected_image_uri = data?.data
            mSelectedImage = FileSearch.getRealPathFromURI(requireContext(), data?.data!!)

            uri = Uri.fromFile(File(mSelectedImage))
            Toast.makeText(context,uri.toString(),Toast.LENGTH_SHORT).show()

        } else if (resultCode == Activity.RESULT_OK && requestCode == CAMERA_REQUEST_CODE) {

            with(binding) {

                profileImage.setImageBitmap(data?.extras?.get("data") as Bitmap)
            }

//            selected_image_uri = data?.data
//            mSelectedImage = FileSearch.getRealPathFromURI(this, data?.data!!)

            uri = Uri.fromFile(File(mSelectedImage))
            Toast.makeText(context,uri.toString(),Toast.LENGTH_SHORT).show()
        }
    }

//    private fun openGalleryForImage() {
//        val intent = Intent(Intent.ACTION_PICK)
//        intent.type = "image/+"
//        startActivityForResult(intent,GALLERY_REQUEST_CODE)
////        updateProfilePic()
//    }
fun getUserDetails(){

    val token = sessionManager.fetchAuthToken().toString()
    try {
        apiClient.getretrofitService(requireContext())
            .getUser()
            .enqueue(object : Callback<UserResponse> {
                override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                    Toast.makeText(context, "error1", Toast.LENGTH_SHORT)
                        .show()
                }

                override fun onResponse(
                    call: Call<UserResponse>,
                    response: Response<UserResponse>
                ) {
                    val userResponse: UserResponse? = response.body()
                    Log.d("prince",userResponse.toString())
                    if (response.isSuccessful) {
                        if (userResponse != null) {

                            binding.userName.text = userResponse.username.toString()
                            binding.userDesc.text = userResponse.about.toString()
                            val options: RequestOptions = RequestOptions()
                                .centerCrop()
                                .placeholder(R.mipmap.ic_launcher_round)
                                .error(R.mipmap.ic_launcher_round)
                            Glide.with(requireContext()).load(userResponse.imageUrl).apply(options).into(binding.profileImage)


                            Toast.makeText(context,userResponse.username,Toast.LENGTH_SHORT).show()
                            Toast.makeText(context,userResponse.about,Toast.LENGTH_SHORT).show()
                            Toast.makeText(context, "done", Toast.LENGTH_SHORT).show()
                        }
                        Toast.makeText(context, "Updated successfully", Toast.LENGTH_SHORT).show()
//                                startActivity(Intent(this@EditProfile, ProfileFragment::class.java))
                    } else {
                        Toast.makeText(context, "Error4", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            })
    } catch (e: Exception) {
        Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
    }
}

//    private fun updateProfilePic() {
//    }

//    private fun updateBio() {
//        val desc = binding.userDesc.text.toString()
//    }


//    private fun replaceFragment(fragment: Fragment) {
//        val transaction = requireActivity().supportFragmentManager.beginTransaction()
//        transaction.replace(R.id.ProfileFragment, fragment)
//        transaction.commit()
//    }

//    private fun updateUserDetails() {
//        val img_url=""
//        val about = binding.userDesc.text.toString()
//    }
//
//    fun getUserdetails(){
//        binding.userName.text = sessionManager.fetchUserName()
//        val authToken= sessionManager.fetchAuthToken()
//    }




















//    Save And Retrieve Class Object From Shared Preferences Using Gson Library in Kotlin

//
//    fun userDataSave(v:View){
//        var userdata =UserResponse("about","image","username")
//        userdata.username = binding.userName.text.toString()
//        userdata.about = binding.userDesc.text.toString()
//
//        val appSharedPrefs = PreferenceManager
//            .getDefaultSharedPreferences(this.context)
//        val prefsEditor = appSharedPrefs.edit()
//        val gson = Gson()
//        val json = gson.toJson(userdata)
//        prefsEditor.putString("MyObject", json)
//        prefsEditor.commit()
//        Toast.makeText(context, "Object stored in SharedPreferences", Toast.LENGTH_LONG)
//
//    }
//
//    fun userDataGet(v:View){
//        val appSharedPrefs = PreferenceManager
//            .getDefaultSharedPreferences(this.context)
//        val gson = Gson()
//        val json = appSharedPrefs.getString("UserResponse", "")
//        val userdata = gson.fromJson(json, UserResponse::class.java)
////        outputEditText.setText("obj.MyFirstName: [" + obj.myFirstName + "] obj.MyLastName: [" + obj.myLastName + "] obj.myAge: [" + obj.myAge + "] obj.MyValue: [" + obj.myValue + "]")
//
//        Toast.makeText(context, "Object fetch from SharedPreferences successfully", Toast.LENGTH_LONG)
//    }
//
//

}