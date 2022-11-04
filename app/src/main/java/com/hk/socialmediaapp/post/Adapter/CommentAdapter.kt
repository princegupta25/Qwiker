package com.hk.socialmediaapp.post.Adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.hk.socialmediaapp.R
import com.hk.socialmediaapp.profile.CommentItem
import com.hk.socialmediaapp.utils.CommentList

class CommentAdapter (
    private val context: Context,
//    private val list: ArrayList<CommentItem>
    private val list: List<CommentItem>
        ):RecyclerView.Adapter<CommentAdapter.CommentViewHolder>(
){




    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        return CommentViewHolder(
            LayoutInflater.from(context).inflate(R.layout.comment_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        Log.d("adaptercomment",position.toString())
        Log.d("adaptercomment",list[position].toString())
        val comment : CommentItem = list[position]

        holder.userComment.text = comment.text

    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class CommentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var userName: TextView =itemView.findViewById(R.id.userNameComment)
        var userComment : TextView =itemView.findViewById(R.id.userComment)
        var userImgUrl : ImageView =itemView.findViewById(R.id.userCommentImage)
    }
}