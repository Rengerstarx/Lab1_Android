package com.example.lab_1_new.Data_Classes

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Users")
/**Data Class для пользователей*/
data class Users(
    @PrimaryKey(autoGenerate = false) val HashId: String,
    @ColumnInfo(name = "Name") val Name: String,
    @ColumnInfo(name = "Login") val Login: String,
    @ColumnInfo(name = "Password") val Password: String,
    @ColumnInfo(name = "FavoriteRecipes") val FavoriteRecipes: MutableList<Recipe_pars>)
