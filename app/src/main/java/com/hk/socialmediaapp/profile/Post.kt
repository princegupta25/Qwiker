package com.hk.socialmediaapp.profile

data class Post(
    val __v: Int,
    val _id: String,
    val body: String,
    val comments: List<CommentItem>,
    val createdAt: String,
    val likes: List<String>,
    val photo: String,
    val postType: String,
    val postedBy: String,
    val title: String,
    val updatedAt: String,
    val username: String
)