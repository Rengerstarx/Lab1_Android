package com.example.lab_1_new.Fragment

import android.R.attr.defaultValue
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.lab_1_new.Data_Classes.Recipe_pars
import com.example.lab_1_new.R
import com.example.lab_1_new.RecipeDatabase
import kotlin.concurrent.thread


class Recipe_now : Fragment(){

    lateinit var db: RecipeDatabase
    var idd = 0
    lateinit var NowRecipe: Recipe_pars

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_recipe_now, container, false)
        db = RecipeDatabase.getDatabase(requireContext())
        val bundle = this.arguments
        if (bundle != null) {
            idd = bundle.getInt("id", defaultValue)
            println(idd)
        }
        thread{
            NowRecipe = db.getDao().getRecipeById(idd)
            requireActivity().runOnUiThread {
                view.findViewById<TextView>(R.id.Name).text = NowRecipe.Name
                view.findViewById<TextView>(R.id.Calory).text = "${NowRecipe.Calorie} ккал"
                view.findViewById<TextView>(R.id.Difficalty).text = NowRecipe.Difficulty.toString()
                view.findViewById<TextView>(R.id.Ingredients).text = "Ингридиенты: ${NowRecipe.Ingredients}"
                view.findViewById<TextView>(R.id.Time).text = "${NowRecipe.Time.toString()} минут"
            }
        }
        return view
    }


}