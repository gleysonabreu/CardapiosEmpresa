package com.gleysonabreu.cardatronicoempresa.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.gleysonabreu.cardatronicoempresa.R
import com.gleysonabreu.cardatronicoempresa.fragment.AddCardapioFragment
import com.gleysonabreu.cardatronicoempresa.fragment.HomeFragment
import com.gleysonabreu.cardatronicoempresa.fragment.ProfileFragment
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
            //FirstFragment Open
            var homeFragment = HomeFragment().newInstance();
            openFragment(homeFragment);
            fundoActivity.setBackgroundResource(R.color.bgHome);
        bottomNavigationView.setOnNavigationItemSelectedListener {
            item: MenuItem -> when(item.itemId){
                R.id.addCardapio -> {
                    toolbar.setTitle(R.string.addCardapio);
                    var addCardapioFragment = AddCardapioFragment().newInstance();
                    openFragment(addCardapioFragment);
                    fundoActivity.setBackgroundResource(R.color.cWhite);
                }
                R.id.profile -> {
                    toolbar.setTitle(R.string.navigationPerson);
                    var profileFragment = ProfileFragment().newInstance();
                    openFragment(profileFragment);
                    fundoActivity.setBackgroundResource(R.color.cWhite);
                }
                R.id.homeFragment -> {
                    toolbar.setTitle(R.string.controlPanel);
                    openFragment(homeFragment);
                    fundoActivity.setBackgroundResource(R.color.bgHome);
                }
            }

            return@setOnNavigationItemSelectedListener true;
        }
        //Settings FirebaseAuth
        autentication = SettingsFirebase.getFirebaseAuth();

        //Settings toolbar
        toolbar.setTitle(R.string.controlPanel);
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

    private fun openFragment(fragment: Fragment){

        var transaction: FragmentTransaction = supportFragmentManager.beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.commit();

    }
}
