package com.gleysonabreu.cardatronicoempresa.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import com.gleysonabreu.cardatronicoempresa.R
import com.gleysonabreu.cardatronicoempresa.helper.Base64Custom
import com.gleysonabreu.cardatronicoempresa.helper.SettingsFirebase
import com.gleysonabreu.cardatronicoempresa.model.Category
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_category.*
import kotlinx.android.synthetic.main.toolbar.*



class CategoryActivity : AppCompatActivity() {

    private lateinit var referenceDatabase: DatabaseReference;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category)

        // Start Firebase
        referenceDatabase = SettingsFirebase.getFirebase();

        //Settings Toolbar
        toolbar.setTitle(R.string.addCategory);
        setSupportActionBar(toolbar);
        supportActionBar?.setDisplayHomeAsUpEnabled(true);


        // Add Category

        buttonAddCategory.setOnClickListener {

            var nameCategory: String = editTextNameCategory.text.toString();

            if( !nameCategory.equals("") and !nameCategory.equals(" ") ){

                var nameCategoryBase64 = Base64Custom.codificarBase64(nameCategory);
                var categoryRef = referenceDatabase.child("category").child(nameCategoryBase64);

                categoryRef.addListenerForSingleValueEvent(object: ValueEventListener {
                    override fun onCancelled(p0: DatabaseError) {
                        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                    }

                    override fun onDataChange(p0: DataSnapshot) {

                            if( !p0.exists() ){

                                var newCategory: Category = Category();
                                newCategory.nameCategory = nameCategory;
                                newCategory.id = nameCategoryBase64;
                                newCategory.saveCategory();
                                finish();

                                Toast.makeText(
                                    this@CategoryActivity,
                                    "Categoria adicionada com sucesso!",
                                    Toast.LENGTH_SHORT
                                ).show();
                            }else{

                                Toast.makeText(
                                    this@CategoryActivity,
                                    "Erro: essa categoria já existe",
                                    Toast.LENGTH_SHORT
                                ).show();

                            }

                    }


                });

            }else{

                Toast.makeText(
                    this@CategoryActivity,
                    "Digite um nome para a categoria.",
                    Toast.LENGTH_SHORT
                ).show();

            }

        }

    }

    override fun onBackPressed() {
        super.onBackPressed();
        finish();

    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        when(item?.itemId){
            android.R.id.home -> {
                onBackPressed();
            }
        }

        return super.onOptionsItemSelected(item)
    }
}
