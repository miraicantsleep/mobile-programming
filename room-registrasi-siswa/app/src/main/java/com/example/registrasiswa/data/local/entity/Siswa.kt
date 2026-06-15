package com.example.registrasiswa.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "siswa")
data class Siswa(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val nama: String,
    val email: String,
    val createdAt: Long = System.currentTimeMillis()
)
