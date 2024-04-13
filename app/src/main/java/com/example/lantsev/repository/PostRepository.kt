package com.example.lantsev.repository
import androidx.lifecycle.LiveData
import com.example.lantsev.dto.Post

interface PostRepository {
    fun getAll(): LiveData<List<Post>>
    fun likeById(id: Int)
    fun shareById(id: Int)
    fun removeById(id: Int)
    fun save(post: Post)


}