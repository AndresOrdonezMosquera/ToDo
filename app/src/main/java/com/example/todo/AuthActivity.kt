package com.example.todo

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.oAuthCredential

class AuthActivity : AppCompatActivity() {
    private val GOOGLE_SING_IN = 100
    private var editEmail : EditText?= null
    private var editPassword : EditText?= null
    private var authLayout : LinearLayout?=null
    private var btnGoogle : SignInButton?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)
        editEmail = findViewById(R.id.editEmail)
        editPassword = findViewById(R.id.editPassword)
        authLayout = findViewById(R.id.authLayouth)
        btnGoogle = findViewById(R.id.btnGoogle)
        session()
        loginGoogle()
    }

    fun loginGoogle() {
        btnGoogle!!.setOnClickListener {
            val googleleconf : GoogleSignInOptions = GoogleSignInOptions.Builder(
                GoogleSignInOptions.DEFAULT_SIGN_IN
            ).requestIdToken("556661578266-47eim6up0c02m4nndi3vatbr3dlpkn8g.apps.googleusercontent.com")
                .requestEmail()
                .build()
            val googleClient : GoogleSignInClient = GoogleSignIn.getClient(this, googleleconf)
            googleClient.signOut()
            startActivityForResult(googleClient.signInIntent,GOOGLE_SING_IN)
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode==GOOGLE_SING_IN){
            val task : Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account : GoogleSignInAccount? = task.getResult(ApiException::class.java)
                if (account != null){
                    val credential : AuthCredential = GoogleAuthProvider.getCredential(account.idToken,null)
                    FirebaseAuth.getInstance().signInWithCredential(credential).addOnCompleteListener {
                        if (it.isSuccessful){
                            showHome(account.email?:"", ProviderTipy.GOOGLE )
                        } else {
                            showAlert()
                        }
                    }
                }
            }catch (e: ApiException){
                showAlert()
            }
        }
    }

    private fun session() {
        val prefs : SharedPreferences= getSharedPreferences(
            getString(R.string.prefs_file),
            Context.MODE_PRIVATE)
        val email : String?=prefs.getString("email",null)
        val provider : String?=prefs.getString("provider", null)
        if (email!=null && provider!=null){
            authLayout!!.visibility= View.INVISIBLE
            showHome(email, ProviderTipy.valueOf(provider))
        }
    }

    override fun onStart() {
        super.onStart()
        authLayout!!.visibility= View.VISIBLE
    }

    fun btn_prueba(boton_prueba: View) {
        val principal= Intent(this, MainActivity::class.java)
        startActivity(principal)

    }

    fun onRegistrer(botonRegister: View) {
        title = "Autenticacion"
        var email = editEmail!!.text
        var password = editPassword!!.text
        if (email.isNotEmpty()&&password.isNotEmpty()){
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(
                email.toString(),
                password.toString()
            ).addOnCompleteListener{
                if(it.isSuccessful){
                    Toast.makeText(this,"correcto", Toast.LENGTH_LONG).show()
                }else {
                    showAlert()
                }
            }
        }
    }

    private fun showAlert() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("error")
        builder.setMessage("se ha producido un error al autenticar el usuario")
        builder.setPositiveButton("aceptar", null)
        val dialog : AlertDialog = builder.create()
        dialog.show()
    }
    private fun showHome (email : String, provider : ProviderTipy){
        val homeIntent= Intent(this, HomeActivity::class.java).apply{
            putExtra("email",email)
            putExtra("provider",provider.name)
        }
        startActivity(homeIntent)
    }

    fun onLogin(botonLogin: View) {
        title = "Login"
        var email = editEmail!!.text
        var password = editPassword!!.text
        if (email.isNotEmpty()&&password.isNotEmpty()){
            FirebaseAuth.getInstance().signInWithEmailAndPassword(
                email.toString(),
                password.toString()
            ).addOnCompleteListener{
                if(it.isSuccessful){
                    showHome(it.result?.user?.email?:"",ProviderTipy.Basic)
                }else {
                    showAlert()
                }
            }
        }
    }
}