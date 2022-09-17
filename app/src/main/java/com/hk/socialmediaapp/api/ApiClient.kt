package com.hk.socialmediaapp.api


import com.hk.socialmediaapp.loginandsignup.RetrofitInterface
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiClient{
    private lateinit var retrofitInterface: RetrofitInterface
    val BASE_URL = "https://social11.herokuapp.com/api/"

    fun getretrofitService(): RetrofitInterface{

        if(!::retrofitInterface.isInitialized){
            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            retrofitInterface = retrofit.create(RetrofitInterface::class.java)
        }
        return retrofitInterface
    }
}