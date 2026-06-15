package com.example.marketplacessiswa.viewmodel

import androidx.lifecycle.ViewModel
import com.example.marketplacessiswa.data.Product
import com.example.marketplacessiswa.data.sampleProducts
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class MarketplaceUiState(
    val products: List<Product> = sampleProducts,
    val searchQuery: String = "",
    val selectedCategory: String = "Semua",
    val filteredProducts: List<Product> = sampleProducts
)

class MarketplaceViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(MarketplaceUiState())
    val uiState: StateFlow<MarketplaceUiState> = _uiState.asStateFlow()

    val categories = listOf("Semua", "Elektronik", "Buku", "Alat Tulis", "Pakaian", "Aksesori")

    fun onSearchQueryChange(query: String) {
        _uiState.value = _uiState.value.copy(searchQuery = query)
        applyFilter()
    }

    fun onCategorySelected(category: String) {
        _uiState.value = _uiState.value.copy(selectedCategory = category)
        applyFilter()
    }

    private fun applyFilter() {
        val state = _uiState.value
        val filtered = sampleProducts.filter { product ->
            val matchesSearch = product.title.contains(state.searchQuery, ignoreCase = true) ||
                    product.description.contains(state.searchQuery, ignoreCase = true)
            val matchesCategory = state.selectedCategory == "Semua" || product.category == state.selectedCategory
            matchesSearch && matchesCategory
        }
        _uiState.value = state.copy(filteredProducts = filtered)
    }
}
