package com.gleysonabreu.cardatronicoempresa.fragment


import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.appcompat.app.AlertDialog

import com.gleysonabreu.cardatronicoempresa.R
import com.gleysonabreu.cardatronicoempresa.activity.CategoryActivity
import com.gleysonabreu.cardatronicoempresa.helper.Base64Custom
import com.gleysonabreu.cardatronicoempresa.helper.Permissions
import com.gleysonabreu.cardatronicoempresa.helper.SettingsFirebase
import com.gleysonabreu.cardatronicoempresa.model.Cardapio
import com.gleysonabreu.cardatronicoempresa.model.Category
import com.google.android.gms.tasks.*
import com.google.android.material.chip.Chip
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.OnProgressListener
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import kotlinx.android.synthetic.main.fragment_add_cardapio.*
import kotlinx.android.synthetic.main.fragment_add_cardapio.view.*
import kotlinx.android.synthetic.main.fragment_add_cardapio.view.buttonOpenCategory
import java.io.ByteArrayOutputStream
import java.lang.Exception

/**
 * A simple [Fragment] subclass.
 */
class AddCardapioFragment : Fragment() {

    private lateinit var storageReference: StorageReference;
    private lateinit var referenceDatabase: DatabaseReference;
    private lateinit var categorys: DatabaseReference;
    private lateinit var viewOfLayout: View;
    private lateinit var eventListener: ValueEventListener;
    private val SELECT_CAMERA = 100;
    private val SELECT_GALLERY = 200;
    private val permissions = arrayOf<String>(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
    private var bitmap: Bitmap? = null;
    private var uri: String = "";

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        activity?.theme?.applyStyle(R.style.AppTheme2, true);
        viewOfLayout = inflater.inflate(R.layout.fragment_add_cardapio, container, false)


        // Valid permissions
        activity?.let { Permissions.validarPermissoes(permissions, it, 1) };

        //Open Add Category
        viewOfLayout.buttonOpenCategory.setOnClickListener {
            var i: Intent = Intent(activity, CategoryActivity::class.java);
            startActivity(i)
        }
            // Settings ChipGroup
            viewOfLayout.chip_group.isSingleSelection = true;


            //Settings firebase
            storageReference = SettingsFirebase.getStorage();
            referenceDatabase = SettingsFirebase.getFirebase();
            categorys = referenceDatabase.child("category");

            eventListener = categorys.addValueEventListener(
                object : ValueEventListener {
                    override fun onCancelled(p0: DatabaseError) {
                        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                    }

                    override fun onDataChange(p0: DataSnapshot) {
                        viewOfLayout.chip_group.removeAllViews();

                        for (cats in p0.children) {
                            var category = cats.getValue(Category::class.java);
                            val chip = Chip(activity);
                            chip.text = category?.nameCategory;
                            chip.setChipBackgroundColorResource(R.color.colorSplash);
                            chip.setTextColor(Color.WHITE);
                            chip.chipCornerRadius = 16F;
                            chip.isCheckable = true;

                            viewOfLayout.chip_group.addView(chip);
                        }

                    }

                });

            viewOfLayout.buttonTest.setOnClickListener {

                sendCardapio();

            }


        //SETTINGS buttons open gallary and camera
        viewOfLayout.buttonOpenCamera.setOnClickListener {

            var i: Intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if( i.resolveActivity( activity?.packageManager ) != null ){

                startActivityForResult(i, SELECT_CAMERA);

            }

        }

        viewOfLayout.buttonOpenGallery.setOnClickListener {

            var i: Intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            if( i.resolveActivity( activity?.packageManager ) != null ){

                startActivityForResult(i, SELECT_GALLERY);

            }

        }


            // Inflate the layout for this fragment
            return viewOfLayout;
        }

        fun newInstance(): AddCardapioFragment {
            return AddCardapioFragment();
        }

    private fun pickChipGroup() : String {

        var chipGroup = viewOfLayout.chip_group;

        var count: Int = chipGroup.childCount;
        if (count != 0) {
            var i: Int = 0;
            while (i < count) {
                var chip: Chip = chipGroup.getChildAt(i) as Chip;
                if (chip.isChecked == true) {
                    return Base64Custom.codificarBase64(chip.text.toString());
                }
                i++;
            }
        }

        return "no";
    }

    private fun sendCardapio(){

        var nameItem = editTextNameProduct.text.toString();
        var priceItem = editTextPriceProduct.text.toString();
        var descriptionProduct = editTextDescriptionProduct.text.toString();
        var disccountProduct = editTextDisccountProduct.text.toString();
        var chipSelected = pickChipGroup();
        var newItemMenu: Cardapio = Cardapio();

        if( !nameItem.isEmpty() && !priceItem.isEmpty() && !descriptionProduct.isEmpty() && !disccountProduct.isEmpty() && bitmap != null && chipSelected != "no" ){

                var baos: ByteArrayOutputStream = ByteArrayOutputStream();
                bitmap?.compress(Bitmap.CompressFormat.JPEG, 70, baos);
                var dadosImage: ByteArray? = baos.toByteArray();


            var imageRef: StorageReference = storageReference
                .child("imagens")
                .child("cardapio")
                .child(newItemMenu.idItemCardapio + ".jpeg");

            var uploadTesk: UploadTask = imageRef.putBytes(dadosImage!!);


            uploadTesk.addOnFailureListener(object: OnFailureListener{
                override fun onFailure(p0: Exception) {
                    Toast.makeText(activity, "Error ao enviar imagem", Toast.LENGTH_LONG).show();
                }

            }).addOnSuccessListener(object:OnSuccessListener<UploadTask.TaskSnapshot>{
                override fun onSuccess(p0: UploadTask.TaskSnapshot?) {

                    val urlTask = uploadTesk.continueWithTask(Continuation<UploadTask.TaskSnapshot, Task<Uri>> { task ->
                        if (!task.isSuccessful) {
                            task.exception?.let {
                                throw it
                            }
                        }
                        return@Continuation imageRef.downloadUrl
                    }).addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val downloadUri = task.result
                            newItemMenu.photoItem = downloadUri.toString();
                            newItemMenu.nameitem = nameItem;
                            newItemMenu.priceitem = priceItem.toDouble();
                            newItemMenu.descriptionItem = descriptionProduct;
                            newItemMenu.discountPorcentItem = disccountProduct.toInt();
                            newItemMenu.idCategory = chipSelected;

                            //Save menu
                            if ( newItemMenu.save() ){
                                Toast.makeText(activity, "Sucesso ao cadastrar produto!", Toast.LENGTH_LONG).show();
                                editTextNameProduct.setText("");
                                editTextPriceProduct.setText("");
                                editTextDescriptionProduct.setText("");
                                editTextDisccountProduct.setText("");
                                bitmap = null;
                            }

                        } else {
                            // Handle failures
                            // ...
                        }
                    }

                }

            })

        }else{
            Toast.makeText(activity, "Preencha todos os dados.", Toast.LENGTH_LONG).show();
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if( resultCode == Activity.RESULT_OK){

            try {

                when(requestCode){
                    SELECT_CAMERA -> {
                        bitmap = data?.extras?.get("data") as Bitmap;
                    }
                    SELECT_GALLERY -> {
                        var localImageSelect: Uri = data!!.data;
                        bitmap = MediaStore.Images.Media.getBitmap(activity?.contentResolver, localImageSelect);
                    }
                }


            }catch (e: Exception){
                e.printStackTrace();
            }

        }

    }

    override fun onDestroy() {
        super.onDestroy()
        categorys.removeEventListener(eventListener);
    }


}
