package com.example.coffeebliss.data.local.dao

import androidx.room.*
import com.example.coffeebliss.data.local.entity.Transaksi
import kotlinx.coroutines.flow.Flow

@Dao
interface TransaksiDao {
    @Insert
    suspend fun insertTransaksi(transaksi: Transaksi): Long

    @Query("SELECT * FROM transaksi WHERE memberId = :memberId ORDER BY tanggal DESC")
    fun getTransaksiByMember(memberId: Int): Flow<List<Transaksi>>
}
