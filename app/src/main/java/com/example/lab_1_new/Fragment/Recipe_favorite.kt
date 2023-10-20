package com.example.lab_1_new.Fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.afollestad.materialdialogs.MaterialDialog
import com.example.lab_1_new.Adapters.Recipe_Adapter
import com.example.lab_1_new.Data_Classes.Recipe_pars
import com.example.lab_1_new.R
import com.example.lab_1_new.Databases.UsersDatabase
import kotlinx.coroutines.runBlocking
import kotlin.concurrent.thread

class Recipe_favorite : androidx.fragment.app.Fragment(), Recipe_Adapter.Listener  {

    lateinit var database: UsersDatabase
    var Recipe_list: MutableList<Recipe_pars> = mutableListOf()
    val adapter=Recipe_Adapter(this)
    var login = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_recipe_favorite, container, false)
        database = UsersDatabase.getDatabase(requireContext())
        login = requireContext().getSharedPreferences("Login", Context.MODE_PRIVATE).getString("Login","").toString()
        thread{
            Recipe_list=database.getDaoUsers().getAllFavorite(login).FavoriteRecipes
            requireActivity().runOnUiThread {
                view.findViewById<RecyclerView>(R.id.RecyclerView2).layoutManager = LinearLayoutManager(requireContext())
                view.findViewById<RecyclerView>(R.id.RecyclerView2).adapter = adapter
                Recipe_list.forEach {
                    adapter.RecipeCreate(it)
                }
            }
        }
        return view
    }

    override fun onClick(recipe: Recipe_pars) {
        val fragment = Recipe_now()
        val bundle = Bundle()
        bundle.putInt("id", recipe.id!!)
        println(recipe.id)
        fragment.arguments = bundle
        val fragmentManager = requireActivity().supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.BAZA, fragment)
        fragmentTransaction.commit()
    }

    override fun onLongclick(recipe: Recipe_pars) {
        MaterialDialog(requireContext()).show {
            title(text = "Избранное")
            message (text = "Удалить из избранного выбранный рецепт?")
            positiveButton(text = "Удалить") { dialog ->
                thread {
                    var User = database.getDaoUsers().getUserByLogin(login)
                    runBlocking {
                        database.getDaoUsers().deleteObject(User)
                    }
                    User.FavoriteRecipes.remove(recipe)
                    database.getDaoUsers().insertItem(User)
                }
            }
            negativeButton(text = "Отмена") { dialog ->

            }
        }
    }


}