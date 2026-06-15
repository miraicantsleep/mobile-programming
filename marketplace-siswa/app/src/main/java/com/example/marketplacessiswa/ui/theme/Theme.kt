package com.example.marketplacessiswa.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColors = lightColorScheme(
    primary = Color(0xFF6200EE),
    secondary = Color(0xFF03DAC6),
    onPrimary = Color.White,
    primaryContainer = Color(0xFFEADDFF),
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
)

@Composable
fun MarketplaceSiswaTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = LightColors,
        content = content
    )
}
