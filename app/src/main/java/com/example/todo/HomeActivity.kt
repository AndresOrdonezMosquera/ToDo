package com.example.todo

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth

enum class ProviderTipy{
    Basic,
    GOOGLE
}
class HomeActivity : AppCompatActivity() {
    private var textViewEmail : TextView?=null
    private var textViewProvider : TextView?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        textViewEmail = findViewById(R.id.textViewEmail)
        textViewProvider = findViewById(R.id.textViewProvider)
        val bundle = intent.extras
        val email = bundle?.getString("email")
        val provider = bundle?.getString("provider")
        cargar(email?:"",provider?:"")
        val prefs = getSharedPreferences(getString(R.string.prefs_file),Context.MODE_PRIVATE).edit()
        prefs.putString("email",email)
        prefs.putString("provider",provider)
        prefs.apply()
    }

    private fun cargar(email: String, provider: String) {
        title="inicio"
        textViewEmail!!.text=email
        textViewProvider!!.text=provider

    }

    fun btnLogOut(botonLogOut: View) {
        val prefs = getSharedPreferences(getString(R.string.prefs_file),Context.MODE_PRIVATE).edit()
        prefs.clear()
        prefs.apply()

        FirebaseAuth.getInstance().signOut()
        onBackPressed()
    }
}