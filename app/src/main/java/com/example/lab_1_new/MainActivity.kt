package com.example.lab_1_new

import android.animation.Animator
import android.animation.AnimatorInflater
import android.animation.AnimatorSet
import android.content.Intent
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.cardview.widget.CardView
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.input.input
import com.example.lab_1_new.Data_Classes.Users
import java.security.MessageDigest
import java.util.*
import kotlin.concurrent.thread
import kotlin.math.log

class MainActivity : AppCompatActivity() {

    /**Переменная для звука*/
    var mMediaPlayer: MediaPlayer? = null

    /**Переменные для анимации поворота карточки*/
    lateinit var front_anim: AnimatorSet
    lateinit var back_anim: AnimatorSet

    /**Маркеры*/
    var isFront1 = true
    var isAnimating = false

    /**Переменные для работы с разметкой*/
    lateinit var txtEm: EditText
    lateinit var txtPs: EditText
    lateinit var bntEx: Button

    /**Переменная для работы с БД*/
    lateinit var database: UsersDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        database = UsersDatabase.getDatabase(this)

        /**Присвоение переменным объектов из разметки*/
        txtEm = findViewById(R.id.textEmail)
        txtPs = findViewById(R.id.textPassword)
        bntEx = findViewById(R.id.buttonExit)
        val card_front=findViewById<CardView>(R.id.Card)
        val card_back=findViewById<CardView>(R.id.CardBack)

        findViewById<Button>(R.id.buttonReg).setOnClickListener {
            Reg1()
        }

        /**Слушатель для обработкии ввода*/
        bntEx.setOnClickListener {
            val textLogin = txtEm.text.toString()
            val textPassword = txtPs.text.toString()
            thread {
                if (database.getDaoUsers().checkUserExists(textLogin) != 0) {
                    if (database.getDaoUsers().getUserByLogin(textLogin).Password == textPassword) {
                        runOnUiThread {
                            val intent = Intent(this, BottomNavActivity::class.java)
                            startActivity(intent)
                        }
                    } else {
                        runOnUiThread {
                            playSound() //Воспроизведение сирены
                            txtPs.setTextColor(applicationContext.resources.getColor(R.color.red))
                            txtEm.setTextColor(applicationContext.resources.getColor(R.color.red))
                        }
                    }
                }
            }
        }
        /**Возвращение нормального цвета при нажатии*/
        txtEm.setOnFocusChangeListener { v, hasFocus ->
            txtEm.setTextColor(applicationContext.resources.getColor(R.color.black))
        }
        txtPs.setOnFocusChangeListener { v, hasFocus ->
            txtPs.setTextColor(applicationContext.resources.getColor(R.color.black))
        }

        val scale:Float = this.resources.displayMetrics.density
        card_front.cameraDistance=8000*scale
        card_back.cameraDistance=8000*scale
        /**Загрузка анимации в аниматор*/
        front_anim = AnimatorInflater.loadAnimator(this, R.animator.front_animator) as AnimatorSet
        back_anim = AnimatorInflater.loadAnimator(this, R.animator.back_animator) as AnimatorSet
        /**Описание функций аниматора*/
        front_anim.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator) {
                isAnimating = true
            }
            override fun onAnimationEnd(animation: Animator) {
                isAnimating = false
            }
            override fun onAnimationCancel(animation: Animator) {
                isAnimating = false
            }
            override fun onAnimationRepeat(animation: Animator) {
                // Not used in this case
            }
        })
        back_anim.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator) {
                isAnimating = true
            }
            override fun onAnimationEnd(animation: Animator) {
                isAnimating = false
            }
            override fun onAnimationCancel(animation: Animator) {
                isAnimating = false
            }
            override fun onAnimationRepeat(animation: Animator) {
                // Not used in this case
            }
        })
        /**Слушатель для cardView*/
        card_front.setOnClickListener {
            if (!isAnimating) {
                if (isFront1) {
                    front_anim.setTarget(card_front)
                    back_anim.setTarget(card_back)
                    front_anim.start()
                    back_anim.start()
                    isFront1 = false
                } else {
                    front_anim.setTarget(card_back)
                    back_anim.setTarget(card_front)
                    front_anim.start()
                    back_anim.start()
                    isFront1 = true
                }
            }
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

    /**Воспроизведение музыки*/
    fun playSound() {
        if (mMediaPlayer == null) {
            mMediaPlayer = MediaPlayer.create(this, R.raw.sirena)
            mMediaPlayer!!.isLooping = true
            mMediaPlayer!!.start()
        } else mMediaPlayer!!.start()
    }

    fun generateRandomHash(): String {
        val random = Random(System.currentTimeMillis())
        val byteArray = ByteArray(16)
        random.nextBytes(byteArray)
        val md5Digest = MessageDigest.getInstance("MD5")
        val md5Hash = md5Digest.digest(byteArray)
        val hexString = StringBuilder()
        for (byte in md5Hash) {
            val hex = String.format("%02x", byte.toInt() and 0xFF)
            hexString.append(hex)
        }
        return hexString.toString()
    }

    fun EmptyField(){
        Toast.makeText(this, "Поле не может быть пустыми", Toast.LENGTH_LONG).show()
    }

    fun Reg1(){
        var username = ""
        val dialog = MaterialDialog(this)
            .title(text = "Регистрация")
            .message(text = "Введите ваше имя")
            .input(hint = "Имя пользователя") { dialog, text ->
                username = text.toString()
            }.positiveButton(text = "Далее") { dialog ->
                if(username.isNotEmpty()){
                    Reg2(username)
                }else
                    EmptyField()
            }.negativeButton(text = "Отмена") { dialog ->
                // Обработка нажатия кнопки "Отмена"
                dialog.dismiss()
            }
        dialog.show()
    }

    fun Reg2(username: String){
        var login = ""
        val dialog = MaterialDialog(this)
            .title(text = "Регистрация")
            .message(text = "Введите ваш логин")
            .input(hint = "Логин") { dialog, text ->
                login = text.toString()
            }.positiveButton(text = "Далее") { dialog ->
                if(login.isNotEmpty()){
                    thread {
                        if (database.getDaoUsers().checkUserExists(login) == 0) {
                            Reg3(username,login)
                        } else {
                            runOnUiThread {
                                Toast.makeText(this, "Пользователь с таким логином уже есть", Toast.LENGTH_LONG).show()
                            }
                        }
                    }
                }else
                    EmptyField()
            }.negativeButton(text = "Отмена") { dialog ->
                // Обработка нажатия кнопки "Отмена"
                dialog.dismiss()
            }
        dialog.show()
    }

    fun Reg3(username: String, login:String){
        var password = ""
        runOnUiThread {
            val dialog = MaterialDialog(this)
                .title(text = "Регистрация")
                .message(text = "Введите ваш пароль")
                .input(hint = "Пароль", InputType.TYPE_TEXT_VARIATION_PASSWORD) { dialog, text ->
                    password = text.toString()
                }.positiveButton(text = "Зарегистрироваться") { dialog ->
                    if (password.isNotEmpty()) {
                        thread {
                            database.getDaoUsers()
                                .insertItem(Users(generateRandomHash(), username, login, password))
                            runOnUiThread {
                                val intent = Intent(this, BottomNavActivity::class.java)
                                startActivity(intent)
                            }
                        }
                    } else
                        EmptyField()
                }.negativeButton(text = "Отмена") { dialog ->
                    // Обработка нажатия кнопки "Отмена"
                    dialog.dismiss()
                }
            dialog.show()
        }
    }
}
