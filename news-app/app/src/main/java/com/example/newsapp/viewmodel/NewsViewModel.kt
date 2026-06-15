package com.example.newsapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newsapp.data.model.Article
import com.example.newsapp.data.repository.NewsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed interface NewsUiState {
    object Loading : NewsUiState
    data class Success(val articles: List<Article>) : NewsUiState
    data class Error(val message: String) : NewsUiState
}

class NewsViewModel : ViewModel() {
    private val repository = NewsRepository()

    private val _homeState = MutableStateFlow<NewsUiState>(NewsUiState.Loading)
    val homeState: StateFlow<NewsUiState> = _homeState.asStateFlow()

    private val _searchState = MutableStateFlow<NewsUiState?>(null)
    val searchState: StateFlow<NewsUiState?> = _searchState.asStateFlow()

    private val _selectedArticle = MutableStateFlow<Article?>(null)
    val selectedArticle: StateFlow<Article?> = _selectedArticle.asStateFlow()

    init {
        loadTopHeadlines()
    }

    fun loadTopHeadlines() {
        viewModelScope.launch(Dispatchers.IO) {
            _homeState.value = NewsUiState.Loading
            val result = repository.getTopHeadlines()
            _homeState.value = result.fold(
                onSuccess = { NewsUiState.Success(it) },
                onFailure = { NewsUiState.Error(it.message ?: "Unknown error") }
            )
        }
    }

    fun refresh() = loadTopHeadlines()

    fun search(query: String) {
        if (query.isBlank()) {
            _searchState.value = null
            return
        }
        viewModelScope.launch(Dispatchers.IO) {
            _searchState.value = NewsUiState.Loading
            val result = repository.searchNews(query)
            _searchState.value = result.fold(
                onSuccess = { NewsUiState.Success(it) },
                onFailure = { NewsUiState.Error(it.message ?: "Unknown error") }
            )
        }
    }

    fun selectArticle(article: Article) {
        _selectedArticle.value = article
    }

    fun clearSearch() {
        _searchState.value = null
    }
}
