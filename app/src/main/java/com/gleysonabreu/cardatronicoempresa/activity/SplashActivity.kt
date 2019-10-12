package com.gleysonabreu.cardatronicoempresa.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.gleysonabreu.cardatronicoempresa.R

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        Handler().postDelayed({

            openActivity();

        }, 2000)
    }

    private fun openActivity(){

        val i: Intent = Intent(this, LoginActivity::class.java);
        startActivity(i);
        finish();

    }
}
