package com.example.fakedcard;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.mongodb.stitch.android.core.Stitch;
import com.mongodb.stitch.android.core.StitchAppClient;
import com.mongodb.stitch.android.core.auth.StitchUser;
import com.mongodb.stitch.android.core.auth.providers.userapikey.UserApiKeyAuthProviderClient;
import com.mongodb.stitch.android.services.mongodb.remote.RemoteMongoClient;
import com.mongodb.stitch.core.auth.providers.userapikey.models.UserApiKey;
import com.mongodb.stitch.core.auth.providers.userpassword.UserPasswordCredential;

public class Login extends AppCompatActivity {

    StitchAppClient client = Stitch.getDefaultAppClient();
    EditText email,pass;
    Button emailbut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        email = findViewById(R.id.email);
        pass = findViewById(R.id.pass);
        emailbut = findViewById(R.id.Loginbut);
        RemoteMongoClient mongoClient = client.getServiceClient(RemoteMongoClient.factory, "AndroidBlog");

        emailbut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                emailLogin();
            }
        });
    }

    private void emailLogin(){
        UserPasswordCredential email_credential = new UserPasswordCredential(email.getText().toString(),pass.getText().toString());
        client.getAuth().loginWithCredential(email_credential).addOnCompleteListener(new OnCompleteListener<StitchUser>() {
            @Override
            public void onComplete(@NonNull Task<StitchUser> task) {
                if (task.isSuccessful()){
                    Toast.makeText(Login.this, "登入成功", Toast.LENGTH_LONG).show();
                    UserApiKeyAuthProviderClient apiClient = Stitch.getDefaultAppClient().getAuth().getProviderClient(UserApiKeyAuthProviderClient.factory);
                    apiClient.createApiKey("apikey").addOnCompleteListener(new OnCompleteListener<UserApiKey>() {
                        @Override
                        public void onComplete(@NonNull Task<UserApiKey> task) {
                            if(task.isSuccessful()){
                                SharedPreferences prefs = getSharedPreferences("apikey",MODE_PRIVATE);
                                prefs.edit().putString("apikey",task.getResult().getKey()).commit();
                                Toast.makeText(Login.this, "加入apikey", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(Login.this,Signup.class);
                                startActivity(intent);
                            }
                            else{
                                Toast.makeText(Login.this, task.getException().toString(), Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(Login.this,Signup.class);
                                startActivity(intent);
                            }
                        }
                    });
                }
                else{
                    Toast.makeText(Login.this, "登入失敗", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
