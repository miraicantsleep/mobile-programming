package com.example.registrasiswa.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.registrasiswa.data.local.entity.Siswa
import com.example.registrasiswa.viewmodel.SiswaViewModel

private fun getInitials(nama: String): String {
    val words = nama.trim().split("\\s+".toRegex()).filter { it.isNotBlank() }
    return when {
        words.size >= 2 -> "${words[0].first().uppercaseChar()}${words[1].first().uppercaseChar()}"
        words.size == 1 && words[0].length >= 2 -> words[0].take(2).uppercase()
        words.size == 1 -> words[0].take(1).uppercase()
        else -> "?"
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormSiswaScreen(
    viewModel: SiswaViewModel,
    siswaToEdit: Siswa? = null,
    onBack: () -> Unit
) {
    val isEdit = siswaToEdit != null
    var nama by remember { mutableStateOf(siswaToEdit?.nama ?: "") }
    var email by remember { mutableStateOf(siswaToEdit?.email ?: "") }
    var namaError by remember { mutableStateOf("") }

    val previewInitials = if (nama.isNotBlank()) getInitials(nama) else "?"
    val previewColor = Color(0xFF45B7D1)

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        if (isEdit) "Edit Data Siswa" else "Tambah Siswa Baru",
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Kembali")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            // Avatar preview header
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.primary)
                    .padding(bottom = 28.dp, top = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Box(
                        modifier = Modifier
                            .size(80.dp)
                            .clip(CircleShape)
                            .background(
                                if (nama.isNotBlank()) previewColor
                                else MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.25f)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = previewInitials,
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                    Spacer(Modifier.height(10.dp))
                    Text(
                        text = nama.ifBlank { "Nama Siswa" },
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                    Text(
                        text = email.ifBlank { "email@siswa.com" },
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.75f)
                    )
                }
            }

            // Form fields
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                OutlinedTextField(
                    value = nama,
                    onValueChange = { nama = it; namaError = "" },
                    label = { Text("Nama Lengkap *") },
                    placeholder = { Text("Contoh: Ahmad Zaki") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true,
                    isError = namaError.isNotBlank(),
                    supportingText = if (namaError.isNotBlank()) {
                        { Text(namaError, color = MaterialTheme.colorScheme.error) }
                    } else null
                )

                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email") },
                    placeholder = { Text("Contoh: siswa@email.com") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
                )

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = {
                        if (nama.isBlank()) {
                            namaError = "Nama tidak boleh kosong"
                        } else {
                            if (isEdit) {
                                viewModel.updateSiswa(
                                    siswaToEdit!!.copy(nama = nama.trim(), email = email.trim())
                                )
                            } else {
                                viewModel.tambahSiswa(nama, email)
                            }
                            onBack()
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        if (isEdit) "Simpan Perubahan" else "Tambah Siswa",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }

                TextButton(
                    onClick = onBack,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Batal")
                }
            }
        }
    }
}
