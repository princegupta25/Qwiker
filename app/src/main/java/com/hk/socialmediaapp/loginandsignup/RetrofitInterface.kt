package com.hk.socialmediaapp.loginandsignup



import com.google.firebase.firestore.auth.User
import com.hk.socialmediaapp.likeresponse.LikeResponse
import com.hk.socialmediaapp.profile.*
import com.hk.socialmediaapp.responses.LoginResponse
import com.hk.socialmediaapp.responses.SignUpResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
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
//
//    @Multipart
//    @FormUrlEncoded
//    @PATCH("user/myProfile")
//    fun  updateUser(@Part image:  MultipartBody.Part,
////        @Field("imageUrl") imageUrl: String,
//                    @Field("about") about: String,
//                    @Field("username") username: String):Call<UserUpdateResponse>


    @Multipart
    @PATCH("user/myProfile")
    fun  updateUser(@Part image:  MultipartBody.Part,
                    @Part("about") about: RequestBody,
                    @Part("username") username: RequestBody
    ):Call<UserUpdateResponse>
//
//    @FormUrlEncoded
//    @POST("posts/createpost")
//    fun createPost(@Field("photo") photo: String,
//                   @Field("title") title: String,
//                   @Field("body") body: String,
//                   @Field("postType") postType: String,
//                   @Field("username") username: String): Call<PostResponse>

    @Multipart
    @POST("posts/createpost")
    fun createPost(@Part image:  MultipartBody.Part,
                   @Part("title") title: RequestBody,
                   @Part("body") body: RequestBody,
                   @Part("postType") postType: RequestBody,
                   @Part("username") username: RequestBody
    ): Call<PostResponse>

    @GET("posts/showAllPosts")
    fun getAllPosts(): Call<GetPostResponse>

    @GET("posts/showMyPosts")
    fun getMyPosts(): Call<GetPostResponse>

    @FormUrlEncoded
    @PUT("posts/comment")
    fun postComment(@Field("text") text: String,@Field("content") content: String,@Field("postId") postId: String) : Call<LikeResponse>

    @FormUrlEncoded
    @PUT("posts/like")
    fun likePost(@Field("postId") postId: String): Call<LikeResponse>

    @FormUrlEncoded
    @PUT("posts/unlike")
    fun unlikePost(@Field("postId") postId: String): Call<LikeResponse>

    @DELETE("posts/deletepost/{id}")
    fun deletePost(@Path("id") postid:String): Call<LikeResponse>

//    @FormUrlEncoded
//    @PUT("posts/comment")
//    fun postComment(@Field("text") text: String,@Field("content") content: String,@Field("postId") postId: String) : Call<String>
//
//    @FormUrlEncoded
//    @PUT("posts/like")
//    fun likePost(@Field("postId") postId: String): Call<LikeResponse>
//
//    @FormUrlEncoded
//    @PUT("posts/unlike")
//    fun unlikePost(@Field("postId") postId: String): Call<LikeResponse>
//
//    @DELETE("posts/deletepost/{id}")
//    fun deletePost(@Path("id") postid:String): Call<LikeResponse>


}
