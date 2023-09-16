package com.example.lab_1_new.Fragment

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.lab_1_new.R

class Recipe_now : Fragment() {

    lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_recipe_now, container, false)
        /**Получаем id выбраного рецепта(по дефолту будет борщ(первый рецепт)) и заносим его в TextView*/
        sharedPreferences = requireContext().getSharedPreferences("NowRecipe", Context.MODE_PRIVATE)
        val array = requireContext().resources.getStringArray(R.array.Recipes_Name)
        view.findViewById<TextView>(R.id.TextRecipe).text = array[sharedPreferences.getInt("NowRecipe", 1)]
        return view
    }

}