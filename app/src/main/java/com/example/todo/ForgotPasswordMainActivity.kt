package com.example.todo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

class ForgotPasswordMainActivity : AppCompatActivity() {
    private var editEmailforget : EditText?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password_main)
        editEmailforget = findViewById(R.id.editEmailforget)
    }

    fun Toforget(view: android.view.View) {
        FirebaseAuth.getInstance().sendPasswordResetEmail(
            editEmailforget!!.text.toString())
            .addOnCompleteListener {
                if (it.isSuccessful){
                    Toast.makeText(this,"correo enviado",Toast.LENGTH_LONG).show()
                    val intent = Intent (this,AuthActivity::class.java)
                    startActivity(intent)
                    finish()
                }else{
                    Toast.makeText(this,"correo no enviado",Toast.LENGTH_LONG).show()
                }
            }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent (this,AuthActivity::class.java)
        startActivity(intent)
        finish()
    }
}