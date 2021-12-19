package com.example.googlebookstore.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.googlebookstore.viewmodel.BookDetailModel

@Database(entities = [BookDetailModel::class], version = 1)
abstract class Database : RoomDatabase() {
    abstract fun userDao(): BookDao
}