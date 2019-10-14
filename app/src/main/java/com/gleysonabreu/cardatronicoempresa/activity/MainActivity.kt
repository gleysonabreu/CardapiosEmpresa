package com.gleysonabreu.cardatronicoempresa.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import com.gleysonabreu.cardatronicoempresa.R
import com.gleysonabreu.cardatronicoempresa.helper.SettingsFirebase
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.toolbar.*

class MainActivity : AppCompatActivity() {

    private lateinit var autentication: FirebaseAuth;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Settings navigation bottom
        bottomNavigationView.itemTextAppearanceActive = R.color.colorAccent;

        //Settings FirebaseAuth
        autentication = SettingsFirebase.getFirebaseAuth();

        //Settings toolbar
        toolbar.title = "Cardatrônico - Painel de Controle";
        setSupportActionBar(toolbar)
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        val inflater: MenuInflater = menuInflater;
        inflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        when(item?.itemId){
            R.id.signOut -> {

                autentication.signOut();
                var intent: Intent = Intent(this, LoginActivity::class.java);
                startActivity(intent);
                finish();

            }
        }
        return super.onOptionsItemSelected(item)
    }
}
