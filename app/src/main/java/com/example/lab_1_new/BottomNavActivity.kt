package com.example.lab_1_new

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import com.example.lab_1_new.Fragment.Recipe_favorite
import com.example.lab_1_new.Fragment.Recipe_list
import com.example.lab_1_new.Fragment.Recipe_now
import io.ak1.Bubble
import io.ak1.BubbleTabBar
import io.ak1.OnBubbleClickListener

class BottomNavActivity : AppCompatActivity() {

    lateinit var bubble: BubbleTabBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bottom_nav)
        replaceFragment(Recipe_list())
        bubble = findViewById(R.id.bubbleTabBar)
        bubble.addBubbleListener(object : OnBubbleClickListener{
            override fun onBubbleClick(id: Int) {
                when(id){
                    R.id.Spisok -> {
                        replaceFragment(Recipe_list())
                    }
                    R.id.Now -> {
                        replaceFragment(Recipe_now())
                    }
                    R.id.Favorite -> {
                        replaceFragment(Recipe_favorite())
                    }
                }
            }
        })
    }

    fun replaceFragment(fragment: Fragment){
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.BAZA,fragment)
        fragmentTransaction.commit()
    }

}