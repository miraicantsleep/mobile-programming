package com.example.coffeebliss.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "redemptions",
    foreignKeys = [ForeignKey(
        entity = Member::class,
        parentColumns = ["id"],
        childColumns = ["memberId"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class Redemption(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val memberId: Int,
    val reward: String,
    val poinDipakai: Int,
    val tanggal: Long = System.currentTimeMillis()
)
