package com.example.mvvmlogin

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
import com.example.mvvmlogin.data.local.AppDatabase
import com.example.mvvmlogin.data.repository.UserRepository
import com.example.mvvmlogin.ui.screens.DashboardScreen
import com.example.mvvmlogin.ui.screens.LoginScreen
import com.example.mvvmlogin.ui.screens.RegisterScreen
import com.example.mvvmlogin.viewmodel.LoginViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val db = AppDatabase.getInstance(this)
        val repository = UserRepository(db.userDao())
        val viewModel = ViewModelProvider(this, LoginViewModel.Factory(repository))[LoginViewModel::class.java]

        setContent {
            MaterialTheme {
                Surface {
                    val navController = rememberNavController()

                    NavHost(navController = navController, startDestination = "login") {
                        composable("login") {
                            LoginScreen(
                                viewModel = viewModel,
                                onLoginSuccess = { username ->
                                    navController.navigate("dashboard/$username") {
                                        popUpTo("login") { inclusive = true }
                                    }
                                },
                                onNavigateToRegister = { navController.navigate("register") }
                            )
                        }
                        composable("register") {
                            RegisterScreen(
                                viewModel = viewModel,
                                onBack = { navController.popBackStack() },
                                onRegisterSuccess = { navController.popBackStack() }
                            )
                        }
                        composable("dashboard/{username}") { backStack ->
                            val username = backStack.arguments?.getString("username") ?: "User"
                            DashboardScreen(username = username) {
                                navController.navigate("login") {
                                    popUpTo("dashboard/$username") { inclusive = true }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
