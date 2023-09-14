package com.example.lab_1_new

import android.animation.Animator
import android.animation.AnimatorInflater
import android.animation.AnimatorSet
import android.content.Intent
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.cardview.widget.CardView

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

    /**Контейнер для работы с паролями и логинами*/
    var EmailAndPassword: MutableMap<String, String> = mutableMapOf<String,String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        /**Присвоение переменным объектов из разметки*/
        txtEm = findViewById(R.id.textEmail)
        txtPs = findViewById(R.id.textPassword)
        bntEx = findViewById(R.id.buttonExit)
        val card_front=findViewById<CardView>(R.id.Card)
        val card_back=findViewById<CardView>(R.id.CardBack)

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
                playSound() //Воспроизведение сирены
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
}
