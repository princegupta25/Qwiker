package com.hk.socialmediaapp.loginandsignup



import com.hk.socialmediaapp.likeresponse.LikeResponse
import com.hk.socialmediaapp.profile.GetPostResponse
import com.hk.socialmediaapp.profile.PostResponse
import com.hk.socialmediaapp.profile.UserResponse
import com.hk.socialmediaapp.profile.UserUpdateResponse
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
    fun  updateUser(@Field("username") username: String,@Field("about") about: String,@Field("imageUrl") imageUrl: String):Call<UserUpdateResponse>

    @FormUrlEncoded
    @POST("posts/createpost")
    fun createPost(@Field("photo") photo: String?,
                   @Field("title") title: String?,@Field("body") body: String,
                   @Field("postType") postType: String,@Field("username") username: String): Call<PostResponse>

    @GET("posts/showAllPosts")
    fun getAllPosts(): Call<GetPostResponse>

    @GET("posts/showMyPosts")
    fun getMyPosts(): Call<GetPostResponse>

    @FormUrlEncoded
    @PUT("posts/comment")
    fun postComment(@Field("text") text: String,@Field("content") content: String,@Field("postId") postId: String) : Call<String>

    @FormUrlEncoded
    @PUT("posts/like")
    fun likePost(@Field("postId") postId: String): Call<LikeResponse>

    @FormUrlEncoded
    @PUT("posts/unlike")
    fun unlikePost(@Field("postId") postId: String): Call<LikeResponse>

    @DELETE("posts/deletepost/{id}")
    fun deletePost(@Path("id") postid:String): Call<LikeResponse>


}

//    @FormUrlEncoded
//    @POST("user/myProfile")
//    fun updateUser(@Field("token") token: String,@Field("username") username: String,@Field("about") about: String,@Field("imageUrl") imageUrl: String):Call<UserResponse>

//    @FormUrlEncoded
//    @POST("user/myProfile")
//    fun  updateUser(@Field("username") username: String,@Field("about") about: String,@Field("imageUrl") imageUrl: String):Call<UserResponse>
//    //change type of userResponse to userUpdateResponse