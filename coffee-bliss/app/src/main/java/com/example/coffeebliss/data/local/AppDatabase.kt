package com.example.coffeebliss.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.coffeebliss.data.local.dao.MemberDao
import com.example.coffeebliss.data.local.dao.RedemptionDao
import com.example.coffeebliss.data.local.dao.TransaksiDao
import com.example.coffeebliss.data.local.entity.Member
import com.example.coffeebliss.data.local.entity.Redemption
import com.example.coffeebliss.data.local.entity.Transaksi

@Database(
    entities = [Member::class, Transaksi::class, Redemption::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun memberDao(): MemberDao
    abstract fun transaksiDao(): TransaksiDao
    abstract fun redemptionDao(): RedemptionDao

    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "coffee_bliss_db"
                ).build().also { INSTANCE = it }
            }
        }
    }
}
