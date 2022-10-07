package com.hk.socialmediaapp.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "postItem")
data class PostItem(
//    @PrimaryKey(autoGenerate = true)
//    val id: Int = 0,
    @PrimaryKey
    @ColumnInfo(name = "post_id")
    val postId: String,
    @ColumnInfo(name = "description")
    val desc: String?,
    @ColumnInfo(name = "image_url")
    val imgUrl: String?,
    @ColumnInfo(name = "post_type")
    val postType: String,
    @ColumnInfo(name = "time_stamp")
    val timeStamp: String,
    @ColumnInfo(name = "user_name")
    val userName: String?,
    @ColumnInfo(name = "auth_token")
    val authToken: String
)