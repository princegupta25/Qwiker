package com.hk.socialmediaapp.profile

data class CommentItem(
    val _id: String,
    val content: String,
    val postId: String,
    val text: String,
    val writer: String
)