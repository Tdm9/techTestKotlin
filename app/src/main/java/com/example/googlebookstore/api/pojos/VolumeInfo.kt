package com.example.googlebookstore.api.pojos

import android.os.Parcelable
import com.example.googlebookstore.viewmodel.BookDetailModel
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ImageLinks(@SerializedName("smallThumbnail") val smallThumbnail: String,
                      @SerializedName("thumbnail") val thumbnail: String) : Parcelable

@Parcelize
data class VolumeInfo(@SerializedName("title") val title: String,
                      @SerializedName("subtitle") val subtitle: String,
                      @SerializedName("authors") val authors: List<String>,
                      @SerializedName("description") val description: String,
                      @SerializedName("imageLinks") val imageLinks: ImageLinks
) : Parcelable {
    fun toBookDetailModel(): BookDetailModel {

        return BookDetailModel(
            if (title.isNullOrBlank())"" else title,
            if (subtitle.isNullOrBlank())"" else subtitle,
            if (authors.isNullOrEmpty()) "" else authors[0],
            if (description.isNullOrBlank())"" else description,
            if (imageLinks.equals(null) && imageLinks.thumbnail.isNullOrBlank())"" else imageLinks.thumbnail,
            null)
    }
}