package com.example.registrasiswa

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModelProvider
import com.example.registrasiswa.data.local.AppDatabase
import com.example.registrasiswa.data.local.entity.Siswa
import com.example.registrasiswa.data.repository.SiswaRepository
import com.example.registrasiswa.ui.screens.FormSiswaScreen
import com.example.registrasiswa.ui.screens.SiswaListScreen
import com.example.registrasiswa.viewmodel.SiswaViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val db = AppDatabase.getInstance(this)
        val repository = SiswaRepository(db.siswaDao())
        val viewModel = ViewModelProvider(this, SiswaViewModel.Factory(repository))[SiswaViewModel::class.java]

        setContent {
            MaterialTheme {
                Surface {
                    var currentScreen by remember { mutableStateOf("list") }
                    var siswaToEdit by remember { mutableStateOf<Siswa?>(null) }

                    when (currentScreen) {
                        "list" -> SiswaListScreen(
                            viewModel = viewModel,
                            onAddClick = {
                                siswaToEdit = null
                                currentScreen = "form"
                            },
                            onEditClick = { siswa ->
                                siswaToEdit = siswa
                                currentScreen = "form"
                            }
                        )
                        "form" -> FormSiswaScreen(
                            viewModel = viewModel,
                            siswaToEdit = siswaToEdit,
                            onBack = { currentScreen = "list" }
                        )
                    }
                }
            }
        }
    }
}
