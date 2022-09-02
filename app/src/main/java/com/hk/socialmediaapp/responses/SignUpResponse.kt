package com.hk.socialmediaapp.responses

import com.google.gson.annotations.SerializedName

data class SignUpResponse(
    @SerializedName("user")
    var user: String
)