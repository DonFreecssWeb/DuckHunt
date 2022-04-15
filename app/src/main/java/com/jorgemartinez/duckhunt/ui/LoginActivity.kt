package com.jorgemartinez.duckhunt.ui
/*
git remote add origin https://github.com/DonFreecssWeb/DuckHunt.git
git branch -M main
git push -u origin main
* */
import android.content.Intent
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.jorgemartinez.duckhunt.R
import com.jorgemartinez.duckhunt.common.Constantes
import com.jorgemartinez.duckhunt.models.User

class LoginActivity : AppCompatActivity() {

    lateinit var etNick:EditText
    lateinit var btnStart: Button
    lateinit var  nick:String
    //instancia de Firestore
    val db = Firebase.firestore
    var contadorBotonStart = 0
    var contadorBotonStartAntes = 0
    var start = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        etNick = findViewById(R.id.editTextTextNick)
        btnStart = findViewById(R.id.buttonStart)

        //Cambiar tipo de fuente

        val typeface = Typeface.createFromAsset(assets,"pixel.ttf")
        etNick.typeface = typeface
        btnStart.typeface = typeface

        btnStart.setOnClickListener {

            nick = etNick.text.toString()
            start = true

            if (nick.isEmpty()) etNick.setError("El nombre de usuario es obligatorio")
            else {

               addNickAndStart()

            }
        }
    }

    private fun addNickAndStart() {
        //Ya tenemos un nick válido

        //consultamos si el nick ya existe en la base de datos

        contadorBotonStartAntes++
        Log.e("e","Antes" + contadorBotonStartAntes.toString())
        db.collection("users").whereEqualTo("nick",nick).addSnapshotListener(EventListener { value, error ->
            contadorBotonStart++
            if(start == true){
                Log.e("e","despues" + contadorBotonStart.toString())
                if (value!!.size() > 0){
                    etNick.setError("El nick no está disponible")
                }else{
                    //inssertamos en base de datos
                    addNickToFirestore()
                }
            }


        })



    }

    private fun addNickToFirestore() {
        //Agregamos al Firestore
        // Create a new user with a first and last name
        val user = hashMapOf(
            "nick" to nick,
            "ducks" to 3,
        )
        //si la clase estaba como private las propiedades del constructor,
        //se produce un error

        val nuevoUsuario = User(nick,0)

        //añadimos al usuario
        db.collection("users")
            .add(nuevoUsuario)
            .addOnSuccessListener {
                //Si es exitoso, osea si se inserta en la base se de datos, que vaya al siguiente activity

                //limpiamos campo, para que al regresar del juego aparezca vacio
                //setText porque es un EditText si fuera un TextView sería etNick.text = ""
                etNick.setText("")
                start = false
                val intent = Intent(this, GameActivity::class.java).apply {
                    putExtra(Constantes.EXTRA_NICK,nick)  //es igual a i.putExtra(xxxx)
                    putExtra(Constantes.EXTRA_ID, it.id)
                }
                startActivity(intent)

            }


    }
}