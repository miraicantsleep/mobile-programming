package com.example.newsapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.newsapp.ui.screens.DetailScreen
import com.example.newsapp.ui.screens.HomeScreen
import com.example.newsapp.ui.screens.SearchScreen
import com.example.newsapp.viewmodel.NewsViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                val navController = rememberNavController()
                val viewModel: NewsViewModel = viewModel()

                NavHost(
                    navController = navController,
                    startDestination = "home"
                ) {
                    composable("home") {
                        HomeScreen(
                            viewModel = viewModel,
                            onArticleClick = { navController.navigate("detail") },
                            onSearchClick = { navController.navigate("search") }
                        )
                    }
                    composable("search") {
                        SearchScreen(
                            viewModel = viewModel,
                            onArticleClick = { navController.navigate("detail") },
                            onBack = { navController.popBackStack() }
                        )
                    }
                    composable("detail") {
                        val article = viewModel.selectedArticle.value
                        if (article != null) {
                            DetailScreen(
                                article = article,
                                onBack = { navController.popBackStack() }
                            )
                        }
                    }
                }
            }
        }
    }
}
