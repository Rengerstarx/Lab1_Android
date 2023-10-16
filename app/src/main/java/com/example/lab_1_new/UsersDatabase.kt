package com.example.lab_1_new

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.lab_1_new.Data_Classes.Recipe_pars
import com.example.lab_1_new.Data_Classes.Users
import com.example.lab_1_new.Interfaces.Dao
import com.example.lab_1_new.Interfaces.DaoUsers

@Database(entities = [Users::class], version = 1)
abstract class UsersDatabase : RoomDatabase() {
    abstract fun getDaoUsers(): DaoUsers

    companion object{
        fun getDatabase(context: Context): UsersDatabase {
            return Room.databaseBuilder(
                context.applicationContext,
                UsersDatabase::class.java,
                "test2.db"
            ).build()
        }
    }
}