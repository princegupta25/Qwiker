package com.hk.socialmediaapp.Feed.Adapters

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.view.menu.MenuView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hk.socialmediaapp.R
import com.hk.socialmediaapp.profile.GetPostResponseItem
import com.hk.socialmediaapp.profile.Post
import kotlinx.coroutines.NonDisposableHandle.parent
import java.util.Collections.addAll


private const val IMAGE_POST = 1
private const val TEXT_POST = 2


class FeedPostAdapter(
    private var list: ArrayList<GetPostResponseItem>,
    private var context: Context,
    //2
    val tag: String,
    val listener: (GetPostResponseItem) -> Unit


): RecyclerView.Adapter<RecyclerView.ViewHolder>(
) {

//    var list = PostList.getList()

//    val list: ArrayList<Post> = arrayListOf()
//
//    fun submitData(post: List<Post>) {
//        this.list.clear()
//        this.list.addAll(post)
//    }

    class ImagePostViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val userName = itemView.findViewById<TextView>(R.id.user_name)
        val profile_image= itemView.findViewById<ImageView>(R.id.profile_image)
        val image_post=itemView.findViewById<ImageView>(R.id.imagePost)
        //2
        val imageBookmark = itemView.findViewById<CardView>(R.id.imageBookmarkView)
        val bookmarkIcon = itemView.findViewById<ImageView>(R.id.bookmarkIcon)
        val share =itemView.findViewById<ImageView>(R.id.shareView)
        val like =itemView.findViewById<ImageView>(R.id.likeIcon)
         @RequiresApi(Build.VERSION_CODES.M)    // added at 2
         fun bind(getPostResponseItem: GetPostResponseItem, context: Context,tag: String, listener: (GetPostResponseItem) -> Unit){
//             if(getPostResponseItem.postedBy!!.username!=null && getPostResponseItem.postedBy!!.imageUrl!=null){
//             userName.text=getPostResponseItem.postedBy!!.username
//             Glide.with(context!!).load(getPostResponseItem.postedBy.imageUrl).error(R.drawable.person_user)
//                 .into(profile_image)
//                 }

             Log.d("adaptercheck",getPostResponseItem.photo.toString())
             Glide.with(context!!).load(getPostResponseItem.photo).error(R.drawable.person_user)
                 .into(image_post)

             //2
             if(tag == "Profile"){
                 bookmarkIcon.setImageDrawable(context.getDrawable(R.drawable.ic_baseline_delete_forever_24))
             }
             imageBookmark.setOnClickListener {
                 if(tag=="Feed"){
//                     if(post.isLiked==true){
//                         bookmarkIcon.setImageDrawable(context.getDrawable(R.drawable.bookmarkred))
//                     }else{
//                         bookmarkIcon.setImageDrawable(context.getDrawable(R.drawable.ic_bookmark_24))
//                     }
                     bookmarkIcon.setImageDrawable(context.getDrawable(R.drawable.bookmarkred))
                     bookmarkIcon.startAnimation(AnimationUtils.loadAnimation(context,R.anim.pulse))
                     imageBookmark.isEnabled = false
                 }
                 listener(getPostResponseItem)
             }
             share.setOnClickListener {
                 val sendIntent: Intent = Intent().apply {
                     action = Intent.ACTION_SEND
//                     putExtra(Intent.EXTRA_TEXT,"Post( ${getPostResponseItem.postedBy.username} , ${getPostResponseItem.body} )")
                     type = "text/plain"
                 }
                 val shareIntent = Intent.createChooser(sendIntent,null)
                 context.startActivity(shareIntent)
                 share.startAnimation(AnimationUtils.loadAnimation(context,R.anim.pulse))
             }
             like.setOnClickListener {
                 like.setImageDrawable(context.getDrawable(R.drawable.heart))
                 like.startAnimation(AnimationUtils.loadAnimation(context,R.anim.pulse))
             }
         }
    }

    class TextPostViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val userName = itemView.findViewById<TextView>(R.id.tvUsername)
        val profile_image= itemView.findViewById<ImageView>(R.id.profile_image)
        val text_post=itemView.findViewById<TextView>(R.id.text_postTv)
        //2
        val textBookmark = itemView.findViewById<CardView>(R.id.textBookmarkView)
        val bookmarkIcon = itemView.findViewById<ImageView>(R.id.bookmarkIcon)
        val share =itemView.findViewById<ImageView>(R.id.shareView)
        val like =itemView.findViewById<ImageView>(R.id.likeIcon)
        fun bind(getPostResponseItem: GetPostResponseItem,context: Context,tag: String, listener: (GetPostResponseItem) -> Unit){
//            if (getPostResponseItem.postedBy!!.username!=null && getPostResponseItem.postedBy!!.imageUrl!=null) {
//                userName.text = getPostResponseItem.postedBy!!.username
//                Glide.with(context!!).load(getPostResponseItem.postedBy!!.imageUrl)
//                    .error(R.drawable.person_user)
//                    .into(profile_image)
//            }
            text_post.text = getPostResponseItem.body
            //2
            if(tag == "Profile"){
                bookmarkIcon.setImageDrawable(context.getDrawable(R.drawable.ic_baseline_delete_forever_24))
            }
            textBookmark.setOnClickListener {
                    if(tag=="Feed"){
//                     if(post.isLiked=true){
//                         bookmarkIcon.setImageDrawable(context.getDrawable(R.drawable.bookmarkred))
//                     }else{
//                         bookmarkIcon.setImageDrawable(context.getDrawable(R.drawable.ic_bookmark_24))
//                     }
                        bookmarkIcon.setImageDrawable(context.getDrawable(R.drawable.bookmarkred))
                        textBookmark.isEnabled = false
                    }
                listener(getPostResponseItem)
            }
            share.setOnClickListener {
                val sendIntent: Intent = Intent().apply {
                    action = Intent.ACTION_SEND
//                    putExtra(Intent.EXTRA_TEXT,"Post( ${getPostResponseItem.postedBy.username} , ${getPostResponseItem.body} )")
                    type = "text/plain"
                }
                val shareIntent = Intent.createChooser(sendIntent,null)
                context.startActivity(shareIntent)
                share.startAnimation(AnimationUtils.loadAnimation(context,R.anim.pulse))

            }

            like.setOnClickListener {
                like.setImageDrawable(context.getDrawable(R.drawable.heart))
                like.startAnimation(AnimationUtils.loadAnimation(context,R.anim.pulse))
            }

        }
    }





    override fun getItemViewType(position: Int): Int {
       if(list[position].postType=="image"){
           return IMAGE_POST
       }else if(list[position].postType == "text"){
           return TEXT_POST
       }else{
           return TEXT_POST
       }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        if (viewType == TEXT_POST) {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.text_post_item, parent, false)
            context = parent.context
            return FeedPostAdapter.TextPostViewHolder(view)
        } else if (viewType == IMAGE_POST) {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.image_post_item, parent, false)
            context = parent.context
            return FeedPostAdapter.ImagePostViewHolder(view)
        }else{
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.text_post_item, parent, false)
            context = parent.context
            return FeedPostAdapter.TextPostViewHolder(view)
        }

    }

    @RequiresApi(Build.VERSION_CODES.M)   //added at 2
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == IMAGE_POST) {
            (holder as ImagePostViewHolder).bind(list[position],context,tag,listener)
        }  else  {
            (holder as TextPostViewHolder).bind(list[position],context,tag,listener)
        }
    }

    override fun getItemCount(): Int = list.size

}




//    if(bookmarkIcon.backgroundTintList ==context.getColorStateList(R.color.white)){
//        val redColor = context.resources.getColor(R.color.red)
//        bookmarkIcon.setImageDrawable()
//    }else{
//        val redColor = context.resources.getColor(R.color.white)
//        imageBookmark.setCardBackgroundColor(redColor)
//    }

//val redColor = context.resources.getColor(R.color.red)
//textBookmark.setCardBackgroundColor(redColor)