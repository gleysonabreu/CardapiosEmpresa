package com.gleysonabreu.cardatronicoempresa.adapter

import androidx.appcompat.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.provider.Settings
import android.view.ContextThemeWrapper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.gleysonabreu.cardatronicoempresa.R
import com.gleysonabreu.cardatronicoempresa.helper.SettingsFirebase
import com.gleysonabreu.cardatronicoempresa.model.Cardapio
import com.google.firebase.database.DatabaseReference
import kotlinx.android.synthetic.main.itens_cardapio.view.*

class AdapterCardapio (private val cardapio: ArrayList<Cardapio>, private val context: Context) : RecyclerView.Adapter<AdapterCardapio.CardapioViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardapioViewHolder {

        val view: View = LayoutInflater.from(context).inflate(R.layout.itens_cardapio, parent, false);
        return CardapioViewHolder(view);

    }

    override fun getItemCount(): Int {
        return cardapio.size;
    }

    override fun onBindViewHolder(holder: CardapioViewHolder, position: Int) {
        holder.bindItems(cardapio[position]);
    }


    class CardapioViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val nomeProduto = itemView.textTitle;
        val descricaoProduto = itemView.textDescription;
        val fotoProduto = itemView.imageProduct;
        val precoProduto = itemView.textPrice;
        var builder: AlertDialog.Builder = AlertDialog.Builder(ContextThemeWrapper(itemView.context, R.style.AlertDialogCustom));


        fun bindItems(cardapio: Cardapio){

            nomeProduto.text = cardapio.nameitem;
            descricaoProduto.text = cardapio.descriptionItem;
            precoProduto.text = "R$ " + cardapio.priceitem;


            if ( cardapio.photoItem != null ){
                var url: Uri = Uri.parse(cardapio.photoItem);
                Glide.with(itemView.context).load(url).into(fotoProduto);
            }

            itemView.imageButtonEdit.setOnClickListener {
                Toast.makeText(itemView.context, "Você está querendo editar: "+nomeProduto.text.toString(), Toast.LENGTH_SHORT).show();
            }

            itemView.imageButtonDelete.setOnClickListener {

                builder.setTitle("Excluindo item do cardápio..");
                builder.setMessage("Você tem certeza que deseja excluir o "+nomeProduto.text.toString()+"?");
                builder.setPositiveButton("Sim", DialogInterface.OnClickListener(){ dialogInterface: DialogInterface, i: Int ->
                    deleteCardapio(cardapio.idItemCardapio)
                });
                builder.setNegativeButton("Não", null);
                val alertDialog: AlertDialog = builder.create();
                alertDialog.window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
                alertDialog.show();
            }

        }

        private fun deleteCardapio(idCardapio: String){
            val firebaseref: DatabaseReference = SettingsFirebase.getFirebase();
            var cardapioRef = firebaseref.child("cardapio").child(idCardapio);
            cardapioRef.setValue(null);

            Toast.makeText(itemView.context, nomeProduto.text.toString()+" foi excluído com sucesso!", Toast.LENGTH_SHORT).show();

        }
    }
}
