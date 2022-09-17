package com.hk.socialmediaapp.loginandsignup



import com.hk.socialmediaapp.profile.UserResponse
import com.hk.socialmediaapp.responses.LoginRequest
import com.hk.socialmediaapp.responses.LoginResponse
import com.hk.socialmediaapp.responses.SignUpRequest
import com.hk.socialmediaapp.responses.SignUpResponse
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*


interface RetrofitInterface {

    @FormUrlEncoded
    @POST("auth/register")
    fun signUp(@Field("name") name: String, @Field("email") email:String, @Field("password") password: String): Call<SignUpResponse>

    @FormUrlEncoded
    @POST("auth/login")
    fun login(@Field("email") email: String, @Field("password") password: String): Call<LoginResponse>

    @GET("user/myProfile")
    fun getUser():Call<UserResponse>

    @FormUrlEncoded
    @POST("user/myProfile")
    fun updateUser(@Field("token") token: String,@Field("username") username: String,@Field("about") about: String,@Field("imageUrl") imageUrl: String):Call<UserResponse>

}

