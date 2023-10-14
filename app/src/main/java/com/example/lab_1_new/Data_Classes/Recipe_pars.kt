package com.example.lab_1_new.Data_Classes

import android.os.Parcel
import android.os.Parcelable
import java.io.Serializable

/**Data Class для запарсеных рецептов*/
data class Recipe_pars(val id: Int, val Calorie: String, val Time: Int, val Name: String, val Ingredients: String, val Difficulty: Int)
