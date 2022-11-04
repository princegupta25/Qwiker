package com.hk.socialmediaapp.profile

data class GetPostResponseItem(
//    val __v: Int,
    val _id: String,
    val body: String?,
    val comments: List<CommentItem>?,
//    val createdAt: String,
    val likes: List<String>?,
    val photo: String?,
    val postType: String,
    val postedBy: PostedBy?,
    val title: String?,
//    val updatedAt: String,
    val username: String?
)