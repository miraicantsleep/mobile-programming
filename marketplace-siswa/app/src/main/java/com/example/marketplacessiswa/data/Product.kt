package com.example.marketplacessiswa.data

data class Product(
    val id: Long = System.currentTimeMillis(),
    val name: String,
    val price: String,
    val description: String
)

val seedProducts = listOf(
    Product(id = 1L, name = "Brownies Lumer", price = "15000", description = "Cokelat melimpah, cocok untuk camilan sore."),
    Product(id = 2L, name = "Kaos Custom", price = "85000", description = "Bahan adem, bisa request desain sendiri.")
)
