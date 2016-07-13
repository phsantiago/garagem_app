package com.garagem.nupark;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import system.Config;


public class ActivitySignup extends AppCompatActivity {

  ImageView signupback;
  customfonts.MyTextView signin;
  customfonts.MyEditText usuario;
  customfonts.MyEditText senha;
  customfonts.MyEditText email;
  customfonts.MyEditText nome;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);

        signupback = (ImageView)findViewById(R.id.signupback);
        signin = (customfonts.MyTextView)findViewById(R.id.signin);
        usuario = (customfonts.MyEditText)findViewById(R.id.usuario);
        senha = (customfonts.MyEditText)findViewById(R.id.senha);
        email = (customfonts.MyEditText)findViewById(R.id.email);
        nome = (customfonts.MyEditText)findViewById(R.id.nome);

        signupback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(ActivitySignup.this, MainActivity.class);
                startActivity(it);

            }
        });

        signin.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Ion.with(getApplicationContext())
                        .load(Config.HTTP_HOST + "/signup")
                        .setBodyParameter("usuario", usuario.getText().toString())
                        .setBodyParameter("senha", senha.getText().toString())
                        .setBodyParameter("email", email.getText().toString())
                        .setBodyParameter("nome", senha.getText().toString())
                        .asString()
                        .setCallback(new FutureCallback<String>() {
                            @Override
                            public void onCompleted(Exception e, String result) {
                                Log.d("retorno", result);
                                if(false){
//                                    Intent it = new Intent(ActivitySignup.this,home.class);
//                                    it.putExtra("nome","value");
//                                    startActivity(it);
                                }else{
                                    Context context = getApplicationContext();
                                    CharSequence text = "Cadastrado!";
                                    int duration = Toast.LENGTH_SHORT;

                                    Toast toast = Toast.makeText(context, text, duration);
                                    toast.show();
                                }
                            }
                        });


            }
        });
    }
}
