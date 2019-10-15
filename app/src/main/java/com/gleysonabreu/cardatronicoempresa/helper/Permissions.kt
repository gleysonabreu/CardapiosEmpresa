package com.gleysonabreu.cardatronicoempresa.helper

import androidx.core.app.ActivityCompat
import java.nio.file.Files.size
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import android.os.Build
import android.app.Activity


object Permissions {

    fun validarPermissoes(
        permissoes: Array<String>,
        activity: Activity,
        requestCode: Int
    ): Boolean {

        if (Build.VERSION.SDK_INT >= 23) {

            val listaPermissoes: ArrayList<String> = ArrayList<String>();

            /*Percorre as permissões passadas,
            verificando uma a uma
            * se já tem a permissao liberada */
            for (permissao in permissoes) {
                val temPermissao = ContextCompat.checkSelfPermission(
                    activity,
                    permissao
                ) == PackageManager.PERMISSION_GRANTED
                if (!temPermissao) listaPermissoes.add(permissao)
            }

            /*Caso a lista esteja vazia, não é necessário solicitar permissão*/
            if (listaPermissoes.isEmpty()) return true
            val novasPermissoes = arrayOfNulls<String>(listaPermissoes.size)
            listaPermissoes.toTypedArray()

            //Solicita permissão
            ActivityCompat.requestPermissions(activity, novasPermissoes, requestCode)


        }

        return true

    }

}
