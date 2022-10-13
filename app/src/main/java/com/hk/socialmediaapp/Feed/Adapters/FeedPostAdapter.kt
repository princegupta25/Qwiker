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
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.view.menu.MenuView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.gson.reflect.TypeToken
import com.hk.UI.commentFragemnt
import com.hk.socialmediaapp.Comment.PostCommentActivity
import com.hk.socialmediaapp.R
import com.hk.socialmediaapp.api.SessionManager
import com.hk.socialmediaapp.databinding.AddBottomSheetBinding
import com.hk.socialmediaapp.databinding.FragmentCommentFragemntBinding
import com.hk.socialmediaapp.post.CreateImagePost
import com.hk.socialmediaapp.post.CreateTextPost
import com.hk.socialmediaapp.profile.CommentItem
import com.hk.socialmediaapp.profile.GetPostResponseItem
import com.hk.socialmediaapp.profile.Post
import com.hk.socialmediaapp.utils.CommentList
import kotlinx.coroutines.NonDisposableHandle.parent
import java.lang.reflect.Type
import java.util.Collections.addAll


private const val IMAGE_POST = 1
private const val TEXT_POST = 2


class FeedPostAdapter(
    private var list: ArrayList<GetPostResponseItem>,
    private var context: Context,
    //2
    val tag: String,
    val listener2: ((String,Boolean) -> Unit)?,
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

    val sessionManager: SessionManager = SessionManager(context)


    class ImagePostViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val userName = itemView.findViewById<TextView>(R.id.user_name)
        val profile_image= itemView.findViewById<ImageView>(R.id.profile_image)
        val image_post=itemView.findViewById<ImageView>(R.id.imagePost)
        //2
        val imageBookmark = itemView.findViewById<CardView>(R.id.imageBookmarkView)

        val comment=itemView.findViewById<CardView>(R.id.cardComment)
        val commentIcon = itemView.findViewById<ImageView>(R.id.commentIcon)
        val shareCard=itemView.findViewById<CardView>(R.id.shareCard)
        var isLiked:Boolean = false
        var isBookmarked:Boolean = false
//         val commentBottomSheet = itemView.findViewById<LinearLayout>(R.id.commentBottomSheet)
//        private lateinit var sheetBinding: FragmentCommentFragemntBinding
//        lateinit var bottomSheetDialog: BottomSheetDialog



        val bookmarkIcon = itemView.findViewById<ImageView>(R.id.bookmarkIcon)
        val share =itemView.findViewById<ImageView>(R.id.shareView)
        val like =itemView.findViewById<ImageView>(R.id.likeIcon)

         @RequiresApi(Build.VERSION_CODES.M)    // added at 2
         fun bind(getPostResponseItem: GetPostResponseItem, context: Context,tag: String,listener2: ((String,Boolean) -> Unit?)?, listener: (GetPostResponseItem) -> Unit,sessionManager: SessionManager){
//             if(getPostResponseItem.postedBy!!.username!=null && getPostResponseItem.postedBy!!.imageUrl!=null){
//             userName.text=getPostResponseItem.postedBy!!.username
             if (getPostResponseItem!=null && getPostResponseItem.postedBy!= null && getPostResponseItem.postedBy.imageUrl != null){
             Glide.with(context!!).load(getPostResponseItem.postedBy.imageUrl.subSequence(19,getPostResponseItem.postedBy.imageUrl.length)).error(R.drawable.person_user)
                 .into(profile_image)
                 }


             Log.d("adaptercheck",getPostResponseItem.photo.toString())
             if (getPostResponseItem.photo!=null) {
                 Glide.with(context!!).load(
                     getPostResponseItem.photo.subSequence(
                         19,
                         getPostResponseItem.photo.length
                     )
                 ).error(R.drawable.person_user)
                     .into(image_post)
             }

             //2
             if(tag == "Profile" || tag =="Bookmark"){
                 bookmarkIcon.setImageDrawable(context.getDrawable(R.drawable.ic_baseline_delete_forever_24))
             }
             if(tag=="Bookmark"){
                 commentIcon.setImageDrawable(context.getDrawable(R.drawable.ic_send_bold))
                 shareCard.visibility = View.GONE
                 shareCard.isEnabled = false

                 comment.setOnClickListener {
                     val sendIntent: Intent = Intent().apply {
                         action = Intent.ACTION_SEND
//                     putExtra(Intent.EXTRA_TEXT,"Post( ${getPostResponseItem.postedBy.username} , ${getPostResponseItem.body} )")
                         type = "text/plain"
                     }
                     val shareIntent = Intent.createChooser(sendIntent,null)
                     context.startActivity(shareIntent)
                     share.startAnimation(AnimationUtils.loadAnimation(context,R.anim.pulse))
                 }

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

             if (tag!="Bookmark") {
                 share.setOnClickListener {
                     val sendIntent: Intent = Intent().apply {
                         action = Intent.ACTION_SEND
//                     putExtra(Intent.EXTRA_TEXT,"Post( ${getPostResponseItem.postedBy.username} , ${getPostResponseItem.body} )")
                         type = "text/plain"
                     }
                     val shareIntent = Intent.createChooser(sendIntent, null)
                     context.startActivity(shareIntent)
                     commentIcon.startAnimation(AnimationUtils.loadAnimation(context, R.anim.pulse))
                 }
             }

//             sheetBinding = FragmentCommentFragemntBinding.bind(
//                 LayoutInflater.from(context).inflate(R.layout.fragment_comment_fragemnt,null)
//             )
//             bottomSheetDialog = BottomSheetDialog(
//                 context,R.style.BottomSheetDialogTheme
//             )

             if (tag!="Bookmark") {
                 comment.setOnClickListener {

//                CommentList.commentList = getPostResponseItem.comments as ArrayList<CommentItem>
                     CommentList.commentList = getPostResponseItem.comments


                     val intent = Intent(context, PostCommentActivity::class.java)
                     intent.putExtra("UserName", getPostResponseItem.username.toString())
                     intent.putExtra(
                         "UserProfileImage",
                         getPostResponseItem.postedBy?.imageUrl.toString()
                     )
                     intent.putExtra("Caption", getPostResponseItem.title.toString())
                     intent.putExtra("postId", getPostResponseItem._id)
                     context.startActivity(intent)

//                val bottomSheetView = LayoutInflater.from(context).inflate(
//                    R.layout.fragment_comment_fragemnt,
//                    itemView.findViewById(R.id.commentBottomSheet)
//                )
//                bottomSheetDialog.setContentView(bottomSheetView)
//                bottomSheetDialog.show()
                 }
             }


             //code to check whether it was already liked before.
             if (getPostResponseItem.likes!=null) {
                 if (getPostResponseItem.likes.contains(sessionManager.fetchUserId())){
                     isLiked=true
                     like.setImageDrawable(context.getDrawable(R.drawable.heart))
                 }
             }

             like.setOnClickListener {
                 if(isLiked==false){
                     isLiked = true
                     like.setImageDrawable(context.getDrawable(R.drawable.heart))
                     like.startAnimation(AnimationUtils.loadAnimation(context,R.anim.pulse))
                     //like increase code

                 }else{
                     isLiked=false
                     like.setImageDrawable(context.getDrawable(R.drawable.ic_heart_bold))
                     like.startAnimation(AnimationUtils.loadAnimation(context,R.anim.pulse))
                     //like decrease code

                 }
             }
         }
    }

    class TextPostViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val userName = itemView.findViewById<TextView>(R.id.tvUsername)
        val profile_image= itemView.findViewById<ImageView>(R.id.profile_image)
        val text_post=itemView.findViewById<TextView>(R.id.text_postTv)
        val comment=itemView.findViewById<CardView>(R.id.cardComment)
        val commentIcon = itemView.findViewById<ImageView>(R.id.commentIcon)
        val shareCard=itemView.findViewById<CardView>(R.id.shareCard)
        var isLiked:Boolean = false
        var isBookmarked: Boolean = false
        //         val commentBottomSheet = itemView.findViewById<LinearLayout>(R.id.commentBottomSheet)
//        private lateinit var sheetBinding: FragmentCommentFragemntBinding
//        lateinit var bottomSheetDialog: BottomSheetDialog


        //2
        val textBookmark = itemView.findViewById<CardView>(R.id.textBookmarkView)
        val bookmarkIcon = itemView.findViewById<ImageView>(R.id.bookmarkIcon)
        val share =itemView.findViewById<ImageView>(R.id.shareView)
        val like =itemView.findViewById<ImageView>(R.id.likeIcon)
        fun bind(getPostResponseItem: GetPostResponseItem,context: Context,tag: String,listener2: ((String,Boolean) -> Unit?)?, listener: (GetPostResponseItem) -> Unit,sessionManager: SessionManager){
           if (getPostResponseItem!=null && getPostResponseItem.postedBy!= null && getPostResponseItem.postedBy.imageUrl != null){
             Glide.with(context!!).load(getPostResponseItem.postedBy.imageUrl.subSequence(19,getPostResponseItem.postedBy.imageUrl.length)).error(R.drawable.person_user)
                 .into(profile_image)
                 }
            text_post.text = getPostResponseItem.body
            //2
            if(tag == "Profile" || tag =="Bookmark"){
                bookmarkIcon.setImageDrawable(context.getDrawable(R.drawable.ic_baseline_delete_forever_24))
            }
            if(tag=="Bookmark"){
                commentIcon.setImageDrawable(context.getDrawable(R.drawable.ic_send_bold))
                shareCard.visibility = View.GONE
                shareCard.isEnabled = false

                comment.setOnClickListener {
                    val sendIntent: Intent = Intent().apply {
                        action = Intent.ACTION_SEND
//                     putExtra(Intent.EXTRA_TEXT,"Post( ${getPostResponseItem.postedBy.username} , ${getPostResponseItem.body} )")
                        type = "text/plain"
                    }
                    val shareIntent = Intent.createChooser(sendIntent,null)
                    context.startActivity(shareIntent)
                    commentIcon.startAnimation(AnimationUtils.loadAnimation(context,R.anim.pulse))
                }

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

            if(tag!="Bookmark") {
                share.setOnClickListener {
                    val sendIntent: Intent = Intent().apply {
                        action = Intent.ACTION_SEND
//                    putExtra(Intent.EXTRA_TEXT,"Post( ${getPostResponseItem.postedBy.username} , ${getPostResponseItem.body} )")
                        type = "text/plain"
                    }
                    val shareIntent = Intent.createChooser(sendIntent, null)
                    context.startActivity(shareIntent)
                    share.startAnimation(AnimationUtils.loadAnimation(context, R.anim.pulse))
                }
            }

//            sheetBinding = FragmentCommentFragemntBinding.bind(
//                LayoutInflater.from(context).inflate(R.layout.fragment_comment_fragemnt,null)
//            )
//            bottomSheetDialog = BottomSheetDialog(
//                context,R.style.BottomSheetDialogTheme
//            )

            if (tag!="Bookmark") {
                comment.setOnClickListener {

//                CommentList.commentList = getPostResponseItem.comments as ArrayList<CommentItem>
                    CommentList.commentList = getPostResponseItem.comments


                    val intent = Intent(context, PostCommentActivity::class.java)
                    intent.putExtra("UserName", getPostResponseItem.username.toString())
                    intent.putExtra(
                        "UserProfileImage",
                        getPostResponseItem.postedBy?.imageUrl.toString()
                    )
                    intent.putExtra("Caption", getPostResponseItem.title.toString())
                    intent.putExtra("postId", getPostResponseItem._id)
                    context.startActivity(intent)

//                val bottomSheetView = LayoutInflater.from(context).inflate(
//                    R.layout.fragment_comment_fragemnt,
//                    itemView.findViewById(R.id.commentBottomSheet)
//                )
//                bottomSheetDialog.setContentView(bottomSheetView)
//                bottomSheetDialog.show()
                }
            }


            if (getPostResponseItem.likes!=null) {
                if (getPostResponseItem.likes.contains(sessionManager.fetchUserId())){
                    isLiked=true
                    like.setImageDrawable(context.getDrawable(R.drawable.heart))
                }
            }


            like.setOnClickListener {
                if (isLiked == false) {
                    isLiked = true
                    like.setImageDrawable(context.getDrawable(R.drawable.heart))
                    like.startAnimation(AnimationUtils.loadAnimation(context, R.anim.pulse))
                    //like increase code


                } else {
                    isLiked = false
                    like.setImageDrawable(context.getDrawable(R.drawable.ic_heart_bold))
                    like.startAnimation(AnimationUtils.loadAnimation(context, R.anim.pulse))
                    //like decrease code
                }
                if (listener2 != null) {
                    listener2(getPostResponseItem._id,isLiked)
                }
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
            (holder as ImagePostViewHolder).bind(list[position],context,tag,listener2,listener,sessionManager)
        }  else  {
            (holder as TextPostViewHolder).bind(list[position],context,tag,listener2,listener,sessionManager)
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