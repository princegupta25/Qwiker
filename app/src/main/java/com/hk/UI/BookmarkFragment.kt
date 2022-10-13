package com.hk.UI

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.coroutineScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.hk.socialmediaapp.Feed.Adapters.FeedPostAdapter
import com.hk.socialmediaapp.QwikerApplication
import com.hk.socialmediaapp.R
import com.hk.socialmediaapp.api.ApiClient
import com.hk.socialmediaapp.api.SessionManager
import com.hk.socialmediaapp.data.PostItem
import com.hk.socialmediaapp.databinding.FragmentBookmarkBinding
import com.hk.socialmediaapp.profile.GetPostResponseItem
import com.hk.socialmediaapp.viewmodel.InventoryViewModelFactory
import com.hk.socialmediaapp.viewmodel.PostViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


class BookmarkFragment : Fragment() {

    private val viewModel: PostViewModel by activityViewModels(){
        InventoryViewModelFactory(
            (activity?.application as QwikerApplication).database
                .postItemDao()
        )
    }
//    private lateinit var savedItems: List<Post>
    private lateinit var binding: FragmentBookmarkBinding
    private lateinit var sessionManager: SessionManager
    private lateinit var apiClient: ApiClient
    private lateinit var postList: MutableList<GetPostResponseItem>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
       binding =  FragmentBookmarkBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        apiClient = ApiClient()
        sessionManager = SessionManager(requireContext())

        postList = mutableListOf()



        lifecycle.coroutineScope.launch{
            viewModel.getAllItems().collect(){
                for (postItem in it){
//                    val post: Post = Post(postItem.postId,postItem.desc,postItem.imgUrl,
//                                           postItem.postType,postItem.timeStamp,postItem.userName,postItem.authToken)
//                    postList.add(post)
                    val post : GetPostResponseItem = GetPostResponseItem(postItem.postId,postItem.desc,null,null,postItem.imgUrl,
                        postItem.postType,null,null,postItem.userName)
                    postList.add(post)
                }
                val adapter = FeedPostAdapter(postList as ArrayList<GetPostResponseItem> ,requireContext(),"Bookmark",null){ post->
                    showConfirmationDialog(post)
                    postList.remove(post)
                }
                binding.savedPostRecView.adapter =adapter
                binding.savedPostRecView.layoutManager = LinearLayoutManager(context)

            }
        }

    }


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
        try {
            val postItem = PostItem(
                postId = post._id,
                desc = post.body,
                imgUrl = post.photo,
                timeStamp = post.postedBy!!.date,
                postType = post.postType,
                userName = post.postedBy.username,
                authToken = "will look later"
            )
            viewModel.deletePostItem(postItem)
        }catch (e: Exception){
            Toast.makeText(requireContext(),"Null",Toast.LENGTH_SHORT).show()
        }
    }

}