package com.hk.UI

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.hk.socialmediaapp.Feed.Adapters.FeedPostAdapter
import com.hk.socialmediaapp.QwikerApplication
import com.hk.socialmediaapp.R
import com.hk.socialmediaapp.api.ApiClient
import com.hk.socialmediaapp.api.SessionManager
import com.hk.socialmediaapp.data.PostItem
import com.hk.socialmediaapp.databinding.FragmentUserPostBinding
import com.hk.socialmediaapp.likeresponse.LikeResponse
import com.hk.socialmediaapp.profile.GetPostResponse
import com.hk.socialmediaapp.profile.GetPostResponseItem
import com.hk.socialmediaapp.viewmodel.InventoryViewModelFactory
import com.hk.socialmediaapp.viewmodel.PostViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class UserPostFragment : Fragment() {


    private lateinit var feedAdapter: FeedPostAdapter
    private lateinit var binding: FragmentUserPostBinding
    private lateinit var sessionManager: SessionManager
    private lateinit var apiClient: ApiClient
//    private lateinit var postList: MutableList<GetPostResponseItem>
    private lateinit var listPosts: ArrayList<GetPostResponseItem>

    private val viewModel: PostViewModel by activityViewModels(){
        InventoryViewModelFactory(
            (activity?.application as QwikerApplication).database
                .postItemDao()
        )
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentUserPostBinding.inflate(layoutInflater)
       return  binding.root
//        return inflater.inflate(R.layout.fragment_user_post, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        apiClient = ApiClient()
        sessionManager = SessionManager(requireContext())
//        postList = mutableListOf()
        listPosts = arrayListOf()
        getUserPost(requireContext())
    }

    fun getUserPost(context: Context){
        try {
            apiClient.getretrofitService(requireContext())
                .getMyPosts()
                .enqueue(object :Callback<GetPostResponse>{
                    override fun onResponse(
                        call: Call<GetPostResponse>,
                        response: Response<GetPostResponse>
                    ) {
                       val getPostResponse:GetPostResponse? = response.body()
                        if (response.isSuccessful){
                            if (getPostResponse!= null){
                                listPosts=getPostResponse
                                setUpRecyclerView(listPosts as GetPostResponse,context)
                            }
                            Toast.makeText(context, "retrived successfully", Toast.LENGTH_SHORT).show()
                        }
                        else{
                            Toast.makeText(context, "Error5", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                    override fun onFailure(call: Call<GetPostResponse>, t: Throwable) {
                        Toast.makeText(context, "error in retreiving", Toast.LENGTH_SHORT)
                            .show()
                    }
                })


        }catch(e : Exception){
            Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
        }
    }

    private fun setUpRecyclerView(postList: GetPostResponse,context: Context) {
        feedAdapter = FeedPostAdapter(postList as ArrayList<GetPostResponseItem> ,context,"Profile",null){ getPostResponseItem,position->
            // to delete post from database and bookmarked posts...
            showConfirmationDialog(context,getPostResponseItem,position)
            Toast.makeText(context, "recycler", Toast.LENGTH_SHORT).show()
        }

//        feedAdapter = FeedPostAdapter(listPosts,requireContext(),"Feed",)
        binding.userPostRecView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = feedAdapter
            setHasFixedSize(false)
        }
    }

    private fun deleteFromAdapter(position: Int){
        feedAdapter.remove(position)
    }

    private fun showConfirmationDialog(context: Context,post: GetPostResponseItem,position: Int) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(android.R.string.dialog_alert_title))
            .setMessage(getString(R.string.delete_question))
            .setCancelable(false)
            .setNegativeButton(getString(R.string.no)) { _, _ -> }
            .setPositiveButton(getString(R.string.yes)) { _, _ ->
                deletePost(context,post._id)
                deleteItem(post)
                deleteFromAdapter(position)
            }.show()
    }
//
    private fun deleteItem(post: GetPostResponseItem) {
        //here we should make postId as primary key and for that we have to modify database ans version schema
        val postItem = PostItem(postId = post._id, desc = post.body,
            imgUrl = post.photo, timeStamp = post.postedBy!!.date,
            postType = post.postType, userName = post.postedBy.username, authToken = "will look later", likeNo = post.likes!!.size.toString())
        viewModel.deletePostItem(postItem)
    }

    private fun deletePost(context: Context,postId: String){
        Log.d("postId",postId)
        try {
            apiClient.getretrofitService(context)
                .deletePost(postId)
                .enqueue(object :Callback<LikeResponse>{
                    override fun onResponse(
                        call: Call<LikeResponse>,
                        response: Response<LikeResponse>
                    ) {
                        val deleteResponse:LikeResponse? = response.body()
                        if (response.isSuccessful){
                            if (deleteResponse!=null){
                                Toast.makeText(context,"Post Deleted",Toast.LENGTH_SHORT).show()
                            }
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