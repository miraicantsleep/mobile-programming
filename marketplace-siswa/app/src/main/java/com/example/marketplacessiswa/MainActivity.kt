package com.example.marketplacessiswa

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.marketplacessiswa.ui.screens.MainScreen
import com.example.marketplacessiswa.ui.theme.MarketplaceSiswaTheme
import com.example.marketplacessiswa.viewmodel.MarketplaceViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MarketplaceSiswaTheme {
                val viewModel: MarketplaceViewModel = viewModel()
                MainScreen(viewModel = viewModel)
            }
        }
    }
}
