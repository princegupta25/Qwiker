package com.hk.socialmediaapp

import android.app.Application
import com.hk.socialmediaapp.data.PostItemRoomDatabase

class QwikerApplication: Application() {
    val database: PostItemRoomDatabase by lazy {
        PostItemRoomDatabase.getDatabase(this)
    }
}