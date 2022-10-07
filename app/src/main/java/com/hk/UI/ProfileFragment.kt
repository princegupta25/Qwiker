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
import androidx.compose.ui.input.key.Key.Companion.I
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat.startActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.coroutineScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.hk.socialmediaapp.Feed.Adapters.FeedPostAdapter
import com.hk.socialmediaapp.QwikerApplication
import com.hk.socialmediaapp.R
import com.hk.socialmediaapp.api.ApiClient
import com.hk.socialmediaapp.api.SessionManager
import com.hk.socialmediaapp.data.PostItem
import com.hk.socialmediaapp.databinding.DialogSettingsBinding
import com.hk.socialmediaapp.databinding.FragmentProfileBinding
import com.hk.socialmediaapp.loginandsignup.LogInActivity
import com.hk.socialmediaapp.loginandsignup.RetrofitInterface
import com.hk.socialmediaapp.profile.GetPostResponseItem
import com.hk.socialmediaapp.profile.Post
import com.hk.socialmediaapp.profile.UserResponse
import com.hk.socialmediaapp.utils.FileSearch
import com.hk.socialmediaapp.viewmodel.InventoryViewModelFactory
import com.hk.socialmediaapp.viewmodel.PostViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import java.io.File
import java.lang.Exception


class ProfileFragment : Fragment() {

//   2
    private val viewModel: PostViewModel by activityViewModels(){
        InventoryViewModelFactory(
            (activity?.application as QwikerApplication).database
                .postItemDao()
        )
    }
    private lateinit var savedItems: List<Post>

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
    private lateinit var postList: MutableList<GetPostResponseItem>






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

        postList = mutableListOf()

        sheetBinding = DialogSettingsBinding.bind(
            LayoutInflater.from(context).inflate(R.layout.dialog_settings,null)
        )

        Log.d("signouut",sessionManager.fetchUserName().toString())
        Log.d("signouut",sessionManager.fetchAuthToken().toString())

        val options: RequestOptions = RequestOptions()
            .centerCrop()
            .placeholder(R.mipmap.ic_launcher_round)
            .error(R.mipmap.ic_launcher_round)

//        if(sessionManager.fetchProfileImgUrl() == null) {
            Glide.with(requireContext()).load(url).apply(options).into(binding.profileImage);
//        }else{
//            Log.d("hii2",sessionManager.fetchProfileImgUrl().toString())
//            Glide.with(requireContext()).load(sessionManager.fetchProfileImgUrl().toString()).apply(options).into(binding.profileImage);
//        }


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

        context?.let { getUserDetails(it) }


        lifecycle.coroutineScope.launch{
            viewModel.getAllItems().collect(){
                for (postItem in it){
//                    val post: Post = Post(postItem.postId,postItem.desc,postItem.imgUrl,
//                                           postItem.postType,postItem.timeStamp,postItem.userName,postItem.authToken)
//                    postList.add(post)
                    val post :GetPostResponseItem = GetPostResponseItem(postItem.postId,postItem.desc,postItem.imgUrl,
                    postItem.postType,null,null,postItem.userName)
                    postList.add(post)
                }
                val adapter = FeedPostAdapter(postList as ArrayList<GetPostResponseItem> ,requireContext(),"Profile"){ post->
                    showConfirmationDialog(post)
                    postList.remove(post)

                }
                binding.savedPostRecView.adapter =adapter
                binding.savedPostRecView.layoutManager = LinearLayoutManager(context)


//                binding.feedBtn.setOnClickListener {
//                    fragmentManager.beginTransaction()
//                        .replace(R.id.flLayout,UserPostFragment())
//                        .commit()
//                }
//
//                binding.savedBtn.setOnClickListener {
//                    fragmentManager.beginTransaction()
//                        .replace(R.id.flLayout,BookmarkFragment())
//                        .commit()
//                }
            }
        }



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
//            Toast.makeText(context, "edit profile", Toast.LENGTH_SHORT).show()
            val intent = Intent(context, EditProfile::class.java)
                .putExtra("USER_NAME",userName)
                .putExtra("IMG_URL",img_url)
                .putExtra("BIO",userBio)
            startActivity(intent)
            bottomSheetDialog.dismiss()
        }
        bottomSheetDialog.findViewById<TextView>(R.id.tvEditPassword)!!.setOnClickListener {
            //edit password
        }

        bottomSheetDialog.findViewById<TextView>(R.id.SignOut)!!.setOnClickListener {
            //edit signout
            sessionManager.removeAuthToken()
            sessionManager.removeUsername()
            Log.d("signouut",sessionManager.fetchUserName().toString())
            Log.d("signouut",sessionManager.fetchAuthToken().toString())
            val intent= Intent(context,LogInActivity::class.java)
            activity?.finishAffinity()
            startActivity(intent)

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
fun getUserDetails(context: Context){

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

//                            binding.userName.text = userResponse.name
                            binding.userName.text = userResponse.username.toString()
                            binding.userDesc.text = userResponse.about.toString()
                            userName = userResponse.username.toString()
                            userBio= userResponse.about.toString()
                            img_url = userResponse.imageUrl.toString()
                            val options: RequestOptions = RequestOptions()
                                .centerCrop()
                                .placeholder(R.mipmap.ic_launcher_round)
                                .error(R.mipmap.ic_launcher_round)
                            Glide.with(context).load(userResponse.imageUrl).apply(options).error(R.drawable.person_user).into(binding.profileImage)


//                            Toast.makeText(context,userResponse.username,Toast.LENGTH_SHORT).show()
//                            Toast.makeText(context,userResponse.about,Toast.LENGTH_SHORT).show()
//                            Toast.makeText(context, "done", Toast.LENGTH_SHORT).show()
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

//   2
   private fun showConfirmationDialog(post: GetPostResponseItem) {
    MaterialAlertDialogBuilder(requireContext())
        .setTitle(getString(android.R.string.dialog_alert_title))
        .setMessage(getString(R.string.delete_question))
        .setCancelable(false)
        .setNegativeButton(getString(R.string.no)) { _, _ -> }
        .setPositiveButton(getString(R.string.yes)) { _, _ ->
            deleteItem(post)
        }.show()
}
    private fun deleteItem(post: GetPostResponseItem) {
        //here we should make postId as primary key and for that we have to modify database ans version schema
        val postItem = PostItem(postId = post._id, desc = post.body,
            imgUrl = post.photo, timeStamp = post.postedBy!!.date,
            postType = post.postType, userName = post.postedBy.username, authToken = "will look later")
            viewModel.deletePostItem(postItem)
    }

}