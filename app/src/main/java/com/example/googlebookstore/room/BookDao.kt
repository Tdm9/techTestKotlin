package com.example.googlebookstore.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.googlebookstore.viewmodel.BookDetailModel

@Dao
interface BookDao {
    @Query("SELECT * FROM book")
    fun getAll(): List<BookDetailModel>

    @Query("SELECT * FROM book WHERE title =(:title) AND image=(:imgLnk)")
    fun loadAllByIds(title: String,imgLnk: String): List<BookDetailModel>

    @Insert
    fun insertAll(vararg users: BookDetailModel)

    @Delete
    fun delete(user: BookDetailModel)
}