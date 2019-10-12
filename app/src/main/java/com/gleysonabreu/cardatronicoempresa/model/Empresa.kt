package com.gleysonabreu.cardatronicoempresa.model

import com.gleysonabreu.cardatronicoempresa.helper.SettingsFirebase
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.Exclude
import java.io.Serializable

class Empresa : Serializable {

    var id: String = "";
    var nomeEmpresa: String = "";
    var telefone: String = "";
    var tipoCozinha: String = "";
    var estado: String = "";
    var cidade: String = "";
    var bairro: String = "";
    var complemento: String = "";
    var cep: String = "";
    @get:Exclude var password: String = "";
    var email: String = ""

    fun save(){

        var firebaseRef: DatabaseReference = SettingsFirebase.getFirebase();
        var empresas: DatabaseReference = firebaseRef.child("empresas").child( id );
        empresas.setValue(this);

    }

}