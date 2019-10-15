package com.gleysonabreu.cardatronicoempresa.model

import android.net.Uri
import android.widget.Toast
import com.gleysonabreu.cardatronicoempresa.helper.SettingsFirebase
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.storage.StorageReference
import java.io.Serializable
import java.lang.Exception

class Cardapio : Serializable{

    var idItemCardapio: String = "";
    var idCategory: String = "";
    var nameitem: String = "";
    var priceitem: Double = 0.0;
    var descriptionItem: String = "";
    var discountPorcentItem: Int = 0;
    var photoItem: String = "";

    fun save() : Boolean{
        var firebaseRef = SettingsFirebase.getFirebase();
        var cardapioRef = firebaseRef.child("cardapio")
            .child(idItemCardapio);
            cardapioRef.setValue(this);
            return true;
    }


    init{

        var firebaseRef = SettingsFirebase.getFirebase();
        var cardapioRef = firebaseRef.child("cardapio");
        var idCardapio: String? = cardapioRef.push().key;
        idItemCardapio = idCardapio.toString();

    }

}