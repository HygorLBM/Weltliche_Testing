package com.example.hygor.weltliche

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.AuthResult
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import android.support.annotation.NonNull
import com.google.android.gms.tasks.OnFailureListener
import com.google.firebase.firestore.DocumentReference
import com.google.android.gms.tasks.OnSuccessListener





class CriarUsuario : AppCompatActivity() {

    private var mAuth: FirebaseAuth? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_criar_usuario)

        //Adicionando instancia do Firebase
// ...
        mAuth = FirebaseAuth.getInstance()


        //Adicionando elementos da interface
        val wam_userName = findViewById<EditText>(R.id.Name2)
        val wam_userEmail = findViewById<EditText>(R.id.Email2)
        val wam_password = findViewById<EditText>(R.id.Password2)
        val wam_signup = findViewById<Button>(R.id.Cadastrar2)


        //Adicionando instancia ao banco de dados
        val db = FirebaseFirestore.getInstance()
        if (FirebaseApp.getApps(this).isEmpty())
        {
            val settings = FirebaseFirestoreSettings.Builder()
                    .setPersistenceEnabled(true)
                    .build()
            db.setFirestoreSettings(settings)
        }

        //Esconder Action-bar
        getSupportActionBar()!!.hide()


        //Tratando criação de conta
        wam_signup.setOnClickListener {
            val username = wam_userName.text.toString()
            val email = wam_userEmail.text.toString()
            val password = wam_password.text.toString()
            mAuth!!.createUserWithEmailAndPassword(email,password).addOnCompleteListener(this,{ task ->
                        if (task.isSuccessful) {
                            // Sign up success, update UI with the signed-in user's information
                            Log.d("Sucesso!", "signUpWithEmail:success")
                            val user = mAuth!!.currentUser
                            //Envio de e-mail
                            user!!.sendEmailVerification().addOnCompleteListener(this) { task2 ->
                                        // [START_EXCLUDE]
                                        // Re-enable button
                                        if (task2.isSuccessful) {
                                            val user = mAuth!!.currentUser
                                            Log.d("Sucesso!", "sendEmailVerification:success")
                                            Toast.makeText(this@CriarUsuario, "E-mail de verificação enviado para: " + user!!.email, Toast.LENGTH_SHORT).show()
                                            val request = UserProfileChangeRequest.Builder().setDisplayName(username).build()
                                            user!!.updateProfile(request)

                                            //Adicionando a conta ao banco de dados com atuais 0 personagens
                                            val new_user = HashMap<String, Any>()
                                            new_user.put("Id", user.uid)
                                            new_user.put("username", username)
                                            new_user.put("characters", 0)
                                            db.collection("users").document(user.uid)
                                                    .set(new_user)
                                                    .addOnSuccessListener { Log.d("DatabaseAdd", "DocumentSnapshot successfully written!") }
                                                    .addOnFailureListener { e -> Log.w("DatabaseAdd", "Error writing document", e) }


                                            //Indo para a tela de login
                                            val intent = Intent (this, Login::class.java)
                                            startActivity(intent)
                                        } else {
                                            Log.d("Falha!", "sendEmailVerification:failed")
                                            Toast.makeText(this@CriarUsuario, "Falha no envio do e-mail de verificação.", Toast.LENGTH_SHORT).show()
                                        }
                                        // [END_EXCLUDE]
                                    }
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(this, "E-mail/Password inválido", Toast.LENGTH_SHORT).show()
                        }
            })
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


}