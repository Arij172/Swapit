package com.example.swapit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;


import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class register extends AppCompatActivity {
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("users");

        final EditText fullName= findViewById(R.id.inputUsername);
        final EditText email=findViewById(R.id.inputEmail);
        final EditText phone = findViewById(R.id.inputPhone);
        final EditText password=findViewById(R.id.inputPassword);
        final EditText conpassword=findViewById(R.id.inputConformPassword);
        final Button registerBtn=findViewById(R.id.btnRegister);
        final TextView btn=findViewById(R.id.alreadyHaveAccount);


        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 final String fullNameText = fullName.getText().toString();
                final String emailText = email.getText().toString();
                final String phoneText = phone.getText().toString();
                final String passwordText = password.getText().toString();
                final String confirmPasswordText = conpassword.getText().toString();
                if (fullNameText.isEmpty() || emailText.isEmpty() || phoneText.isEmpty() || passwordText.isEmpty() || confirmPasswordText.isEmpty()) {
                    Toast.makeText(register.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                } else if (!passwordText.equals(confirmPasswordText)) {
                    Toast.makeText(register.this, "Passwords are not matching", Toast.LENGTH_SHORT).show();
                } else {
                    databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            // Check if phone number is already registered
                            if (snapshot.hasChild(phoneText)) {
                                Toast.makeText(register.this, "Phone number is already registered ", Toast.LENGTH_SHORT).show();
                            } else {
                                // Sending data to Firebase Realtime Database
                                DatabaseReference newUserRef = databaseReference.child(phoneText);
                                newUserRef.child("fullname").setValue(fullNameText);
                                newUserRef.child("email").setValue(emailText);
                                newUserRef.child("password").setValue(passwordText);
                                Toast.makeText(register.this, "User registered successfully", Toast.LENGTH_SHORT).show();
                                finish();
                            }


                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(register.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();


                        }


                    });



                }



            }

        });
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(register.this, Main2Activity2.class));
                finish();

            }
        });

    }
    // Method to encode email address for Firebase Database key
    private String encodeEmail(String email) {
        return email.replace(".", ",");
    }
}
