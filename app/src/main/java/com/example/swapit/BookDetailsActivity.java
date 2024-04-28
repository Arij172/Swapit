package com.example.swapit;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

import java.util.List;

public class BookDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.produit2);

        // Initialiser les vues
        ImageView imageView1 = findViewById(R.id.imageView1);
        ImageView imageView4 = findViewById(R.id.imageView4);
        ImageView imageView5 = findViewById(R.id.imageView5);
        ImageView imageView6 = findViewById(R.id.imageView6);
        TextView textView4 = findViewById(R.id.textView4);
        TextView textView6 = findViewById(R.id.textView6);
        TextView textView8 = findViewById(R.id.textView8);
        TextView textView10 = findViewById(R.id.textView10);

        // Récupérer les données de l'article à partir de l'intent
        Intent intent = getIntent();
        if (intent != null) {
            String bookName = intent.getStringExtra("bookName");
            String description = intent.getStringExtra("description");
            String phoneNumber = intent.getStringExtra("phone");
            String localisation = intent.getStringExtra("location");
            String[] imageUrls = intent.getStringArrayExtra("imageUrls"); // Modifier pour récupérer un tableau de chaînes

            // Mettre à jour les vues avec les données de l'article
            textView4.setText(bookName);
            textView6.setText(description);
            textView8.setText(phoneNumber);
            textView10.setText(localisation);

            // Charger les images à partir des URL dans les ImageView
            if (imageUrls != null && imageUrls.length > 0) {
                Glide.with(this).load(imageUrls[0]).into(imageView1);
                if (imageUrls.length > 1) {
                    Glide.with(this).load(imageUrls[1]).into(imageView4);
                }
                if (imageUrls.length > 2) {
                    Glide.with(this).load(imageUrls[2]).into(imageView5);
                }
            }
        }
    }

}

