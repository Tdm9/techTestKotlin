package com.example.googlebookstore.api

import com.example.googlebookstore.api.pojos.BooksResponse
import io.reactivex.Single
import retrofit2.Call
import retrofit2.http.*

interface BooksService {

    @GET("books/v1/volumes?maxResults=40")
    fun search40Books(@Query("startIndex") startIndex: Int,
                      @Query("q") searchString: String,
                      @Query("key") apiKey: String
    ): Call<BooksResponse>

    @GET("books/v1/volumes?q=ios&maxResults=20")
    fun searchBooks(): Call<BooksResponse>

    @GET("volumes/{id}")
    fun getBook(
            @Path("id") id: String
    ): Single<BooksResponse>
}