package com.hk.socialmediaapp.likeresponse

data class Comment(
    val _id: String,
    val content: String,
    val postId: String,
    val text: String,
    val writer: Writer
)