package com.example.marketplacessiswa.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.example.marketplacessiswa.data.Product
import com.example.marketplacessiswa.data.seedProducts

class MarketplaceViewModel : ViewModel() {
    val products = mutableStateListOf<Product>().apply { addAll(seedProducts) }

    fun addProduct(name: String, price: String, description: String) {
        products.add(0, Product(name = name, price = price, description = description))
    }
}
