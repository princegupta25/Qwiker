package com.hk.socialmediaapp.api

import android.content.Context
import android.content.SharedPreferences
import com.hk.socialmediaapp.R

class SessionManager(context: Context) {
    private var prefs: SharedPreferences = context.getSharedPreferences(context.getString(R.string.app_name),Context.MODE_PRIVATE)

    companion object{
        const val USER_TOKEN = "user_token"
        const val USER_NAME = "user_name"
        const val USER_ID = "user_id"
//        const val PROFILE_IMG = "img_profile"
    }

    fun saveAuthToken(token: String){
        val editor = prefs.edit()
        editor.putString(USER_TOKEN,token)
        editor.apply()
    }

    fun fetchAuthToken(): String?{
        return prefs.getString(USER_TOKEN,null)
    }

    fun saveUserName(name: String){
        val editor = prefs.edit()
        editor.putString(USER_NAME,name)
        editor.apply()
    }

    fun saveUserID(Id: String){
        val editor = prefs.edit()
        editor.putString(USER_ID,Id)
        editor.apply()
    }

    fun fetchUserId(): String?{
        return prefs.getString(USER_ID,null)
    }

    fun removeUserId(){
        val editor = prefs.edit()
        editor.putString(USER_ID,null)
        editor.apply()
    }



    fun fetchUserName(): String?{
        return prefs.getString(USER_NAME,null)
    }
    fun removeAuthToken(){
        val editor = prefs.edit()
        editor.putString(USER_TOKEN,null)
        editor.apply()
    }
    fun removeUsername(){
        val editor = prefs.edit()
        editor.putString(USER_NAME,null)
        editor.apply()
    }

//    fun saveProfileImgUrl(name: String){
//        val editor = prefs.edit()
//        editor.putString(PROFILE_IMG,null)
//        editor.apply()
//    }
//
//    fun fetchProfileImgUrl(): String?{
//        return prefs.getString(PROFILE_IMG,null)
//    }
//
//    fun removeImgProfile(){
//        val editor = prefs.edit()
//        editor.putString(PROFILE_IMG,null)
//        editor.apply()
//    }

}