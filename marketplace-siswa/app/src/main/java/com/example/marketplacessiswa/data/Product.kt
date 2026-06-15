package com.example.marketplacessiswa.data

data class Product(
    val id: Int,
    val title: String,
    val price: Int,
    val description: String,
    val category: String,
    val seller: String,
    val condition: String,
    val emoji: String
)

val sampleProducts = listOf(
    Product(1, "Kalkulator Casio FX-991", 150000, "Kalkulator scientific masih mulus, lengkap dengan buku panduan.", "Alat Tulis", "Budi S.", "Bekas - Sangat Baik", "🧮"),
    Product(2, "Buku Kalkulus Stewart Ed.8", 120000, "Buku kalkulus edisi 8, sedikit coretan pensil, mudah dihapus.", "Buku", "Rina A.", "Bekas - Baik", "📚"),
    Product(3, "Laptop Stand Adjustable", 85000, "Stand laptop aluminium, bisa diatur ketinggiannya, ringan.", "Elektronik", "Dika P.", "Bekas - Baik", "💻"),
    Product(4, "Seragam Praktikum Lab (M)", 65000, "Jas lab ukuran M, sudah dicuci bersih.", "Pakaian", "Sari W.", "Bekas - Baik", "🥼"),
    Product(5, "Headset JBL Tune 510BT", 200000, "Headset Bluetooth, baterai tahan lama, audio jernih.", "Elektronik", "Fajar M.", "Bekas - Sangat Baik", "🎧"),
    Product(6, "Tas Ransel Laptop 15 inch", 180000, "Tas kuliah waterproof, slot laptop 15 inch, banyak kantong.", "Aksesori", "Citra N.", "Bekas - Baik", "🎒"),
    Product(7, "Mouse Wireless Logitech M280", 95000, "Mouse wireless, masih berfungsi sempurna, baterai baru.", "Elektronik", "Andi K.", "Bekas - Sangat Baik", "🖱️"),
    Product(8, "Buku Pemrograman Kotlin", 75000, "Buku pemrograman Kotlin untuk pemula, lengkap dan terstruktur.", "Buku", "Mega L.", "Bekas - Baik", "📖"),
)
