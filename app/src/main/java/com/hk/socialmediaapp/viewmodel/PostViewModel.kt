package com.hk.socialmediaapp.viewmodel

import android.util.Log
import androidx.lifecycle.*
import com.hk.socialmediaapp.data.PostItem
import com.hk.socialmediaapp.data.PostItemDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class PostViewModel(private val postItemDao: PostItemDao): ViewModel() {

//    val savedPostItems: LiveData<List<PostItem>> = postItemDao.getItems().asLiveData()
//    private lateinit var savedPostItems: LiveData<List<PostItem>>
    fun getAllItems(): Flow<List<PostItem>> = postItemDao.getItems()

    private fun insertPostItem(postItem: PostItem){
//        viewModelScope.launch {
//            postItemDao.insert(postItem)
//        }
        CoroutineScope(Dispatchers.IO).launch {
            postItemDao.insert(postItem)
        }

    }

//    fun getItems(): LiveData<List<PostItem>>{
//        CoroutineScope(Dispatchers.IO).launch {
//            savedPostItems = postItemDao.getItems().asLiveData()
//        }
//        return savedPostItems
//    }

    private fun getNewPostItem(postId: String,desc: String?,imgUrl : String?,postType: String,timeStamp: String,userName: String,authToken: String): PostItem{
        return PostItem(postId = postId, desc = desc, imgUrl = imgUrl,
            postType = postType, timeStamp = timeStamp,
           userName = userName, authToken = authToken
        )
    }

    fun addNewPostItem(postItem: PostItem){
        insertPostItem(postItem)
    }
    // retrieve one post and update any post functionality not added

    fun deletePostItem(postItem: PostItem){
//        viewModelScope.launch {
//            postItemDao.delete(postItem)
//        }
        CoroutineScope(Dispatchers.IO).launch {
            Log.d("Delete",postItemDao.toString())
            Log.d("Delete",postItem.toString())
            postItemDao.delete(postItem)
            Log.d("Delete",postItemDao.delete(postItem).toString())
        }
    }
}

class InventoryViewModelFactory(private val postItemDao: PostItemDao) : ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PostViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PostViewModel(postItemDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}