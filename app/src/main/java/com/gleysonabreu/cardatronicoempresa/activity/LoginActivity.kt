package com.gleysonabreu.cardatronicoempresa.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.gleysonabreu.cardatronicoempresa.R
import com.gleysonabreu.cardatronicoempresa.helper.SettingsFirebase
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    private lateinit var autentication: FirebaseAuth;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Create auth
        autentication = SettingsFirebase.getFirebaseAuth();
        // VerifyLogin
        verifyLogin();

        buttonSignup.setOnClickListener {
            openSignup();
        }

        buttonLogin.setOnClickListener {

            val email: String = editTextEmailLogin.text.toString();
            val password: String = editTextPaswordLogin.text.toString();
            if( !email.isEmpty() && !password.isEmpty() ) {

                autentication.signInWithEmailAndPassword(email, password).addOnCompleteListener{
                    task: Task<AuthResult> ->

                    if(task.isSuccessful){

                        Toast.makeText(
                            this,
                            R.string.loginSuccessful,
                            Toast.LENGTH_LONG
                        ).show()

                        openMain();

                    }else{
                        Toast.makeText(
                            this,
                            "Erro ao fazer lgoin: "+ task.exception,
                            Toast.LENGTH_LONG
                        ).show()
                    }

                }

            }else{

                Toast.makeText(
                    this,
                    R.string.messageLoginNeedData,
                    Toast.LENGTH_LONG
                ).show()

            }
        }

    }

    fun openSignup(){
        var intent: Intent = Intent(this, SingupActivity::class.java);
        startActivity(intent);
    }

    fun openMain(){
        var intent: Intent = Intent(this, MainActivity::class.java);
        startActivity(intent);
        finish();
    }

    fun verifyLogin(){

        var currentUser: FirebaseUser? = autentication.currentUser;
        if(currentUser != null){
            openMain();
        }

    }
}
