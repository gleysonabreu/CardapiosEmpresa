package com.gleysonabreu.cardatronicoempresa.activity

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.opengl.Visibility
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.gleysonabreu.cardatronicoempresa.R
import com.gleysonabreu.cardatronicoempresa.helper.SettingsFirebase
import com.gleysonabreu.cardatronicoempresa.model.Cardapio
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.Task
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import kotlinx.android.synthetic.main.activity_edit_menu.*
import kotlinx.android.synthetic.main.activity_edit_menu.buttonOpenCamera
import kotlinx.android.synthetic.main.activity_edit_menu.buttonOpenGallery
import kotlinx.android.synthetic.main.fragment_add_cardapio.*
import kotlinx.android.synthetic.main.toolbar.*
import java.io.ByteArrayOutputStream
import java.lang.Exception

class EditMenuActivity : AppCompatActivity() {

    private val SELECT_CAMERA = 100;
    private val SELECT_GALLERY = 200;
    private lateinit var bundle:Cardapio;
    private lateinit var builder: AlertDialog.Builder;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_menu)

        toolbar.setTitle("Edite");
        setSupportActionBar(toolbar);
        supportActionBar?.setDisplayHomeAsUpEnabled(true);
        bundle = intent.getSerializableExtra("dadosEditar") as Cardapio;
        builder = AlertDialog.Builder(this);
        builder.setView(R.layout.dialog_loading);
        builder.setCancelable(false);

        if (bundle != null) {

            editTextNameProductEdit.setText(bundle.nameitem)
            editTextDescriptionProductEdit.setText(bundle.descriptionItem);
            editTextPriceProductEdit.setText(bundle.priceitem.toString());
            editTextDisccountProductEdit.setText(bundle.discountPorcentItem.toString());

            if( bundle.photoItem != null ){
                val url: Uri = Uri.parse(bundle.photoItem);
                Glide.with(this@EditMenuActivity).load(url).into(imageMenuEdit);
            }

            buttonEditProduct.setOnClickListener {
                var dialog = builder.create();
                dialog.setCanceledOnTouchOutside(false);
                dialog.window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
                dialog.show();
                bundle.nameitem = editTextNameProductEdit.text.toString();
                bundle.descriptionItem = editTextDescriptionProductEdit.text.toString();
                bundle.priceitem = toDouble(editTextPriceProductEdit.text.toString());
                bundle.discountPorcentItem = toInt(editTextDisccountProductEdit.text.toString())

                if (bundle.update()) {
                    dialog.dismiss();
                    Toast.makeText(this@EditMenuActivity, "Item do cardápio atualizado com sucesso!", Toast.LENGTH_SHORT).show();
                    finish();
                }else{
                    dialog.dismiss();
                }

            }

            buttonOpenCamera.setOnClickListener {
                var i: Intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (i.resolveActivity(this@EditMenuActivity.packageManager) != null) {
                    startActivityForResult(i, SELECT_CAMERA);
                }
            }

            buttonOpenGallery.setOnClickListener {
                var i: Intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                if( i.resolveActivity( this@EditMenuActivity.packageManager ) != null ){
                    startActivityForResult(i, SELECT_GALLERY);
                }
            }


        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if( resultCode == Activity.RESULT_OK){
            var dialog = builder.create();
            dialog.setCanceledOnTouchOutside(false);
            dialog.window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
            dialog.show();
            try {
                    lateinit var bitmap: Bitmap;
                when(requestCode){
                    SELECT_CAMERA -> {
                        bitmap = data?.extras?.get("data") as Bitmap;
                    }
                    SELECT_GALLERY -> {
                        var localImageSelect: Uri = data!!.data;
                         bitmap = MediaStore.Images.Media.getBitmap(this@EditMenuActivity?.contentResolver, localImageSelect);
                    }
                }

                var baos: ByteArrayOutputStream = ByteArrayOutputStream();
                bitmap?.compress(Bitmap.CompressFormat.JPEG, 70, baos);
                var dadosImage: ByteArray? = baos.toByteArray();

                val storageReference = SettingsFirebase.getStorage();

                var imageRef: StorageReference = storageReference
                    .child("imagens")
                    .child("cardapio")
                    .child( bundle.idItemCardapio+ ".jpeg");

                var uploadTesk: UploadTask = imageRef.putBytes(dadosImage!!);

                uploadTesk.addOnFailureListener(object: OnFailureListener {
                    override fun onFailure(p0: Exception) {
                        dialog.dismiss();
                        Toast.makeText(this@EditMenuActivity, "Error ao enviar imagem", Toast.LENGTH_LONG).show();
                    }

                }).addOnSuccessListener(object: OnSuccessListener<UploadTask.TaskSnapshot> {
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
                                bundle.photoItem = downloadUri.toString();

                                if(bundle.update()){
                                    Toast.makeText(this@EditMenuActivity, "Sucesso ao alterar imagem.", Toast.LENGTH_LONG).show();
                                    dialog.dismiss();
                                    finish();
                                }

                            } else {
                                // Handle failures
                                // ...
                                dialog.dismiss();
                            }
                        }

                    }

                })



            }catch (e: Exception){
                e.printStackTrace();
            }

        }

    }


        private fun toDouble(c: String): Double {
            return c.toDouble();
        }

        private fun toInt(c: String): Int {
            return c.toInt();
        }

        override fun onBackPressed() {
            super.onBackPressed();
            finish();

        }

        override fun onOptionsItemSelected(item: MenuItem?): Boolean {

            when (item?.itemId) {
                android.R.id.home -> {
                    onBackPressed();
                }
            }

            return super.onOptionsItemSelected(item)
        }
}
