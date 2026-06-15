package com.example.marketplacessiswa.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.marketplacessiswa.data.Product

// FR-01: Menampilkan daftar produk dalam Card dengan nama, harga, dan deskripsi singkat.
@Composable
fun HomeScreen(products: List<Product>) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text("Halo, Siswa!", fontSize = 24.sp, fontWeight = FontWeight.ExtraBold)
            Text(
                "Mau belanja apa hari ini?",
                color = Color.Gray,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }
        items(products) { product ->
            ProductCard(product)
        }
    }
}

@Composable
private fun ProductCard(product: Product) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(product.name, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Text(
                    "Rp ${product.price}",
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Black
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(product.description, color = Color.DarkGray, fontSize = 14.sp)
        }
    }
}
