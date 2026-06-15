package com.example.coffeebliss.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "members")
data class Member(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val nama: String,
    val email: String,
    val noHp: String,
    val nomorMember: String,
    val totalPoin: Int = 0,
    val status: String = "Silver",
    val createdAt: Long = System.currentTimeMillis()
)
