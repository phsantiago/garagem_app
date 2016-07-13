package com.garagem.nupark;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import customfonts.MyEditText;
import customfonts.MyTextView;

import static com.google.android.gms.common.api.GoogleApiClient.*;


public class ActivitySignin extends AppCompatActivity {

    ImageView signinback;
    View underbar, underbar2;
    MyTextView signin;
    MyEditText username;
    MyEditText password;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signin);

        signinback = (ImageView) findViewById(R.id.signinback);
        underbar = findViewById(R.id.underbar);
        underbar2 = findViewById(R.id.underbar2);
        signin = (MyTextView) findViewById(R.id.signin);
        username = (MyEditText) findViewById(R.id.username);
        password = (MyEditText) findViewById(R.id.password);

        signinback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(ActivitySignin.this, MainActivity.class);
                startActivity(it);
            }
        });


        signin.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Ion.with(getApplicationContext())
                        .load("http://192.168.1.37:8080/nupark_api/signin")
                        .setBodyParameter("usuario", username.getText().toString())
                        .setBodyParameter("senha", password.getText().toString())
                        .asString()
                        .setCallback(new FutureCallback<String>() {
                            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                            @Override
                            public void onCompleted(Exception e, String result) {


                                JsonObject resobj = new Gson().fromJson(result, JsonObject.class);
                                System.out.println(result);

                                if (resobj.get("valid").getAsDouble() == 1) {
//                                    Intent it = new Intent(ActivitySignin.this, home.class);
//                                    it.putExtra("nome", resobj.get("nome").toString());
//                                    startActivity(it);
                                }else{
                                    Context context = getApplicationContext();
                                    CharSequence text = "Usuário não encontrado!";
                                    int duration = Toast.LENGTH_SHORT;

                                    Toast toast = Toast.makeText(context, text, duration);
                                    toast.show();
                                }
                            }
                        });


            }
        });


        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new Builder(this).addApi(AppIndex.API).build();
    }

//    @Override
//    public void onStart() {
//        super.onStart();
//
//        // ATTENTION: This was auto-generated to implement the App Indexing API.
//        // See https://g.co/AppIndexing/AndroidStudio for more information.
//        client.connect();
//        Action viewAction = Action.newAction(
//                Action.TYPE_VIEW, // TODO: choose an action type.
//                "ActivitySignin Page", // TODO: Define a title for the content shown.
//                // TODO: If you have web page content that matches this app activity's content,
//                // make sure this auto-generated web page URL is correct.
//                // Otherwise, set the URL to null.
//                Uri.parse("http://host/path"),
//                // TODO: Make sure this auto-generated app URL is correct.
//                Uri.parse("android-app://com.garagem.nupark.com.garagem.nupark/http/host/path")
//        );
//        AppIndex.AppIndexApi.start(client, viewAction);
//    }
//
//    @Override
//    public void onStop() {
//        super.onStop();
//
//        // ATTENTION: This was auto-generated to implement the App Indexing API.
//        // See https://g.co/AppIndexing/AndroidStudio for more information.
//        Action viewAction = Action.newAction(
//                Action.TYPE_VIEW, // TODO: choose an action type.
//                "ActivitySignin Page", // TODO: Define a title for the content shown.
//                // TODO: If you have web page content that matches this app activity's content,
//                // make sure this auto-generated web page URL is correct.
//                // Otherwise, set the URL to null.
//                Uri.parse("http://host/path"),
//                // TODO: Make sure this auto-generated app URL is correct.
//                Uri.parse("android-app://com.garagem.nupark.com.garagem.nupark/http/host/path")
//        );
//        AppIndex.AppIndexApi.end(client, viewAction);
//        client.disconnect();
//    }
}


