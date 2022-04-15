package com.jorgemartinez.duckhunt.ui

import android.content.DialogInterface
import android.content.Intent
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.jorgemartinez.duckhunt.R
import com.jorgemartinez.duckhunt.common.Constantes
import kotlin.random.Random

class GameActivity : AppCompatActivity() {
    lateinit var tvCounterDucks:TextView
    lateinit var tvTimer:TextView
    lateinit var tvNick:TextView
    lateinit var ivDuck:ImageView
    var aleatorio = 0

    var counter = 0
    var anchoPantalla = 0
    var altoPantalla = 0
    var gameOver = false


    //instnacia
    val db = Firebase.firestore
    lateinit var id:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

      iniViewComponents()
      eventos()

        initPantalla()
        initCuentaAtras()


    }

    private fun initCuentaAtras() {
        //primer parámetro indica el total de duración
        //el segundo parámetro es el intervalo de cada segundo
        object : CountDownTimer(5000, 1000) {

            override fun onTick(millisUntilFinished: Long) {
                val segundosRestantes = millisUntilFinished/1000
                tvTimer.setText(segundosRestantes.toString() + "s" )
            }

            override fun onFinish() {
                tvTimer.setText("0")
                gameOver = true
                mostrarDialogoGameOver()
                saveResultFirestore()
            }
        }.start()

    }

    private fun saveResultFirestore() {

        db.collection("users").document(id)
            .update(
                "ducks",counter
            )
    }

    private fun mostrarDialogoGameOver() {
// 1. Instantiate an <code><a href="/reference/android/app/AlertDialog.Builder.html">AlertDialog.Builder</a></code> with its constructor
        val builder: AlertDialog.Builder = this.let {
            AlertDialog.Builder(it)
        }

// 2. Chain together various setter methods to set the dialog characteristics
        builder.setMessage("Has conseguido cazar $counter patos")
            .setTitle("Game over")
            .setPositiveButton("Reiniciar",
                DialogInterface.OnClickListener { dialog, id ->

                    //Reiniciamos juego
                    counter = 0
                    tvCounterDucks.text = ""
                    gameOver = false
                    initCuentaAtras()

                    //movemos el pato a una posicion aleatoria
                    //para que no inicie en el mismo lugar donde acabó
                    moveDuck()
                })
            .setNegativeButton("Ver Ranking",
                DialogInterface.OnClickListener { dialog, id ->
                    // User cancelled the dialog
                    dialog.dismiss()

                    //finish()

                    val intent = Intent(this,RankingActivity::class.java)
                    startActivity(intent)

                })

// 3. Get the <code><a href="/reference/android/app/AlertDialog.html">AlertDialog</a></code> from <code><a href="/reference/android/app/AlertDialog.Builder.html#create()">create()</a></code>
        val dialog: AlertDialog = builder.create()
        //mostrar
        dialog.show()
    }

    private fun initPantalla() {
        // Obtener el tamaño de la pantalla del dispositivo

        val display = windowManager.currentWindowMetrics.bounds
        anchoPantalla = display.width()
        altoPantalla = display.height()

    }


    private fun iniViewComponents() {
        tvCounterDucks = findViewById(R.id.textViewCounter)
        tvTimer = findViewById(R.id.textViewTimer)
        tvNick = findViewById(R.id.textViewNick)
        ivDuck = findViewById(R.id.imageViewDuck)

        //Cambiar tipo de fuente

        val typeface = Typeface.createFromAsset(assets,"pixel.ttf")
        tvNick.typeface = typeface
        tvTimer.typeface = typeface
        tvCounterDucks.typeface = typeface

        val extras =   intent.extras

        val nick = extras?.getString(Constantes.EXTRA_NICK) ?: "La variable es null"
        tvNick.text = nick


        id = extras?.getString(Constantes.EXTRA_ID)!!
    }

    private fun eventos() {

        ivDuck.setOnClickListener {
            //ara evitar dar clicks despues de acabado el juego
            if (!gameOver){
                counter++
                tvCounterDucks.text = counter.toString()


                ivDuck.setImageResource(R.drawable.duck_hunt)
                ivDuck.isEnabled = false

                retraso()
            }



        }
    }

    private fun retraso() {
            Handler.createAsync(mainLooper).postDelayed(Runnable {
                ivDuck.setImageResource(R.drawable.pato)
                ivDuck.isEnabled = true
                moveDuck()

            },1000)

    }

    private fun moveDuck() {
        //Conocer tamaño de pantalla
        val min = 0
        //El máximo del rango x menos la anchura del pato para que no se quede fuera del display
        val maximoX = anchoPantalla - ivDuck.width
        val maximoY = altoPantalla -ivDuck.height

        //inicializamos el objeto para generar números aleatorios



        //Generamos 2 números aleatorios, uno para x  y otro para y


        val randomX =   Random.nextInt(min,maximoX)
        val randomY =   Random.nextInt(min,maximoY)

        //Utilizamos los números aleatorios para mover el pato a esa posición

        ivDuck.x = randomX.toFloat()
        ivDuck.y = randomY.toFloat()


    }

}