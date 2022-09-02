package com.hk.socialmediaapp.loginandsignup



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
    @POST("register")
    fun signUp(@Field("name") name: String, @Field("email") email:String, @Field("password") password: String): Call<SignUpResponse>

    @FormUrlEncoded
    @POST("login")
    fun login(@Field("email") email: String, @Field("password") password: String): Call<LoginResponse>

}

