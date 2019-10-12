package com.gleysonabreu.cardatronicoempresa.helper

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.storage.StorageReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage


class SettingsFirebase {

    companion object Factory{

        private var referenceFirebase: DatabaseReference? = null;
        private var referenceAuth: FirebaseAuth? = null;
        private var referenceStorage: StorageReference? = null;

        public fun getFirebase(): DatabaseReference {

            if (referenceFirebase == null) {
                referenceFirebase = FirebaseDatabase.getInstance().reference;
            }

            return referenceFirebase as DatabaseReference;

        }

        public fun getIdUsuario(): String{
            var autentication: FirebaseAuth = getFirebaseAuth();
            return autentication.currentUser?.uid.toString();
        }


        public fun getFirebaseAuth(): FirebaseAuth {

            if (referenceAuth == null) {
                referenceAuth = FirebaseAuth.getInstance()
            }

            return referenceAuth as FirebaseAuth;

        }

        public fun getStorage(): StorageReference{
            if(referenceStorage == null){
                referenceStorage = FirebaseStorage.getInstance().reference;
            }
            return referenceStorage as StorageReference;
        }

    }



}