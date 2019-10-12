package com.gleysonabreu.cardatronicoempresa.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.gleysonabreu.cardatronicoempresa.R
import com.gleysonabreu.cardatronicoempresa.helper.SettingsFirebase
import com.gleysonabreu.cardatronicoempresa.model.Empresa
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.*
import kotlinx.android.synthetic.main.activity_singup.*
import kotlinx.android.synthetic.main.toolbar.*
import java.lang.Exception

class SingupActivity : AppCompatActivity() {
    private lateinit var autentication: FirebaseAuth;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_singup)

        autentication = SettingsFirebase.getFirebaseAuth();
        toolbar.title = "Cadastre-se";
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true);

        buttonSignup.setOnClickListener {
            var email: String = editTextEmailSignup.text.toString();
            var password: String = editTextPasswordSignup.text.toString();
            var empresaNome: String = editTextNomeEmpresa.text.toString();
            var cep: String = editTextCep.text.toString();
            var estado: String = editTextEstado.text.toString();
            var cidade: String = editTextCidade.text.toString();
            var bairro: String = editTextBairro.text.toString();
            var complemento: String = editTextComplemento.text.toString();
            var tipoCozinha: String = editTextTipoCozinha.text.toString();
            var telefone: String = editTextTelefoneEmpresa.text.toString();

            if( !email.isEmpty() && !password.isEmpty() && !empresaNome.isEmpty() && !cep.isEmpty() && !estado.isEmpty() && !cidade.isEmpty()
                && !bairro.isEmpty() && !complemento.isEmpty() && !tipoCozinha.isEmpty() && !telefone.isEmpty()){

                var empresa = Empresa();
                empresa.email = email;
                empresa.password = password;
                empresa.nomeEmpresa = empresaNome;
                empresa.cep = cep;
                empresa.estado = estado;
                empresa.cidade = cidade;
                empresa.bairro = cidade;
                empresa.complemento = complemento;
                empresa.tipoCozinha = tipoCozinha;
                empresa.telefone = telefone;

                signupUser(empresa);



            }else{
                Toast.makeText(
                    this,
                    R.string.messageSignupNeddData,
                    Toast.LENGTH_LONG
                ).show()
            }
        }

    }

    fun signupUser( empresa: Empresa ){

        this.autentication.createUserWithEmailAndPassword(empresa.email, empresa.password).addOnCompleteListener{
            
            task: Task<AuthResult> ->

            if(task.isSuccessful){

                var idUsuario: String = task.getResult()?.user!!.uid;
                empresa.id = idUsuario;
                empresa.save();

                Toast.makeText(
                    this,
                    R.string.accountCreated,
                    Toast.LENGTH_LONG
                ).show();

                var intent: Intent = Intent(this, MainActivity::class.java);
                startActivity(intent);
                finish();

            }else{

                var error: String = "";
                try {
                    throw task.exception!!;
                }catch (e: FirebaseAuthWeakPasswordException){
                    error = "Digite uma senha mais forte.";
                }catch (e: FirebaseAuthInvalidCredentialsException){
                    error = "Por favor, digite um email válido.";
                }catch (e: FirebaseAuthUserCollisionException){
                    error = "Esta conta já foi cadastrada.";
                }catch (e: Exception){
                    error = "ao cadastrar usuário: " + e.message;
                    e.printStackTrace();
                }

                Toast.makeText(
                    this,
                    "Erro: "+ error,
                    Toast.LENGTH_LONG
                ).show()

            }
        }

    }
}
