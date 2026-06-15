package com.example.mvvmlogin.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.mvvmlogin.data.local.entity.User
import com.example.mvvmlogin.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class LoginUiState {
    object Idle : LoginUiState()
    object Loading : LoginUiState()
    data class Success(val user: User) : LoginUiState()
    data class Error(val message: String) : LoginUiState()
}

sealed class RegisterUiState {
    object Idle : RegisterUiState()
    object Loading : RegisterUiState()
    object Success : RegisterUiState()
    data class Error(val message: String) : RegisterUiState()
}

class LoginViewModel(private val repository: UserRepository) : ViewModel() {

    private val _loginState = MutableStateFlow<LoginUiState>(LoginUiState.Idle)
    val loginState: StateFlow<LoginUiState> = _loginState.asStateFlow()

    private val _registerState = MutableStateFlow<RegisterUiState>(RegisterUiState.Idle)
    val registerState: StateFlow<RegisterUiState> = _registerState.asStateFlow()

    fun login(username: String, password: String) {
        if (username.isBlank() || password.isBlank()) {
            _loginState.value = LoginUiState.Error("Username dan password tidak boleh kosong")
            return
        }
        viewModelScope.launch {
            _loginState.value = LoginUiState.Loading
            val user = repository.login(username.trim(), password.trim())
            _loginState.value = if (user != null) {
                LoginUiState.Success(user)
            } else {
                LoginUiState.Error("Username atau password salah")
            }
        }
    }

    fun register(username: String, password: String, confirmPassword: String, email: String) {
        when {
            username.isBlank() || password.isBlank() || email.isBlank() ->
                _registerState.value = RegisterUiState.Error("Semua field wajib diisi")
            password.length < 6 ->
                _registerState.value = RegisterUiState.Error("Password minimal 6 karakter")
            password != confirmPassword ->
                _registerState.value = RegisterUiState.Error("Password dan konfirmasi password tidak cocok")
            !email.contains("@") ->
                _registerState.value = RegisterUiState.Error("Format email tidak valid")
            else -> {
                viewModelScope.launch {
                    _registerState.value = RegisterUiState.Loading
                    val result = repository.register(username.trim(), password.trim(), email.trim())
                    _registerState.value = if (result.isSuccess) {
                        RegisterUiState.Success
                    } else {
                        RegisterUiState.Error(result.exceptionOrNull()?.message ?: "Registrasi gagal")
                    }
                }
            }
        }
    }

    fun resetLoginState() { _loginState.value = LoginUiState.Idle }
    fun resetRegisterState() { _registerState.value = RegisterUiState.Idle }

    class Factory(private val repository: UserRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            @Suppress("UNCHECKED_CAST")
            return LoginViewModel(repository) as T
        }
    }
}
