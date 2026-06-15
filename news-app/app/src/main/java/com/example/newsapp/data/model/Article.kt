package com.example.newsapp.data.model

import com.google.gson.annotations.SerializedName

data class Source(
    val id: String?,
    val name: String?
)

data class Article(
    val source: Source?,
    val author: String?,
    val title: String?,
    val description: String?,
    val url: String?,
    @SerializedName("urlToImage") val urlToImage: String?,
    @SerializedName("publishedAt") val publishedAt: String?,
    val content: String?
)
