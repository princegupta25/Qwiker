package com.hk.socialmediaapp.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [PostItem::class], version = 3, exportSchema = false)
abstract class PostItemRoomDatabase: RoomDatabase() {

    abstract fun postItemDao() : PostItemDao

    companion object{
        @Volatile
        private var INSTANCE: PostItemRoomDatabase?=null
        fun getDatabase(context: Context): PostItemRoomDatabase{
            return INSTANCE ?: synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    PostItemRoomDatabase::class.java,
                    "post_item_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                return instance
            }
        }
    }


}