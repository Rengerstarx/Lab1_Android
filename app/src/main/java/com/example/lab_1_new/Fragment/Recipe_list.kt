package com.example.lab_1_new.Fragment

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.bekawestberg.loopinglayout.library.LoopingLayoutManager
import com.beust.klaxon.JsonObject
import com.beust.klaxon.Klaxon
import com.example.lab_1_new.Data_Classes.Recipe
import com.example.lab_1_new.Adapters.Recipe_Adapter
import com.example.lab_1_new.Data_Classes.Recipe_pars
import com.example.lab_1_new.R
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import kotlin.concurrent.thread

class Recipe_list : Fragment(), Recipe_Adapter.Listener {

    lateinit var sharedPreferences: SharedPreferences
    lateinit var editor: SharedPreferences.Editor
    private lateinit var viewManager: RecyclerView.LayoutManager
    lateinit var apiJson: URL
    var Recipe_pars_list: MutableList<Recipe_pars> = mutableListOf()
    val adapter=Recipe_Adapter(this) //Адаптер для списка

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_recipe_list, container, false)
        thread {
            apiJson = URL("https://raw.githubusercontent.com/Lpirskaya/JsonLab/master/recipes2022.json")
            ParsJson(apiJson)
            PrintJson()
        }
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

    /**Функция получения JSON файла по ссылке используя HttpURLConnection и парсинга спомощью библиотеки Klaxon*/
    private fun ParsJson(url: URL){
        /**Устанавливаем соединение с удаленным сервером по URL
         * Создаем объект для работы с этим соединением
         * Указываем что работаем именно с HTTP соединением*/
        val connection = url.openConnection() as HttpURLConnection
        /**Проверка на адекваттный возврат*/
        if(connection.responseCode == HttpURLConnection.HTTP_OK) {
            /**Возвращаем поток данных сс удаленного сервера
             * Преобразуем байтовый поток в символьный
             * Получаем объект с большими возможностями для чтения*/
            val Input = BufferedReader(InputStreamReader(connection.inputStream))
            /**Парсим полученные данный используя Klaxon (https://github.com/cbeust/klaxon)*/
            val JSONArray = Klaxon().parseJsonArray(Input)
            /**Берем каждый обьект
             * Преобразовываем в JsonObject
             * Преобразовываем в массив
             * Формируем объект типа DataClass
             * Записывем его в MutableList c Дата классами*/
            JSONArray.forEach {
                val JSONobject = it as JsonObject
                val field = JSONobject.values.toTypedArray()
                Recipe_pars_list.add(
                    Recipe_pars(
                        Recipe_pars_list.size,
                        field[0].toString(),
                        field[1] as Int,
                        field[2].toString(),
                        field[3].toString(),
                        field[4] as Int
                    )
                )
            }
        }
    }

    /**Выводим в Log все поля поочереди (Паралельно фильтруя их)*/
    private fun PrintJson(){
        Recipe_pars_list.forEach {
            val string ="Название рецепта: ${it.Name}\n" +
                    "Время готовки: ${it.Time}\n" +
                    "Сложность: ${it.Difficulty}\n" +
                    "Каллорийность: ${it.Calorie}\n" +
                    "Необходимые ингридиенты: ${it.Ingredients}"
            Log.e("Parse", string)
        }
    }

}