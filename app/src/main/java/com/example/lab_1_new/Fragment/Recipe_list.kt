package com.example.lab_1_new.Fragment

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.bekawestberg.loopinglayout.library.LoopingLayoutManager
import com.example.lab_1_new.Adapters.Recipe
import com.example.lab_1_new.Adapters.Recipe_Adapter
import com.example.lab_1_new.R

class Recipe_list : Fragment(), Recipe_Adapter.Listener {

    lateinit var sharedPreferences: SharedPreferences
    lateinit var editor: SharedPreferences.Editor
    private lateinit var viewManager: RecyclerView.LayoutManager
    val adapter=Recipe_Adapter(this) //Адаптер для списка

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_recipe_list, container, false)
        /**Объявляем sharedPreferences для сохранения данных о выбраном рецепте*/
        sharedPreferences = requireContext().getSharedPreferences("NowRecipe", Context.MODE_PRIVATE)
        editor = sharedPreferences.edit()
        /**Цикличный слой для бесконечной крутилки(https://github.com/BeksOmega/looping-layout)*/
        viewManager = LoopingLayoutManager(
            requireContext(),
            LoopingLayoutManager.VERTICAL,
            false
        )
        /**Обьявляем разметку списка и его адаптер*/
        view.findViewById<RecyclerView>(R.id.RecyclerView).layoutManager= viewManager
        view.findViewById<RecyclerView>(R.id.RecyclerView).adapter=adapter
        /**Массивы из ресурсов для заполнения списка*/
        val arrayName = requireContext().resources.getStringArray(R.array.Recipes_Name)
        val arrayURL = requireContext().resources.getStringArray(R.array.Recipes_URL)
        /**Заполнения списка*/
        arrayName.forEach{
            val id = arrayName.indexOf(it)
            println("$id $it")
            adapter.RecipeCreate(Recipe(id, it, arrayURL[id]))
        }
        return view
    }

    /**Наследуемый от адаптера слушатель для обраюотки нажания - в данном случае переход на некст фрагмент*/
    override fun onClick(recipe: Recipe) {
        /**Сохраняем выбраный рецепт*/
        editor.putInt("NowRecipe", recipe.id)
        editor.apply()
        val fragmentManager = requireActivity().supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.BAZA,Recipe_now())
        fragmentTransaction.commit()
    }

}