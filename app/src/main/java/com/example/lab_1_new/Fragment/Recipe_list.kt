package com.example.lab_1_new.Fragment

 import android.content.Context
 import android.content.SharedPreferences
 import android.os.Bundle
 import android.view.LayoutInflater
 import android.view.View
 import android.view.ViewGroup
 import androidx.recyclerview.widget.RecyclerView
 import com.afollestad.materialdialogs.MaterialDialog
 import com.bekawestberg.loopinglayout.library.LoopingLayoutManager
 import com.beust.klaxon.JsonObject
 import com.beust.klaxon.Klaxon
 import com.example.lab_1_new.Adapters.Recipe_Adapter
 import com.example.lab_1_new.Data_Classes.Recipe_pars
 import com.example.lab_1_new.R
 import com.example.lab_1_new.Databases.RecipeDatabase
 import com.example.lab_1_new.Databases.UsersDatabase
 import kotlinx.coroutines.runBlocking
 import java.io.BufferedReader
 import java.io.InputStreamReader
 import java.net.HttpURLConnection
 import java.net.URL
 import kotlin.concurrent.thread

class Recipe_list : androidx.fragment.app.Fragment(), Recipe_Adapter.Listener {

    private lateinit var viewManager: RecyclerView.LayoutManager
    lateinit var apiJson: URL
    var Recipe_pars_list: List<Recipe_pars> = listOf()
    val adapter=Recipe_Adapter(this) //Адаптер для списка
    lateinit var db: RecipeDatabase
    lateinit var dbU: UsersDatabase
    var login = ""
    lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_recipe_list, container, false)
        sharedPreferences = requireContext().getSharedPreferences("Login", Context.MODE_PRIVATE)
        login = sharedPreferences.getString("Login","").toString()
        println(login)
        db = RecipeDatabase.getDatabase(requireContext())
        dbU = UsersDatabase.getDatabase(requireContext())
        thread {
            apiJson = URL("https://raw.githubusercontent.com/Lpirskaya/JsonLab/master/recipes2022.json")
            if(db.getDao().getCountRecipes()==0){
                ParsJson(apiJson)
            }
            Recipe_pars_list=db.getDao().getAllRecipes()
            requireActivity().runOnUiThread {
                /**Цикличный слой для бесконечной крутилки(https://github.com/BeksOmega/looping-layout)*/
                viewManager = LoopingLayoutManager(
                    requireContext(),
                    LoopingLayoutManager.VERTICAL,
                    false
                )
                /**Обьявляем разметку списка и его адаптер*/
                view.findViewById<RecyclerView>(R.id.RecyclerView).layoutManager= viewManager
                view.findViewById<RecyclerView>(R.id.RecyclerView).adapter=adapter
                /**Заполнения списка*/
                Recipe_pars_list.forEach{
                    adapter.RecipeCreate(it)
                }
            }
        }
        return view
    }

    /**Наследуемый от адаптера слушатель для обработки нажания - в данном случае переход на некст фрагмент*/
    override fun onClick(recipe: Recipe_pars) {
        val fragment = Recipe_now()
        val bundle = Bundle()
        bundle.putInt("id", recipe.id!!)
        print(recipe.id)
        fragment.arguments = bundle
        val fragmentManager = requireActivity().supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.BAZA, fragment)
        fragmentTransaction.commit()
    }

    override fun onLongclick(recipe: Recipe_pars) {
        thread {
            val User = dbU.getDaoUsers().getUserByLogin(login)
            if(User.FavoriteRecipes.contains(recipe)){
                requireActivity().runOnUiThread {
                    MaterialDialog(requireContext()).show {
                        title(text = "Избранное")
                        message (text = "Удалить из избранного выбранный рецепт?")
                        positiveButton(text = "Удалить") { dialog ->
                            runBlocking {
                                dbU.getDaoUsers().deleteObject(User)
                            }
                            thread {
                                User.FavoriteRecipes.remove(recipe)
                                dbU.getDaoUsers().insertItem(User)
                            }
                        }
                        negativeButton(text = "Отмена") { dialog ->

                        }
                    }
                }
            }else{
                requireActivity().runOnUiThread {
                    MaterialDialog(requireContext()).show {
                        title(text = "Избранное")
                        message(text = "Добавить в избранное выбранный рецепт?")
                        positiveButton(text = "Добавить") { dialog ->
                            runBlocking {
                                dbU.getDaoUsers().deleteObject(User)
                            }
                            thread {
                                User.FavoriteRecipes.add(recipe)
                                dbU.getDaoUsers().insertItem(User)
                            }
                        }
                        negativeButton(text = "Отмена") { dialog ->

                        }
                    }
                }
            }
        }
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
                val recipe = Recipe_pars(
                    null,
                    field[0].toString(),
                    field[1] as Int,
                    field[2].toString(),
                    field[3].toString(),
                    field[4] as Int
                )
                db.getDao().insertItem(recipe)
            }
        }
    }

}