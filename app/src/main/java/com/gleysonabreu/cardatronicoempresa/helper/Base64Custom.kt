package com.gleysonabreu.cardatronicoempresa.helper

import android.util.Base64

object Base64Custom {

    fun codificarBase64(category: String): String {

        return Base64.encodeToString(category.toByteArray(), Base64.DEFAULT)
            .replace("(\\n|\\r)".toRegex(), "")

    }

    fun decodificarBase64(category: String): String {

        return String(Base64.decode(category, Base64.DEFAULT))

    }

}
