package com.example.googlebookstore.viewmodel

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.googlebookstore.api.pojos.ImageLinks
import com.example.googlebookstore.api.pojos.VolumeInfo
import com.google.gson.annotations.SerializedName

@Entity(tableName = "book")
data class BookDetailModel(
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "subtitle") val subtitle: String,
    @ColumnInfo(name = "author") val author: String,
    @ColumnInfo(name = "description") val description: String,
    @ColumnInfo(name = "image") val image: String,
    @PrimaryKey(autoGenerate = true) val id: Long?) {

    fun toVolumeInfo():VolumeInfo = VolumeInfo(title,subtitle, arrayOf(author).asList(),description,
        ImageLinks(image,image))
}