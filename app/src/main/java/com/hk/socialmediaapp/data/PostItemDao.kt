package com.hk.socialmediaapp.data

import android.content.ClipData
import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface PostItemDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(postItem: PostItem)

    @Update
    fun update(postItem: PostItem)

    @Delete
    fun delete(postItem: PostItem)

    @Query("SELECT * FROM postItem WHERE post_id = :id")
    fun getItem(id: Int): Flow<PostItem>

    @Query("SELECT * FROM postItem ORDER BY time_stamp ASC")
    fun getItems(): Flow<List<PostItem>>
}