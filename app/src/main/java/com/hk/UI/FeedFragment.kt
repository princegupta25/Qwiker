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
import com.hk.socialmediaapp.Feed.Adapters.FeedPostAdapter
import com.hk.socialmediaapp.QwikerApplication
import com.hk.socialmediaapp.api.ApiClient
import com.hk.socialmediaapp.api.SessionManager
import com.hk.socialmediaapp.data.PostItem
import com.hk.socialmediaapp.databinding.FragmentFeedBinding
import com.hk.socialmediaapp.likeresponse.LikeResponse
import com.hk.socialmediaapp.profile.GetPostResponse
import com.hk.socialmediaapp.profile.GetPostResponseItem
import com.hk.socialmediaapp.viewmodel.InventoryViewModelFactory
import com.hk.socialmediaapp.viewmodel.PostViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.properties.Delegates


class FeedFragment : Fragment() {

//  2
    private val viewModel: PostViewModel by activityViewModels(){
        InventoryViewModelFactory(
            (activity?.application as QwikerApplication).database
                .postItemDao()
        )
    }

    private lateinit var feedAdapter: FeedPostAdapter
    private lateinit var binding: FragmentFeedBinding
    private lateinit var apiClient: ApiClient
    private lateinit var sessionManager: SessionManager
    private lateinit var listPosts: ArrayList<GetPostResponseItem>

//    private lateinit var postId:String
//    private var isLiked=false

//    1
//    var totalPosts=18
//    private lateinit var lastVisible: DocumentSnapshot
//    var isLastPage = false
//    var index=0


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentFeedBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        apiClient = ApiClient()
        sessionManager = SessionManager(requireContext())

        Log.d("harsh",apiClient.toString() + sessionManager.toString())
        listPosts = arrayListOf()

        getPostItems(requireContext())


        Log.d("harsh",listPosts.toString())



        //3
//        var list = PostList.getList()
//        list = list.sortedBy { post: Post -> post.time_stamp }

//    1
//        setUpRecyclerView(list.reversed().take(5),requireContext())
//        index=4
//        if(!isLastPage){
//            feedAdapter.submitData(list.subList(index,index+5))
//            if(index>=totalPosts){
//                isLastPage=true
//            }
//        }

        3
//        feedAdapter = FeedPostAdapter(list.reversed(),requireContext(),"Feed"){
//            //2
//            savePostItem(it)
//            Toast.makeText(context, "Post Saved", Toast.LENGTH_SHORT).show()
//        }


//        feedAdapter = FeedPostAdapter(listPosts,requireContext(),"Feed",){
//            Log.d("harsh",feedAdapter.toString())
//            savePostItem(it)
//        }
//
//        binding.postRecView.apply {
//            layoutManager = LinearLayoutManager(context)
//            adapter = feedAdapter
//            setHasFixedSize(false)
//        }
    }

    private fun getPostItems(context: Context){
        Log.d("harsh","start")
        try {
            apiClient.getretrofitService(requireContext())
                .getAllPosts()
                .enqueue(object: Callback<GetPostResponse>{
                    override fun onResponse(
                        call: Call<GetPostResponse>,
                        response: Response<GetPostResponse>
                    ) {
                        Log.d("harsh",response.body().toString())
                        Log.d("harsh",response.code().toString())
                        val getPostResponse : GetPostResponse? = response.body()
                        if (response.isSuccessful){
                            if (getPostResponse!=null){
                                listPosts=getPostResponse
                                Log.d("harsh1",listPosts.toString())
                                Toast.makeText(requireContext(),"retrieved",Toast.LENGTH_SHORT).show()
                                setUpRecyclerView(context,listPosts)
                            }else{
                                Toast.makeText(context,"Error",Toast.LENGTH_SHORT).show()
                            }
                        }
                    }

                    override fun onFailure(call: Call<GetPostResponse>, t: Throwable) {
                        Toast.makeText(context,"Network error",Toast.LENGTH_SHORT).show()
                    }

                })

        }catch (e: Exception){
            Toast.makeText(context,e.message,Toast.LENGTH_SHORT).show()
        }
    }

