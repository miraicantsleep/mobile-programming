package com.example.coffeebliss.data.local.dao

import androidx.room.*
import com.example.coffeebliss.data.local.entity.Redemption
import kotlinx.coroutines.flow.Flow

@Dao
interface RedemptionDao {
    @Insert
    suspend fun insertRedemption(redemption: Redemption): Long

    @Query("SELECT * FROM redemptions WHERE memberId = :memberId ORDER BY tanggal DESC")
    fun getRedemptionsByMember(memberId: Int): Flow<List<Redemption>>
}
