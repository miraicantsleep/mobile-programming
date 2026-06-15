package com.example.registrasiswa.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.registrasiswa.data.local.dao.SiswaDao
import com.example.registrasiswa.data.local.entity.Siswa

@Database(entities = [Siswa::class], version = 2, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun siswaDao(): SiswaDao

    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "registrasi_siswa_db"
                )
                .fallbackToDestructiveMigration()
                .build().also { INSTANCE = it }
            }
        }
    }
}
