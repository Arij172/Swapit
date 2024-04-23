package com.example.swapit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
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

public class Main2Activity2 extends AppCompatActivity {

    DatabaseReference usersRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        final EditText phone = findViewById(R.id.inputPhone);
        final EditText password = findViewById(R.id.inputPassword);

        final Button loginBtn = findViewById(R.id.btnlogin);
        final TextView registerNow = findViewById(R.id.textViewSignUp);

        // Reference to "users" node in Firebase Realtime Database
        usersRef = FirebaseDatabase.getInstance().getReference().child("users");

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String phoneTxt = phone.getText().toString();
                final String passwordTxt = password.getText().toString();

                if (phoneTxt.isEmpty() || passwordTxt.isEmpty()) {
                    Toast.makeText(Main2Activity2.this, "Please enter your email and password", Toast.LENGTH_SHORT).show();
                } else {
                    // Check if the entered credentials exist in the database
                    usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.child(phoneTxt).exists()) {
                                // User exists, check password
                                String storedPassword = dataSnapshot.child(phoneTxt).child("password").getValue(String.class);
                                if (passwordTxt.equals(storedPassword)) {
                                    // Password matches, login successful
                                    Intent intent = new Intent(Main2Activity2.this, MainActivity3.class);
                                    startActivity(intent);
                                    finish(); // Close current activity
                                } else {
                                    // Password does not match
                                    Toast.makeText(Main2Activity2.this, "Incorrect password", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                // User does not exist
                                Toast.makeText(Main2Activity2.this, "User not found", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Toast.makeText(Main2Activity2.this, "Database error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

        registerNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Main2Activity2.this, register.class));
            }
        });
    }
}
