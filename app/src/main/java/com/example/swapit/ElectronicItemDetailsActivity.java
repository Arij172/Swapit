package com.example.swapit;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;

public class ElectronicItemDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.produit2);

        // Initialiser les vues
        ImageView imageView1 =  findViewById(R.id.imageView1);
        ImageView imageView2 = findViewById(R.id.imageView4);
        ImageView imageView3 =  findViewById(R.id.imageView5);
        ImageView imageView6 = findViewById(R.id.imageView6);
        TextView textViewName = findViewById(R.id.textView4);
        TextView textViewDescription = findViewById(R.id.textView6);
        TextView textViewPhone =  findViewById(R.id.textView8);
        TextView textViewLocation =  findViewById(R.id.textView10);

        // Récupérer les données de l'article à partir de l'intent
        Intent intent = getIntent();
        if (intent != null) {
            String itemName = intent.getStringExtra("itemName");
            String description = intent.getStringExtra("description");
            String phone = intent.getStringExtra("phone");
            String location = intent.getStringExtra("location");
            String[] imageUrls = intent.getStringArrayExtra("imageUrls");

            // Mettre à jour les vues avec les données de l'article
            textViewName.setText(itemName);
            textViewDescription.setText(description);
            textViewPhone.setText(phone);
            textViewLocation.setText(location);

            // Charger les images à partir des URL dans les ImageView
            if (imageUrls != null && imageUrls.length > 0) {
                Glide.with(this).load(imageUrls[0]).into(imageView1);
                if (imageUrls.length > 1) {
                    Glide.with(this).load(imageUrls[1]).into(imageView2);
                }
                if (imageUrls.length > 2) {
                    Glide.with(this).load(imageUrls[2]).into(imageView3);
                }
            }
        }
    }
}

