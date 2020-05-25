package com.example.fakedcard;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.mongodb.stitch.android.core.Stitch;
import com.mongodb.stitch.android.core.StitchAppClient;
import com.mongodb.stitch.android.core.auth.providers.userpassword.UserPasswordAuthProviderClient;
import com.mongodb.stitch.android.services.mongodb.remote.RemoteMongoClient;
import com.mongodb.stitch.android.services.mongodb.remote.RemoteMongoCollection;
import com.mongodb.stitch.core.auth.providers.userapikey.UserApiKeyCredential;

import org.bson.Document;

public class Signup extends AppCompatActivity {

    StitchAppClient client = Stitch.getDefaultAppClient();
    EditText email,pass;
    Button emailbut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        email = findViewById(R.id.email);
        pass = findViewById(R.id.pass);
        emailbut = findViewById(R.id.emailbut);
        RemoteMongoClient mongoClient = client.getServiceClient(RemoteMongoClient.factory, "AndroidBlog");
        SharedPreferences prefs = getSharedPreferences("apikey",MODE_PRIVATE);
        System.out.println(prefs.getString("apikey",""));

        emailbut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerEmail();
            }
        });
    }

        private void registerEmail(){

            if(email.getText().toString().isEmpty() && pass.getText().toString().isEmpty()){
                Toast.makeText(getApplicationContext(),"Please Filled in Email and Password",Toast.LENGTH_SHORT).show();
            }
            else {
                UserPasswordAuthProviderClient emailClient = client.getAuth().getProviderClient(UserPasswordAuthProviderClient.factory);
                emailClient.registerWithEmail(email.getText().toString(), pass.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(Signup.this, "收取Email", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(Signup.this, "註冊失敗", Toast.LENGTH_LONG).show();
                        }
                    };
                });
            }

        }
    }

