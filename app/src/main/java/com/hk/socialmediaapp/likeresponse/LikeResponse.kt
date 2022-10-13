package com.hk.socialmediaapp.likeresponse

data class LikeResponse(
    val __v: Int,
    val _id: String,
    val body: String,
    val comments: List<Comment>,
    val likes: List<String>,
    val photo: String,
    val postType: String,
    val postedBy: PostedBy,
    val title: String,
    val updatedAt: String
)