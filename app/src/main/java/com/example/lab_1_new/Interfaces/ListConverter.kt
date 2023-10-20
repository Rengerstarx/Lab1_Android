package com.example.lab_1_new.Interfaces
import androidx.room.TypeConverter
import com.example.lab_1_new.Data_Classes.Recipe_pars
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class ListConverter {
    private val gson = Gson()

    @TypeConverter
    fun fromList(list: List<Recipe_pars>): String {
        return gson.toJson(list)
    }

    @TypeConverter
    fun toList(json: String): List<Recipe_pars> {
        val type = object : TypeToken<List<Recipe_pars>>() {}.type
        return gson.fromJson(json, type)
    }
}
