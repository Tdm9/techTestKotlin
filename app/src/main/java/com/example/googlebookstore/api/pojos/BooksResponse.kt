package com.example.googlebookstore.api.pojos

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import com.google.gson.annotations.SerializedName

@Parcelize
data class BooksResponse(@SerializedName("totalItems") val totalItems: Int,
                         @SerializedName( "items") val items: List<BookResult>) : Parcelable
