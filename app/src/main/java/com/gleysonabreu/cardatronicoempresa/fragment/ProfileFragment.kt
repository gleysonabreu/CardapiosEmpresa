package com.gleysonabreu.cardatronicoempresa.fragment


import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide

import com.gleysonabreu.cardatronicoempresa.R
import com.gleysonabreu.cardatronicoempresa.helper.Permission
import com.gleysonabreu.cardatronicoempresa.helper.SettingsFirebase
import com.gleysonabreu.cardatronicoempresa.model.Empresa
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.Task
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import kotlinx.android.synthetic.main.activity_singup.*
import kotlinx.android.synthetic.main.fragment_add_cardapio.view.*
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.fragment_profile.view.*
import java.io.ByteArrayOutputStream
import java.lang.Exception

/**
 * A simple [Fragment] subclass.
 */
class ProfileFragment : Fragment() {

    private lateinit var viewOf: View;
    private lateinit var empresa: Empresa;
    private lateinit var firebaseRef: DatabaseReference;
    private lateinit var empresaRef: DatabaseReference;
    private lateinit var eventListener: ValueEventListener;
    private val SELECT_CAMERA = 100;
    private lateinit var builder: AlertDialog.Builder;
    private val SELECT_GALLERY = 200;
    private val permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        viewOf = inflater.inflate(R.layout.fragment_profile, container, false);


        // Dialog loading
        builder = activity?.let { AlertDialog.Builder(it) }!!;
        builder.setView(R.layout.dialog_loading);
        builder.setCancelable(false);

        var idUser = SettingsFirebase.getIdUsuario();
        firebaseRef = SettingsFirebase.getFirebase();
        empresaRef = firebaseRef.child("empresas").child(idUser);
        eventListener = empresaRef.addValueEventListener(object: ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {

                Log.e("Error Categoria", "mensagem: "+p0.message.toString());

            }

            override fun onDataChange(p0: DataSnapshot) {
                empresa = p0.getValue(Empresa::class.java)!!;

                if(empresa != null){

                    editTextNameCompanyEdit.setText( empresa.nomeEmpresa );
                    editTextTypeKitchenEdit.setText( empresa.tipoCozinha );
                    editTextZipCodeEdit.setText(empresa.cep);
                    editTextStateEdit.setText(empresa.estado);
                    editTextCityEdit.setText(empresa.cidade);
                    editTextNeighborhoodEdit.setText(empresa.bairro);
                    editTextComplementEdit.setText(empresa.complemento);
                    editTextPhoneEdit.setText(empresa.telefone);

                    if(empresa.photoLink != null){

                        var url: Uri = Uri.parse(empresa.photoLink);
                        activity?.let { Glide.with(it).load(url).into(imageCompany) };

                    }

                }
            }

        })

        //SETTINGS buttons open gallary and camera
        viewOf.imageButtonOpenCamera.setOnClickListener {

            if(  ContextCompat.checkSelfPermission(activity!!, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ){
                Toast.makeText(activity, "O aplicativo precisa de permissão para acessar sua câmera", Toast.LENGTH_LONG).show();
                permissions();
            }else {

                var i: Intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (i.resolveActivity(activity?.packageManager) != null) {

                    startActivityForResult(i, SELECT_CAMERA);

                }
            }

        }

        viewOf.imageButtonOpenGallery.setOnClickListener {

            if(ContextCompat.checkSelfPermission(activity!!, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                Toast.makeText(activity, "O aplicativo precisa de permissão para acessar sua galeria de fotos", Toast.LENGTH_LONG).show();
                permissions();
            }else {

                var i: Intent =
                    Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                if (i.resolveActivity(activity?.packageManager) != null) {

                    startActivityForResult(i, SELECT_GALLERY);

                }
            }

        }

        viewOf.buttonUpdateCompany.setOnClickListener {

            empresa.nomeEmpresa = editTextNameCompanyEdit.text.toString();
            empresa.tipoCozinha = editTextTypeKitchenEdit.text.toString();
            empresa.cep = editTextZipCodeEdit.text.toString();
            empresa.estado = editTextStateEdit.text.toString();
            empresa.cidade = editTextCityEdit.text.toString();
            empresa.bairro = editTextNeighborhoodEdit.text.toString();
            empresa.complemento = editTextComplementEdit.text.toString();
            empresa.telefone = editTextPhoneEdit.text.toString();

            if(empresa.update()){
                Toast.makeText(activity, "Cadastro atualizado.", Toast.LENGTH_SHORT).show();
            }
        }
        return viewOf;
    }

     fun newInstance() : ProfileFragment{

        return ProfileFragment();

    }

    override fun onDestroy() {
        super.onDestroy()
        empresaRef.removeEventListener(eventListener);
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
                        bitmap = MediaStore.Images.Media.getBitmap(activity?.contentResolver, localImageSelect);
                    }
                }

                var baos: ByteArrayOutputStream = ByteArrayOutputStream();
                bitmap?.compress(Bitmap.CompressFormat.JPEG, 70, baos);
                var dadosImage: ByteArray? = baos.toByteArray();

                val storageReference = SettingsFirebase.getStorage();

                var imageRef: StorageReference = storageReference
                    .child("imagens")
                    .child("empresa")
                    .child( empresa.id+ ".jpeg");

                var uploadTesk: UploadTask = imageRef.putBytes(dadosImage!!);

                uploadTesk.addOnFailureListener(object: OnFailureListener {
                    override fun onFailure(p0: Exception) {
                        dialog.dismiss();
                        Toast.makeText(activity, "Error ao enviar imagem", Toast.LENGTH_LONG).show();
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
                                empresa.photoLink = downloadUri.toString();

                                if(empresa.update()){
                                    Toast.makeText(activity, "Sucesso ao alterar imagem.", Toast.LENGTH_LONG).show();
                                    dialog.dismiss();

                                    if(empresa.photoLink != null){

                                        var uri: Uri = Uri.parse(empresa.photoLink);
                                        activity?.let { Glide.with(it).load(uri).into(imageCompany) }

                                    }

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

    private fun permissions(){
        Permission.validPermissions(permissions, activity!!, 1);
    }


}
