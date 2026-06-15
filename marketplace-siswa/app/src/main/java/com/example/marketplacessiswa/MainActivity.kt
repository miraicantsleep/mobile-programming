package com.example.marketplacessiswa

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.marketplacessiswa.data.sampleProducts
import com.example.marketplacessiswa.ui.screens.HomeScreen
import com.example.marketplacessiswa.ui.screens.ProductDetailScreen
import com.example.marketplacessiswa.ui.theme.MarketplaceSiswaTheme
import com.example.marketplacessiswa.viewmodel.MarketplaceViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MarketplaceSiswaTheme {
                val navController = rememberNavController()
                val viewModel: MarketplaceViewModel = viewModel()

                NavHost(navController = navController, startDestination = "home") {
                    composable("home") {
                        HomeScreen(
                            viewModel = viewModel,
                            onProductClick = { product ->
                                navController.navigate("detail/${product.id}")
                            }
                        )
                    }
                    composable("detail/{productId}") { backStack ->
                        val productId = backStack.arguments?.getString("productId")?.toIntOrNull()
                        val product = sampleProducts.find { it.id == productId }
                        if (product != null) {
                            ProductDetailScreen(product = product, onBack = { navController.popBackStack() })
                        }
                    }
                }
            }
        }
    }
}
