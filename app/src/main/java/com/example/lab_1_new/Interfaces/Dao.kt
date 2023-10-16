package com.example.lab_1_new.Interfaces

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.lab_1_new.Data_Classes.Recipe_pars

@Dao
interface Dao {
    @Insert
    fun insertItem(newRecipe: Recipe_pars)
    @Query("SELECT * FROM Recipes")
    fun getAllRecipes():List<Recipe_pars>
    @Query("SELECT COUNT(*) FROM Recipes")
    fun getCountRecipes(): Int
    @Query("SELECT * FROM Recipes WHERE id = :id")
    fun getRecipeById(id: Int): Recipe_pars
}