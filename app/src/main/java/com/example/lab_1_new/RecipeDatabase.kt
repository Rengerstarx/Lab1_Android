package com.example.lab_1_new

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.lab_1_new.Data_Classes.Recipe_pars
import com.example.lab_1_new.Data_Classes.Users
import com.example.lab_1_new.Interfaces.Dao
import com.example.lab_1_new.Interfaces.DaoUsers

@Database(entities = [Recipe_pars::class], version = 1)
abstract class RecipeDatabase : RoomDatabase() {
    abstract fun getDao(): Dao

    companion object{
        fun getDatabase(context: Context): RecipeDatabase {
            return Room.databaseBuilder(
                context.applicationContext,
                RecipeDatabase::class.java,
                "test.db"
            ).build()
        }
    }
}