//    1
//    private fun hideProgressBar(){
//        binding.progressBar.visibility = View.INVISIBLE
//        isLoading = false
//
//    }
//
//    private fun showProgressBar(){
//        binding.progressBar.visibility = View.VISIBLE
//        isLoading= true
//    }
//
//    var isLoading = false
////    var isLastPage = false
//    var isScrolling = false
//
//    val scrollListener = object : RecyclerView.OnScrollListener(){
//        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
//            super.onScrollStateChanged(recyclerView, newState)
//            if(newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL){
//                isScrolling = true
//            }
//
//        }
//
//        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
//            super.onScrolled(recyclerView, dx, dy)
//
//            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
//            val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
//            val visibleItemCount = layoutManager.childCount
//            val totalItemCount = layoutManager.itemCount
//
//            val isNotLoadingAndNotLastPage =  !isLoading && !isLastPage
//            val isAtLastItem = firstVisibleItemPosition + visibleItemCount >= totalItemCount
//            val isNotBeginning = firstVisibleItemPosition >= 0
//            val isTotalMoreThanVisible = totalItemCount >= QUERY_PAGE_SIZE
//
//            val shouldPaginate = isNotLoadingAndNotLastPage && isAtLastItem && isNotBeginning &&
//                    isTotalMoreThanVisible && isScrolling
//            if(shouldPaginate){
////                viewModel.getBreakingNews("in")
//                Toast.makeText(context,"Loading more posts",Toast.LENGTH_SHORT).show()
//                isScrolling= false
//            }
//        }
//    }
//
//    private fun setUpRecyclerView(list: List<Post>,context: Context){
//        feedAdapter = FeedPostAdapter(list,context)
//        binding.postRecView.apply {
//            adapter = feedAdapter
//            layoutManager = LinearLayoutManager(activity)
//            addOnScrollListener(this@FeedFragment.scrollListener)
//        }
//        feedAdapter.notifyDataSetChanged()
//    }

//2

    private fun setUpRecyclerView(context: Context,postList: ArrayList<GetPostResponseItem>){
        feedAdapter = FeedPostAdapter(listPosts,requireContext(),"Feed", listener2 = { postId , isLiked->
            Log.d("forlike",postId)
            if(isLiked == true){
                //code for liking the post
                likePost(context,postId)
            }else{
                //code for unliking the post
                unlikePost(context,postId)
            }
        }){
            Log.d("harsh",feedAdapter.toString())
            savePostItem(it)
        }


        binding.postRecView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = feedAdapter
            setHasFixedSize(false)
        }
    }

   private fun savePostItem(getPostResponseItem: GetPostResponseItem){
       try {
           val postItem = PostItem(postId = getPostResponseItem._id, desc = getPostResponseItem.body,
               imgUrl = getPostResponseItem.photo, timeStamp = getPostResponseItem.postedBy!!.date ?: "hii",
               postType = getPostResponseItem.postType, userName = getPostResponseItem.postedBy.username, authToken = "will check later")
           viewModel.addNewPostItem(postItem)

       }catch (e: Exception){
           Toast.makeText(context,"unable to save",Toast.LENGTH_SHORT).show()
       }

}

    private fun likePost(context: Context, postId: String){
        try {
            apiClient.getretrofitService(context)
                .likePost(postId)
                .enqueue(object : Callback<LikeResponse>{
                    override fun onResponse(
                        call: Call<LikeResponse>,
                        response: Response<LikeResponse>
                    ) {
                        val likeResponse = response.body()
                        if (response.isSuccessful){
                            if (likeResponse!=null){
                                Toast.makeText(context,"Liked",Toast.LENGTH_SHORT).show()
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

    private fun unlikePost(context: Context, postId: String){
        try {
            apiClient.getretrofitService(context)
                .unlikePost(postId)
                .enqueue(object : Callback<LikeResponse>{
                    override fun onResponse(
                        call: Call<LikeResponse>,
                        response: Response<LikeResponse>
                    ) {
                        val unlikeResponse = response.body()
                        if (response.isSuccessful){
                            if (unlikeResponse!=null){
                                Toast.makeText(context,"Unliked",Toast.LENGTH_SHORT).show()
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