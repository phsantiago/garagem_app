package com.garagem.nupark.service;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.garagem.nupark.dto.UsuarioDto;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import org.json.JSONObject;

import system.Config;

/**
 * Created by Gabriel on 18/07/2016.
 */
public class UsuarioService {
    public void registraUsuario(UsuarioDto usuarioDto,Context applicationContext, FutureCallback futureCallback){
        JsonObject json = new JsonObject();
        json.addProperty("email",usuarioDto.getEmail());
        json.addProperty("nome",usuarioDto.getNome());
        json.addProperty("senha",usuarioDto.getSenha());

        Ion.with(applicationContext)
                .load("POST", Config.HTTP_HOST +"/usuario")
                .setHeader("Content-Type","application/json")
                .setJsonObjectBody(json)
                .asJsonObject()
                .setCallback(futureCallback);
    }
}
