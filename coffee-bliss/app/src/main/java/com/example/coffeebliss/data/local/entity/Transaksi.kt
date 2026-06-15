package com.example.coffeebliss.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "transaksi",
    foreignKeys = [ForeignKey(
        entity = Member::class,
        parentColumns = ["id"],
        childColumns = ["memberId"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class Transaksi(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val memberId: Int,
    val jumlahBelanja: Long,
    val poinDapat: Int,
    val keterangan: String,
    val tanggal: Long = System.currentTimeMillis()
)
