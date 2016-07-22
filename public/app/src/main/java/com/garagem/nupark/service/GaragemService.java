package com.garagem.nupark.service;

import android.content.Context;

import com.garagem.nupark.dto.GaragemDto;
import com.garagem.nupark.dto.PosicaoDto;
import com.garagem.nupark.dto.UsuarioDto;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import system.Config;

/**
 * Created by Gabriel on 18/07/2016.
 */
public class GaragemService {
    public void consultaGaragem(GaragemDto posicaoDto, Context applicationContext, FutureCallback futureCallback){
//        JsonObject json = new JsonObject();
//        json.addProperty("email",usuarioDto.getEmail());
//        json.addProperty("nome",usuarioDto.getNome());

        Ion.with(applicationContext)
                .load(Config.HTTP_HOST +"/garagem")
                .setHeader("Content-Type","application/json")
//                .setJsonObjectBody(json)
                .asJsonArray()
                .setCallback(futureCallback);
        }

}
