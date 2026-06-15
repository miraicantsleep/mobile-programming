package com.example.marketplacessiswa.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// FR-03: Tab Profile pada Bottom Navigation.
@Composable
fun ProfileScreen() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.AccountCircle,
            contentDescription = null,
            modifier = Modifier.size(100.dp),
            tint = Color.LightGray
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text("John Siswa", fontSize = 20.sp, fontWeight = FontWeight.Bold)
        Text("XII RPL 1", color = Color.Gray)
    }
}
