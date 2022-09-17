package com.hk.socialmediaapp.utils

import android.net.Uri
import com.hk.socialmediaapp.post.Post


object PostHelperKit {

    var post: Post? = null
    var selected_image: String? = null
    var selected_image_uri: Uri? = null

    fun clearPostData() {
        post = null
    }

    fun clearSelectedImage() {
        selected_image = null
        selected_image_uri = null

    }


}