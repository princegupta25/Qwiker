package com.hk.UI

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.hk.socialmediaapp.R
import com.hk.socialmediaapp.databinding.FragmentCommentFragemntBinding
import com.hk.socialmediaapp.post.Adapter.CommentAdapter
import com.hk.socialmediaapp.profile.CommentItem
import com.hk.socialmediaapp.utils.CommentList


class commentFragemnt : BottomSheetDialogFragment(){
    lateinit var  binding: FragmentCommentFragemntBinding
     lateinit var  adapter: CommentAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentCommentFragemntBinding.inflate(layoutInflater)
        return  binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val list= arrayListOf<CommentItem>(CommentItem(text="Good", writer="62b55cc902b708bcd8a18222", postId="63115f6d7d4449eb6ffa9b68", content="Good", _id="633d0c996d9c4080a8388dc3"))
        adapter = CommentAdapter(requireContext(),list)
//        adapter = CommentAdapter(requireContext(),CommentList.commentList)
        binding.commentRV.adapter = adapter
        binding.commentRV.layoutManager = LinearLayoutManager(requireContext())

    }

}