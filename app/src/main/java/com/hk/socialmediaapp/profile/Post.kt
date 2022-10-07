package com.hk.socialmediaapp.profile

data class Post(
    val __v: Int,
    val _id: String,
    val body: String,
    val comments: List<Any>,
    val createdAt: String,
    val likes: List<Any>,
    val photo: String,
    val postType: String,
    val postedBy: String,
    val title: String,
    val updatedAt: String,
    val username: String
)