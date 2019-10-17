package com.gleysonabreu.cardatronicoempresa.activity

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import com.gleysonabreu.cardatronicoempresa.R
import com.gleysonabreu.cardatronicoempresa.model.Cardapio
import kotlinx.android.synthetic.main.activity_edit_menu.*
import kotlinx.android.synthetic.main.toolbar.*

class EditMenuActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_menu)

        toolbar.setTitle("Edite");
        setSupportActionBar(toolbar);
        supportActionBar?.setDisplayHomeAsUpEnabled(true);
        var bundle: Cardapio = intent.getSerializableExtra("dadosEditar") as Cardapio;

            if(bundle != null) {

                editTextNameProductEdit.setText(bundle.nameitem)
                editTextDescriptionProductEdit.setText(bundle.descriptionItem);
                editTextPriceProductEdit.setText(bundle.priceitem.toString());
                editTextDisccountProductEdit.setText(bundle.discountPorcentItem.toString());

                buttonEditProduct.setOnClickListener {

                    bundle.nameitem = editTextNameProductEdit.text.toString();
                    bundle.descriptionItem = editTextDescriptionProductEdit.text.toString();
                    bundle.priceitem = toDouble(editTextPriceProductEdit.text.toString());
                    bundle.discountPorcentItem = toInt(editTextDisccountProductEdit.text.toString())

                    if( bundle.update() ){

                        finish();

                    }

                }

            }

    }

    fun toDouble(c: String): Double{
        return c.toDouble();
    }
    fun toInt(c: String): Int{
        return c.toInt();
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
