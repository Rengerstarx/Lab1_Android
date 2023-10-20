package com.example.lab_1_new.Interfaces

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.lab_1_new.Data_Classes.Recipe_pars
import com.example.lab_1_new.Data_Classes.Users

@Dao
interface DaoUsers {
    @Insert
    fun insertItem(newUser: Users)
    @Query("SELECT * FROM Users WHERE Login = :Login")
    fun getUserByLogin(Login: String): Users
    @Query("SELECT COUNT(*) FROM Users WHERE Login = :Login")
    fun checkUserExists(Login: String): Int
    @Query("SELECT * FROM Users WHERE Login = :Login")
    fun getAllFavorite(Login: String):Users
    @Query("SELECT * FROM Users WHERE HashId = :HashId")
    fun getUserByHash(HashId: String): Users
    @Delete
    suspend fun deleteObject(User: Users)
}