package com.example.admincollegeapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    private EditText user_email,user_pass;
    private Button login_btn;
    private SharedPreferences sharedPreference;
    private SharedPreferences.Editor editor;
    String email,pass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();

        user_email=findViewById(R.id.user_email);
        user_pass=findViewById(R.id.user_pass);
        login_btn=findViewById(R.id.login_btn);

     sharedPreference=this.getSharedPreferences("login",MODE_PRIVATE);
     editor = sharedPreference.edit();

     if(sharedPreference.getString("isLogin","false").equals("yes")){
         openDash();
     }




     login_btn.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {

             validateData();
         }
     });



    }

    private void validateData() {
        email = user_email.getText().toString();
        pass = user_pass.getText().toString();

        if (email.isEmpty()) {
            Toast.makeText(this, "Enter your email", Toast.LENGTH_SHORT).show();
        }
        else if (pass.isEmpty()) {
            Toast.makeText(this, "Enter your password", Toast.LENGTH_SHORT).show();
        }
        else if (email.equals("admin@gmail") && pass.equals("12345"))
        {
            editor.putString("isLogin","yes");
            editor.commit();
            openDash();
        }
        else{
            Toast.makeText(this, "Credential not matched", Toast.LENGTH_SHORT).show();
        }


    }

    private void openDash() {
        startActivity(new Intent(this,MainActivity.class));
        finish();
    }
}