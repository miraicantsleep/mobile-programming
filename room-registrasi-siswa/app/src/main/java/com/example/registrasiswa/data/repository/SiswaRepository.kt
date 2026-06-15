package com.example.registrasiswa.data.repository

import com.example.registrasiswa.data.local.dao.SiswaDao
import com.example.registrasiswa.data.local.entity.Siswa
import kotlinx.coroutines.flow.Flow

class SiswaRepository(private val dao: SiswaDao) {

    fun getAllSiswa(): Flow<List<Siswa>> = dao.getAllSiswa()

    fun searchSiswa(query: String): Flow<List<Siswa>> = dao.searchSiswa(query)

    fun getSiswaCount(): Flow<Int> = dao.getSiswaCount()

    suspend fun insertSiswa(siswa: Siswa): Long = dao.insertSiswa(siswa)

    suspend fun updateSiswa(siswa: Siswa) = dao.updateSiswa(siswa)

    suspend fun deleteSiswa(siswa: Siswa) = dao.deleteSiswa(siswa)

    suspend fun getSiswaById(id: Int): Siswa? = dao.getSiswaById(id)
}
