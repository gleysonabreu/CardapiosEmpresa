package com.gleysonabreu.cardatronicoempresa.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.gleysonabreu.cardatronicoempresa.R
import com.gleysonabreu.cardatronicoempresa.helper.SettingsFirebase
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_forgot_password.*
import kotlinx.android.synthetic.main.toolbar.*

class ForgotPasswordActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        toolbar.title = "Recuperar senha";
        setSupportActionBar(toolbar);
        supportActionBar?.setDisplayHomeAsUpEnabled(true);

        auth = SettingsFirebase.getFirebaseAuth();


        buttonForgot.setOnClickListener {
            var email = editTextEmailForgot.text.toString();

            if(email.isEmpty()){
                Toast.makeText(this, "Preencha um e-mail", Toast.LENGTH_SHORT).show();
            }else{

                forgotPassword(email);

            }

        }

    }

    private fun forgotPassword(emailForgot: String){

        auth.sendPasswordResetEmail(emailForgot)
            .addOnCompleteListener{
                task ->
                if(task.isSuccessful){
                    Toast.makeText(this, "E-mail para redefinir senha enviado com sucesso!", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(this, "Preencha um e-mail válido.", Toast.LENGTH_SHORT).show();
                }
            }

    }
}
