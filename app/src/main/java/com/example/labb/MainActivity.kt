package com.example.labb

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {
    private lateinit var auth:FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        auth = Firebase.auth
        val currentuser = auth.currentUser
        if(currentuser==null){
            callInicioSesion()
        } else {
            currentuser.email?.let { init(it) }
        }
        val btnchat = findViewById<Button>(R.id.btnchat)
        btnchat.setOnClickListener{
            val irchat = Intent(this, ChatActivity::class.java)
            startActivity(irchat)
        }
    }

    private fun init(correo : String){
        val db = FirebaseFirestore.getInstance()
        val btnmanage = findViewById<Button>(R.id.btn_admin)
        val btnCerrarSesion = findViewById<Button>(R.id.btn_cerrar)

        btnCerrarSesion.setOnClickListener{
            borrarPreferences()
            val iniciarSesion = Intent(this, LoginActivity::class.java)
            startActivity(iniciarSesion)
            finish()
        }
        btnmanage.setOnClickListener{
            calladmin()
        }
        db.collection("Users").whereEqualTo("correo", correo).get().addOnSuccessListener {
            it.forEach{
                if(it.getString("type").toString().equals("admin")){
                    btnmanage.visibility = View.VISIBLE
                }else{
                    btnmanage.visibility = View.GONE
                }
            }
        }
    }

    private fun borrarPreferences(){
        val sharedPref = applicationContext.getSharedPreferences("user", Context.MODE_PRIVATE)
        val edit = sharedPref?.edit()
        edit?.putString("prefUser", "Usuario")?.commit()
        edit?.apply()
    }

    private fun calladmin(){
        val sesion = Intent(this, AdminActivity::class.java)
        startActivity(sesion)
    }

    private fun callInicioSesion(){
        val sesion = Intent(this, LoginActivity::class.java)
        startActivity(sesion)
        finish()
    }

    override fun onBackPressed() {
        //super.onBackPressed()
    }
}

