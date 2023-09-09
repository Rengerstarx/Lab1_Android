package com.example.lab_1_new

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText

class MainActivity : AppCompatActivity() {

    /**Переменные для работы с разметкой*/
    lateinit var txtEm: EditText
    lateinit var txtPs: EditText
    lateinit var bntEx: Button

    /**Контейнер для работы с паролями и логинами*/
    var EmailAndPassword: MutableMap<String, String> = mutableMapOf<String,String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        /**Присвоение переменным объектов из разметки*/
        txtEm = findViewById(R.id.textEmail)
        txtPs = findViewById(R.id.textPassword)
        bntEx = findViewById(R.id.buttonExit)
        /**Заполнение контейнера*/
        val array1 = applicationContext.resources.getStringArray(R.array.Emails)
        val array2 = applicationContext.resources.getStringArray(R.array.Passwords)
        for (i in array1.indices){
            EmailAndPassword[array1[i]] = array2 [i]
        }
        /**Слушатель для обработкии ввода*/
        bntEx.setOnClickListener {
            if(EmailAndPassword.containsKey(txtEm.text.toString()) && EmailAndPassword[txtEm.text.toString()] == txtPs.text.toString()){
                val intent = Intent(this, BottomNavActivity::class.java)
                startActivity(intent)
            }else{
                txtPs.setTextColor(applicationContext.resources.getColor(R.color.red))
                txtEm.setTextColor(applicationContext.resources.getColor(R.color.red))
            }
        }
        /**Возвращение нормального цвета при нажатии*/
        txtEm.setOnFocusChangeListener { v, hasFocus ->
            txtEm.setTextColor(applicationContext.resources.getColor(R.color.black))
        }
        txtPs.setOnFocusChangeListener { v, hasFocus ->
            txtPs.setTextColor(applicationContext.resources.getColor(R.color.black))
        }
    }

    /**Сохранение состояния activity при перезаписи(повороте)*/
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("email", txtEm.text.toString())
        outState.putString("password", txtPs.text.toString())
    }

    /**Восстановление состояния активити*/
    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        val email = savedInstanceState.getString("email")
        val password = savedInstanceState.getString("password")
        txtEm.setText(email)
        txtPs.setText(password)
    }
}
