package com.example.lab_1_new.Data_Classes

import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity (tableName = "Recipes")
/**Data Class для запарсеных рецептов*/
data class Recipe_pars(
    @PrimaryKey(autoGenerate = true) val id: Int? = null,
    @ColumnInfo(name = "Calorie") val Calorie: String,
    @ColumnInfo(name = "Time") val Time: Int,
    @ColumnInfo(name = "Name") val Name: String,
    @ColumnInfo(name = "Ingredients") val Ingredients: String,
    @ColumnInfo(name = "Difficulty") val Difficulty: Int)
