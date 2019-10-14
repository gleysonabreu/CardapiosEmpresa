package com.gleysonabreu.cardatronicoempresa.model

import java.io.Serializable

class Cardapio : Serializable{

    var idItemCardapio: String = "";
    var idCategory: String = "";
    var nameitem: String = "";
    var priceitem: Double = 0.0;
    var descriptionItem: String = "";
    var discountPorcentItem: Int = 0;
    var photoItem: String = "";

    fun save(){

    }

}