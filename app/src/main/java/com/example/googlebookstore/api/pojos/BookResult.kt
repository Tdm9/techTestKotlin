package com.example.googlebookstore.api.pojos

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class BookResult(@SerializedName("id") val id: String,
                      @SerializedName( "kind") val kind: String,
                      @SerializedName("selfLink") val selfLink: String,
                      @SerializedName( "volumeInfo") val volumeInfo: VolumeInfo
) : Parcelable