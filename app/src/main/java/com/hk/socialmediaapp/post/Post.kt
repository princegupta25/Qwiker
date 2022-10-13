package com.hk.socialmediaapp.post

data class Post(
    var post_id: String = "",
    var description: String = "",
    var image_url: String? = null,
    var owner_user_id: String = "",
    var post_type: String = "",
    var time_stamp: String = ""


)
