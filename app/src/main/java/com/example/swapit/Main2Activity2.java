package com.example.swapit;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;


import android.content.Intent;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Main2Activity2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        final EditText mail=findViewById(R.id.inputEmail);
        final EditText  password=findViewById(R.id.inputPassword);
        final Button loginBtn =findViewById(R.id.btnlogin);
        final TextView registerNow =findViewById(R.id.textViewSignUp);


        loginBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                final String phoneTxt=mail.getText().toString();
                final  String passwordTxt=password.getText().toString();


                if (phoneTxt.isEmpty() || passwordTxt.isEmpty()){
                    Toast.makeText(Main2Activity2.this,"Please enter your Email or password",Toast.LENGTH_SHORT).show();
                }
                else{

            }

            }
        });



        registerNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Main2Activity2.this,register.class));
            }

        });
    }
}