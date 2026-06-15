package com.example.registrasiswa.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.registrasiswa.data.local.entity.Siswa
import com.example.registrasiswa.data.repository.SiswaRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class SiswaUiState(
    val siswaList: List<Siswa> = emptyList(),
    val searchQuery: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val successMessage: String? = null,
    val totalSiswa: Int = 0
)

@OptIn(ExperimentalCoroutinesApi::class)
class SiswaViewModel(private val repository: SiswaRepository) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    private val _uiMessages = MutableStateFlow<Pair<String?, String?>>(Pair(null, null))
    private val _isLoading = MutableStateFlow(false)

    val siswaList: StateFlow<List<Siswa>> = _searchQuery
        .flatMapLatest { query ->
            if (query.isBlank()) repository.getAllSiswa()
            else repository.searchSiswa(query)
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val totalSiswa: StateFlow<Int> = repository.getSiswaCount()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val uiState: StateFlow<SiswaUiState> = combine(
        siswaList, _searchQuery, _isLoading, _uiMessages, totalSiswa
    ) { list, query, loading, messages, total ->
        SiswaUiState(
            siswaList = list,
            searchQuery = query,
            isLoading = loading,
            errorMessage = messages.first,
            successMessage = messages.second,
            totalSiswa = total
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), SiswaUiState())

    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
    }

    fun tambahSiswa(nama: String, email: String) {
        if (nama.isBlank()) {
            _uiMessages.value = Pair("Nama tidak boleh kosong", null)
            return
        }
        viewModelScope.launch {
            _isLoading.value = true
            try {
                repository.insertSiswa(Siswa(nama = nama.trim(), email = email.trim()))
                _uiMessages.value = Pair(null, "${nama.trim()} berhasil ditambahkan")
            } catch (e: Exception) {
                _uiMessages.value = Pair("Gagal menambahkan: ${e.message}", null)
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun updateSiswa(siswa: Siswa) {
        viewModelScope.launch {
            try {
                repository.updateSiswa(siswa)
                _uiMessages.value = Pair(null, "Data ${siswa.nama} berhasil diperbarui")
            } catch (e: Exception) {
                _uiMessages.value = Pair("Gagal memperbarui: ${e.message}", null)
            }
        }
    }

    fun hapusSiswa(siswa: Siswa) {
        viewModelScope.launch {
            try {
                repository.deleteSiswa(siswa)
                _uiMessages.value = Pair(null, "${siswa.nama} berhasil dihapus")
            } catch (e: Exception) {
                _uiMessages.value = Pair("Gagal menghapus: ${e.message}", null)
            }
        }
    }

    fun clearMessages() { _uiMessages.value = Pair(null, null) }

    class Factory(private val repository: SiswaRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            @Suppress("UNCHECKED_CAST")
            return SiswaViewModel(repository) as T
        }
    }
}
