package com.example.registrasiswa.data.local.dao

import androidx.room.*
import com.example.registrasiswa.data.local.entity.Siswa
import kotlinx.coroutines.flow.Flow

@Dao
interface SiswaDao {
    @Query("SELECT * FROM siswa ORDER BY createdAt DESC")
    fun getAllSiswa(): Flow<List<Siswa>>

    @Query("SELECT * FROM siswa WHERE id = :id")
    suspend fun getSiswaById(id: Int): Siswa?

    @Query("SELECT * FROM siswa WHERE nama LIKE '%' || :query || '%' OR email LIKE '%' || :query || '%'")
    fun searchSiswa(query: String): Flow<List<Siswa>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSiswa(siswa: Siswa): Long

    @Update
    suspend fun updateSiswa(siswa: Siswa)

    @Delete
    suspend fun deleteSiswa(siswa: Siswa)

    @Query("SELECT COUNT(*) FROM siswa")
    fun getSiswaCount(): Flow<Int>
}
