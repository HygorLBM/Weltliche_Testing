package com.example.hygor.weltliche

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import android.util.Log
import android.graphics.Paint


class Login : AppCompatActivity() {

    private var mAuth: FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

// ...
        mAuth = FirebaseAuth.getInstance()


        //Adicionando elementos da interface
        val wam_userEmail = findViewById<EditText>(R.id.Email1)
        val wam_password = findViewById<EditText>(R.id.Password1)
        val wam_login = findViewById<Button>(R.id.toLogin1)
        val cadastrar = findViewById<Button>(R.id.Cadastrar1)
        cadastrar.setPaintFlags(cadastrar.getPaintFlags() or Paint.UNDERLINE_TEXT_FLAG)

        //Escondendo Action-bar
        getSupportActionBar()!!.hide()

        //Tratando login:
        wam_login.setOnClickListener {
            val username = wam_userEmail.text.toString()
            val password = wam_password.text.toString()
            mAuth!!.signInWithEmailAndPassword(username, password).addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("Sucesso!", "signInWithEmail:success")
                    val user = mAuth!!.currentUser
                    // Sign in success, update UI with the signed-in user's information
                    if (!user!!.isEmailVerified){
                        Toast.makeText(this@Login, "Por favor, confirme seu e-mail",Toast.LENGTH_LONG).show()
                        user.sendEmailVerification()
                    }
                    else{
                        LoginSucesful(user)
                    }

                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("Falha!", "signInWithEmail:failure", task.exception)
                    LoginFailed()
                        }
                    }
        }

        //Tratando criação de conta
        cadastrar.setOnClickListener{
            val goToSignUp = Intent(this@Login, CriarUsuario::class.java)
            startActivity(goToSignUp)

        }

    }

    public override fun onStart() {
        super.onStart()
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = mAuth!!.currentUser
    }

    public override fun onStop() {
        super.onStop()
        val mAuth = FirebaseAuth.getInstance();
        val  mAuthListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
            val user = firebaseAuth.currentUser
            if (user != null) {
                // User is signed in
                Log.d("User signed in!", "User signed in")
            } else {
                // User is signed out
                Log.d("User signed out!", "User signed out")
            }
            // ...
        }
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener)
        }
    }

    private fun LoginSucesful(user: FirebaseUser) {

        Toast.makeText(this@Login, "Bem -vindo, " + user.displayName, Toast.LENGTH_LONG).show()
        val goToPersonagens = Intent(this@Login, Personagens::class.java)
        startActivity(goToPersonagens)
    }

    private fun LoginFailed() {
        Toast.makeText(this@Login, "E-mail/Senha inválido", Toast.LENGTH_LONG).show()
        val wam_userEmail = findViewById<EditText>(R.id.Email1)
        val wam_password = findViewById<EditText>(R.id.Password1)
        val reset : String
        reset = ""
        wam_userEmail.setText(reset)
        wam_password.setText(reset)
    }


}
