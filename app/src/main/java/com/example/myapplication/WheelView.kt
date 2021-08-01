package com.example.myapplication
import com.example.myapplication.MainActivity
import com.example.myapplication.R
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.MotionEvent
import android.view.View
import android.view.animation.DecelerateInterpolator
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import kotlin.properties.Delegates
import kotlin.random.Random

class WheelView : AppCompatActivity() {

     fun letterDialog(v: TextView,theunknown: String,prize: Int,gracz: TextView,v2: TextView) {
        val builder = AlertDialog.Builder(this)
        val inflater = layoutInflater
        val dialogLayout = inflater.inflate(R.layout.letter_dialog,null)
        val editText = dialogLayout.findViewById<EditText>(R.id.letter)
        val consonants = arrayOf<String>("B", "C", "D", "F", "G", "H", "J", "K", "L", "M", "N", "P", "Q", "R", "S", "T", "V", "X", "Z", "W")
        val vowels = arrayOf<String>("A", "E", "I", "O", "U", "Y")
        var state = findViewById<TextView>(R.id.state)
        var letter = ""
        var count=0
        var stringcurrent = v.text
        with (builder) {
            setTitle("Wpisz tu literę")
            setPositiveButton("OK") { dialog, which ->
                letter = editText.text.toString()
                if ((vowels.contains(letter.uppercase())||vowels.contains(letter.lowercase()))&& state.text.toString()=="0") {
                    Toast.makeText(this@WheelView, "Samogłoska? W pierwszej rundzie? Wpisz coś innego.", Toast.LENGTH_SHORT).show()
                    letterDialog(v,theunknown,prize,gracz,v2)
                }
                else if ((vowels.contains(letter.uppercase())||vowels.contains(letter.lowercase()))&& (state.text.toString()=="0" || state.text.toString()=="1") && gracz.text.toString().toInt()<200) {
                    Toast.makeText(this@WheelView, "Nie masz tyle pieniędzy na koncie. Wpisz coś innego.", Toast.LENGTH_SHORT).show()
                    letterDialog(v,theunknown,prize,gracz,v2)
                }
                else {
                    if(stringcurrent.contains(letter,true)) {
                        Toast.makeText(this@WheelView, "Ta litera została już odkryta", Toast.LENGTH_SHORT).show()
                        state.text = "0"
                    }
                    else {
                        if(vowels.contains(letter.uppercase()) && state.text.toString()=="1")
                            gracz.text = (gracz.text.toString().toInt() - 200).toString()
                        if(!consonants.contains(letter.uppercase())) {
                            Toast.makeText(this@WheelView, "Niepoprawny znak!", Toast.LENGTH_SHORT).show()
                            state.text = "0"
                        }
                        for (i in 0 until theunknown!!.length) {
                            if (theunknown[i].toString() == letter.uppercase() || theunknown[i].toString() == letter.lowercase()) {
                                stringcurrent = stringcurrent.substring(0,i)+letter+stringcurrent.substring(i+1,stringcurrent.length)
                                count+=1
                                v.text=stringcurrent
                            }
                        }
                    }
                    if(count!=0) {
                        gracz.text = (gracz.text.toString().toInt() + prize*count).toString()
                        Toast.makeText(this@WheelView, "Zakręć jeszcze raz!", Toast.LENGTH_SHORT).show()
                        state.text = "1"
                    }
                    else {
                        state.text = "0"
                        if(v2.text.toString()=="1")
                            v2.text = "2"
                        else if (v2.text.toString()=="2")
                            v2.text = "3"
                        else if (v2.text.toString()=="3")
                            v2.text = "1"
                        Toast.makeText(this@WheelView, "Good luck next time!", Toast.LENGTH_SHORT).show()
                    }
                }

            }
            setView(dialogLayout)
            show()
        }
    }

