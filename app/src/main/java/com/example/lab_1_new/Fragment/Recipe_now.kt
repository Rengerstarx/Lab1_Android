package com.example.lab_1_new.Fragment

import android.R.attr.defaultValue
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.lab_1_new.Data_Classes.Recipes
import com.example.lab_1_new.R


class Recipe_now : Fragment(){

    private val dataModel: Recipes by activityViewModels()
    var idd = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_recipe_now, container, false)
        val bundle = this.arguments
        if (bundle != null) {
            idd = bundle.getInt("id", defaultValue)
        }
        dataModel.recipes.value?.forEach {
            if(it.id==idd){
                view.findViewById<TextView>(R.id.Name).text=it.Name
                view.findViewById<TextView>(R.id.Calory).text="${it.Calorie} ккал"
                view.findViewById<TextView>(R.id.Difficalty).text=it.Difficulty.toString()
                view.findViewById<TextView>(R.id.Ingredients).text="Ингридиенты: ${it.Ingredients}"
                view.findViewById<TextView>(R.id.Time).text="${it.Time.toString()} минут"
            }
        }
        return view
    }


}