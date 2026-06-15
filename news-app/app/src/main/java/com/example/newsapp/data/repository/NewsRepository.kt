package com.example.newsapp.data.repository

import com.example.newsapp.data.api.RetrofitClient
import com.example.newsapp.data.model.Article

class NewsRepository {
    private val api = RetrofitClient.apiService
    private val apiKey = RetrofitClient.API_KEY

    suspend fun getTopHeadlines(country: String = "us"): Result<List<Article>> {
        return try {
            val response = api.getTopHeadlines(country = country, apiKey = apiKey)
            if (response.status == "ok") {
                Result.success(response.articles)
            } else {
                Result.failure(Exception("API error: ${response.status}"))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Failed to load news: ${e.message}"))
        }
    }

    suspend fun searchNews(query: String): Result<List<Article>> {
        return try {
            val response = api.searchNews(query = query, apiKey = apiKey)
            if (response.status == "ok") {
                Result.success(response.articles)
            } else {
                Result.failure(Exception("API error: ${response.status}"))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Failed to search news: ${e.message}"))
        }
    }
}
