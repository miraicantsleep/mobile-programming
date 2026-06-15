package com.example.coffeebliss.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.coffeebliss.data.local.entity.Member
import com.example.coffeebliss.data.local.entity.Redemption
import com.example.coffeebliss.data.local.entity.Transaksi
import com.example.coffeebliss.data.repository.CoffeeRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class CoffeeUiState(
    val currentMember: Member? = null,
    val transaksiList: List<Transaksi> = emptyList(),
    val redemptionList: List<Redemption> = emptyList(),
    val isLoading: Boolean = false,
    val successMessage: String? = null,
    val errorMessage: String? = null
)

class CoffeeViewModel(private val repository: CoffeeRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(CoffeeUiState())
    val uiState: StateFlow<CoffeeUiState> = _uiState.asStateFlow()

    private val _loggedInMemberId = MutableStateFlow<Int?>(null)

    init {
        _loggedInMemberId
            .filterNotNull()
            .flatMapLatest { memberId ->
                combine(
                    repository.getMemberById(memberId),
                    repository.getTransaksi(memberId),
                    repository.getRedemptions(memberId)
                ) { member, transaksi, redemptions ->
                    Triple(member, transaksi, redemptions)
                }
            }
            .onEach { (member, transaksi, redemptions) ->
                _uiState.update { it.copy(currentMember = member, transaksiList = transaksi, redemptionList = redemptions) }
            }
            .launchIn(viewModelScope)
    }

    fun register(nama: String, email: String, noHp: String) {
        if (nama.isBlank() || email.isBlank() || noHp.isBlank()) {
            _uiState.update { it.copy(errorMessage = "Semua field wajib diisi") }
            return
        }
        if (!email.contains("@")) {
            _uiState.update { it.copy(errorMessage = "Format email tidak valid") }
            return
        }
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val result = repository.registerMember(nama.trim(), email.trim(), noHp.trim())
            if (result.isSuccess) {
                val member = result.getOrThrow()
                _loggedInMemberId.value = member.id
                _uiState.update { it.copy(isLoading = false, successMessage = "Selamat datang, ${member.nama}! Nomor member Anda: ${member.nomorMember}") }
            } else {
                _uiState.update { it.copy(isLoading = false, errorMessage = result.exceptionOrNull()?.message) }
            }
        }
    }

    fun login(email: String) {
        if (email.isBlank()) {
            _uiState.update { it.copy(errorMessage = "Email tidak boleh kosong") }
            return
        }
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val member = repository.login(email.trim())
            if (member != null) {
                _loggedInMemberId.value = member.id
                _uiState.update { it.copy(isLoading = false, successMessage = "Selamat datang kembali, ${member.nama}!") }
            } else {
                _uiState.update { it.copy(isLoading = false, errorMessage = "Email tidak terdaftar. Silakan daftar terlebih dahulu.") }
            }
        }
    }

    fun tambahTransaksi(jumlahBelanja: Long, keterangan: String) {
        val member = _uiState.value.currentMember ?: return
        if (jumlahBelanja <= 0) {
            _uiState.update { it.copy(errorMessage = "Jumlah belanja harus lebih dari 0") }
            return
        }
        viewModelScope.launch {
            val result = repository.addTransaksiAndUpdatePoin(member, jumlahBelanja, keterangan.ifBlank { "Pembelian Kopi" })
            if (result.isSuccess) {
                val poin = result.getOrThrow()
                _uiState.update { it.copy(successMessage = "Transaksi berhasil! Anda mendapat $poin poin") }
            } else {
                _uiState.update { it.copy(errorMessage = result.exceptionOrNull()?.message) }
            }
        }
    }

    fun redeemReward(reward: String, poinDibutuhkan: Int) {
        val member = _uiState.value.currentMember ?: return
        viewModelScope.launch {
            val result = repository.redeemPoin(member, reward, poinDibutuhkan)
            if (result.isSuccess) {
                _uiState.update { it.copy(successMessage = "Berhasil menukar poin dengan $reward!") }
            } else {
                _uiState.update { it.copy(errorMessage = result.exceptionOrNull()?.message) }
            }
        }
    }

    fun logout() {
        _loggedInMemberId.value = null
        _uiState.value = CoffeeUiState()
    }

    fun clearMessages() {
        _uiState.update { it.copy(successMessage = null, errorMessage = null) }
    }

    class Factory(private val repository: CoffeeRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            @Suppress("UNCHECKED_CAST")
            return CoffeeViewModel(repository) as T
        }
    }
}
