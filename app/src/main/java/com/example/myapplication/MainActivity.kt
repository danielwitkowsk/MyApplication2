package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.MotionEvent
import android.view.animation.AnimationUtils
import android.view.animation.DecelerateInterpolator
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.ViewSwitcher
import androidx.appcompat.app.AppCompatActivity
import kotlin.random.Random


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val myIntent = Intent(this, WheelView::class.java)

        val muzyka = arrayOf<String>("Billie Eilish","Drake")
        val gry = arrayOf<String>("Mass Effect","Minecraft")
        val literatura = arrayOf<String>("Harry Potter","Lord Of The Rings")
        val filmy = arrayOf<String>("Spirited Away","City of God")
        val przyslowia = arrayOf<String>("Bez pracy nie ma kolaczy","Fortuna kolem sie toczy")
        val categories = arrayOf<String>("Muzyka","Gry","Literatura","Filmy","Przysłowia")
        val cat_text = findViewById<TextView>(R.id.category)
        val cat_button = findViewById<Button>(R.id.rand_category)
        val cat_top = findViewById<TextView>(R.id.top_category)
        cat_button.setOnClickListener{

            val randInt = Random.nextInt(15,25)
            if (randInt%(categories.size)==0) myIntent.putExtra("Category", muzyka[randInt%(muzyka.size)])
            if (randInt%(categories.size)==1) myIntent.putExtra("Category", gry[randInt%(gry.size)])
            if (randInt%(categories.size)==2) myIntent.putExtra("Category", literatura[randInt%(literatura.size)])
            if (randInt%(categories.size)==3) myIntent.putExtra("Category", filmy[randInt%(filmy.size)])
            if (randInt%(categories.size)==4) myIntent.putExtra("Category", przyslowia[randInt%(przyslowia.size)])
            for (i in randInt downTo  0 step 1) {
                val handler = Handler()
                handler.postDelayed({
                    cat_text.text = categories[i%(categories.size)]
                }, (i*200).toLong())
            }
            val handler = Handler()
            handler.postDelayed({
                startActivity(myIntent);
            },6000)
        }



    }


}