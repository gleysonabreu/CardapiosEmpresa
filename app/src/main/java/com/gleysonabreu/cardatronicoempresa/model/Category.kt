package com.gleysonabreu.cardatronicoempresa.model

import com.gleysonabreu.cardatronicoempresa.helper.SettingsFirebase
import com.google.firebase.database.DatabaseReference
import java.io.Serializable

class Category : Serializable {

    var id: String = "";
    var nameCategory: String = "";

    fun saveCategory(){

        var firebaseRef: DatabaseReference = SettingsFirebase.getFirebase();
        var categorys: DatabaseReference = firebaseRef.child("category").child( id );
        categorys.setValue(this);

    }

}