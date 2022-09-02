package com.hk.socialmediaapp.responses

import com.google.gson.annotations.SerializedName

data class LoginResponse (
//    @SerializedName("Auth-Token")
//    var authToken: String,
//
//    @SerializedName("status_code")
//    var statusCode: Int

   @SerializedName("token")
   var token : String
)