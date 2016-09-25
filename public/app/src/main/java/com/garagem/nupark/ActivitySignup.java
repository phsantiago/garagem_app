package com.garagem.nupark;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.garagem.nupark.dto.RetornoDto;
import com.garagem.nupark.dto.UsuarioDto;
import com.garagem.nupark.service.UsuarioService;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;


public class ActivitySignup extends AppCompatActivity {

    UsuarioService usuarioService = new UsuarioService();
    Gson gson = new Gson();


  ImageView signupback;
  customfonts.MyTextView signin;
  customfonts.MyEditText senha;
  customfonts.MyEditText senha2;
  customfonts.MyEditText email;
  customfonts.MyEditText nome;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);

        signupback = (ImageView)findViewById(R.id.signupback);
        signin = (customfonts.MyTextView)findViewById(R.id.signin);
        senha = (customfonts.MyEditText)findViewById(R.id.senha);
        senha2 = (customfonts.MyEditText)findViewById(R.id.senha2);
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
                if (!senha.getText().toString().equals(senha2.getText().toString())){
                    Toast.makeText(getApplicationContext(), "As senhas não conferem", Toast.LENGTH_SHORT).show();
                    return;
                }


                UsuarioDto usuarioDto = new UsuarioDto();
                usuarioDto.setNome(nome.getText().toString());
                usuarioDto.setEmail(email.getText().toString());
                usuarioDto.setSenha(senha.getText().toString());

                usuarioService.registraUsuario(usuarioDto, getApplicationContext(), new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        if(e != null){
                            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        }else{
                            Log.d("retorno", result.toString());
                            RetornoDto retornoDto = gson.fromJson(result, RetornoDto.class);
                            Toast.makeText(getApplicationContext(), retornoDto.getMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }
                });



            }


        });
    }
}
