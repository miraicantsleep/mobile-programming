package com.example.mvvmlogin.data.repository

import com.example.mvvmlogin.data.local.dao.UserDao
import com.example.mvvmlogin.data.local.entity.User

class UserRepository(private val userDao: UserDao) {

    suspend fun login(username: String, password: String): User? {
        return userDao.login(username, password)
    }

    suspend fun register(username: String, password: String, email: String): Result<Unit> {
        return try {
            val existing = userDao.getUserByUsername(username)
            if (existing != null) {
                Result.failure(Exception("Username '$username' sudah digunakan"))
            } else {
                userDao.insertUser(User(username = username, password = password, email = email))
                Result.success(Unit)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