    private  fun tryingDialog(theunknown: String,gracz: TextView,v: TextView) {
        val builder = AlertDialog.Builder(this)
        val inflater = layoutInflater
        val dialogLayout = inflater.inflate(R.layout.trying_dialog,null)
        val editText = dialogLayout.findViewById<EditText>(R.id.letter)
        var theword = ""
        var prize=""
        val myIntent = Intent(this, MainActivity::class.java)
        with (builder) {
            setTitle("Wpisz tu wyrażenie")
            setPositiveButton("OK") { dialog, which ->
                theword = editText.text.toString()
                if(theword.equals(theunknown,ignoreCase = true)) {
                    prize = gracz.text.toString()
                    Toast.makeText(this@WheelView, "Gracz" + v.text.toString() + " wygrywa " + prize + "!!!", Toast.LENGTH_SHORT).show()
                    startActivity(myIntent)
                }
                else {
                    Toast.makeText(this@WheelView, "Tough Life", Toast.LENGTH_SHORT).show()
                    gracz.text="0"
                }

            }
            setView(dialogLayout)
            show()
        }
    }
    fun changeText(string: String): String {
        var retstring=""
        for (i in 0..string!!.length-1) {
            if(string[i]!=' ')
                retstring+="X"
            else
                retstring+=" "
        }
        return retstring
    }
    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wheel_view)
        val wheel = findViewById<ImageView>(R.id.wheelimg)
        val spinbutton = findViewById<Button>(R.id.spin_button)
        val thecurrent = findViewById<TextView>(R.id.TheCurrent)
        val theunknown = intent.getStringExtra("Category")
        val wheelStates = arrayOf<Int>(
            0,
            500,
            225,
            425,
            300,
            175,
            0,
            125,
            400,
            200,
            325,
            100,
            0,
            450,
            300,
            200,
            375,
            100,
            0,
            150,
            250,
            350,
            275,
            50
        )
        thecurrent.text = theunknown?.let { changeText(it) }
        val gracz1 = findViewById<TextView>(R.id.Player1)
        val gracz2 = findViewById<TextView>(R.id.Player2)
        val gracz3 = findViewById<TextView>(R.id.Player3)
        val currentTurn = findViewById<TextView>(R.id.PlayerTurn)
        var wheelstate = 0
        var state = findViewById<TextView>(R.id.state)
        state.text="0"
        val trying = findViewById<Button>(R.id.theTry)
        var currentPlayer=0
        spinbutton.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    currentPlayer = currentTurn.text.toString().toInt() - 1
                    var rotateby = Random.nextInt(0, 23)
                    while (wheelStates[(wheelstate + rotateby) % 24] == 0)
                        rotateby = Random.nextInt(0, 23)
                    wheelstate = (wheelstate + rotateby) % 24
                    var prize = wheelStates[wheelstate]
                    val handler = Handler()
                    wheel.animate().rotationBy(-(720 + (rotateby) * 15).toFloat())
                    wheel.animate().interpolator = DecelerateInterpolator(1.0f)
                    wheel.animate().duration = 3200
                    wheel.animate().start()
                    handler.postDelayed({
                        if (theunknown != null) {
                            if (currentPlayer == 0) {
                                letterDialog(thecurrent, theunknown, prize, gracz1, currentTurn)
                            }
                            else if (currentPlayer == 1) {
                                letterDialog(thecurrent, theunknown, prize, gracz2, currentTurn)
                            }
                            else if (currentPlayer == 2) {
                                letterDialog(thecurrent, theunknown, prize, gracz3, currentTurn)
                            }

                        }
                    }, 4600.toLong())
                }
            }
            false
        }
        trying.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    if (theunknown != null) {
                        if (currentPlayer == 0) {
                            tryingDialog(theunknown, gracz1,currentTurn)
                        }
                        else if (currentPlayer == 1) {
                            tryingDialog(theunknown, gracz2,currentTurn)
                        }
                        else if (currentPlayer == 2) {
                            tryingDialog(theunknown, gracz3,currentTurn)
                        }
                    }

                }

            }
            false
        }
    }
    
}