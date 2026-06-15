package com.example.coffeebliss

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.coffeebliss.data.local.AppDatabase
import com.example.coffeebliss.data.repository.CoffeeRepository
import com.example.coffeebliss.ui.screens.LoginScreen
import com.example.coffeebliss.ui.screens.MainScreen
import com.example.coffeebliss.ui.screens.RegisterScreen
import com.example.coffeebliss.ui.screens.WelcomeScreen
import com.example.coffeebliss.viewmodel.CoffeeViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val db = AppDatabase.getInstance(this)
        val repository = CoffeeRepository(db.memberDao(), db.transaksiDao(), db.redemptionDao())
        val viewModel = ViewModelProvider(this, CoffeeViewModel.Factory(repository))[CoffeeViewModel::class.java]

        setContent {
            MaterialTheme {
                Surface {
                    val navController = rememberNavController()

                    NavHost(navController = navController, startDestination = "welcome") {
                        composable("welcome") {
                            WelcomeScreen(
                                onLogin = { navController.navigate("login") },
                                onRegister = { navController.navigate("register") }
                            )
                        }
                        composable("login") {
                            LoginScreen(
                                viewModel = viewModel,
                                onBack = { navController.popBackStack() },
                                onSuccess = {
                                    navController.navigate("main") {
                                        popUpTo("welcome") { inclusive = true }
                                    }
                                }
                            )
                        }
                        composable("register") {
                            RegisterScreen(
                                viewModel = viewModel,
                                onBack = { navController.popBackStack() },
                                onSuccess = {
                                    navController.navigate("main") {
                                        popUpTo("welcome") { inclusive = true }
                                    }
                                }
                            )
                        }
                        composable("main") {
                            MainScreen(
                                viewModel = viewModel,
                                onLogout = {
                                    viewModel.logout()
                                    navController.navigate("welcome") {
                                        popUpTo("main") { inclusive = true }
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}
