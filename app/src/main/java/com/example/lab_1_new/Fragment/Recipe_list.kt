package com.example.lab_1_new.Fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.lab_1_new.Adapters.Recipe
import com.example.lab_1_new.Adapters.Recipe_Adapter
import com.example.lab_1_new.R

class Recipe_list : Fragment(), Recipe_Adapter.Listener {

    val adapter=Recipe_Adapter(this)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_recipe_list, container, false)
        view.findViewById<RecyclerView>(R.id.RecyclerView).layoutManager= LinearLayoutManager(requireContext())
        view.findViewById<RecyclerView>(R.id.RecyclerView).adapter=adapter
        val arrayName = requireContext().resources.getStringArray(R.array.Recipes_Name)
        Log.e("aboba",arrayName[0])
        val arrayURL = requireContext().resources.getStringArray(R.array.Recipes_URL)
        arrayName.forEach{
            val id = arrayName.indexOf(it)
            println("$id $it")
            adapter.RecipeCreate(Recipe(id, it, arrayURL[id]))
        }
        return view
    }

    override fun onClick(recipe: Recipe) {

    }

}