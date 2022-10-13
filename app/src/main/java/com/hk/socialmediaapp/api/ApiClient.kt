package com.hk.socialmediaapp.api


import android.content.Context
import com.hk.socialmediaapp.loginandsignup.RetrofitInterface
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.HTTP

class ApiClient{
    private lateinit var retrofitInterface: RetrofitInterface
    val BASE_URL = "https://social11.herokuapp.com/api/"

    fun getretrofitService(context: Context): RetrofitInterface{

        if(!::retrofitInterface.isInitialized){
            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okhttpClient(context))
                .build()

            retrofitInterface = retrofit.create(RetrofitInterface::class.java)
        }
        return retrofitInterface
    }

    private fun okhttpClient(context: Context): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor(context))
            .build()
    }
}
