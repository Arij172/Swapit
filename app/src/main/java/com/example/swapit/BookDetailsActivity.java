package com.example.swapit;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

import java.util.Arrays;
import java.util.List;

public class BookDetailsActivity extends AppCompatActivity {

    private FavoritesDatabaseHelper databaseHelper;
    private DataClass book;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product1);

        // Initialiser les vues
        ImageView imageView1 = findViewById(R.id.imageView1);
        TextView textView4 = findViewById(R.id.textView4);
        TextView textView6 = findViewById(R.id.textView6);
        TextView textView8 = findViewById(R.id.textView8);
        TextView textView10 = findViewById(R.id.textView10);
        Button button3 = findViewById(R.id.button3);

        // Initialiser le helper de la base de données
        databaseHelper = new FavoritesDatabaseHelper(this);

        // Récupérer les données de l'intent
        Intent intent = getIntent();
        if (intent != null) {
            String bookName = intent.getStringExtra("bookName");
            String description = intent.getStringExtra("description");
            String phone = intent.getStringExtra("phone");
            String location = intent.getStringExtra("location");
            String[] imageUrlsArray = intent.getStringArrayExtra("imageUrls");

            book = new DataClass(bookName, description, phone, location, Arrays.asList(imageUrlsArray), "", ""); // Création de l'objet DataClass avec les données reçues

            // Vérifier si l'objet DataClass n'est pas null
            if (book != null) {
                // Mettre à jour les vues avec les données de l'article
                textView4.setText(book.getName());
                textView6.setText(book.getDescription());
                textView8.setText(book.getPhone());
                textView10.setText(book.getLocation());

                // Récupérer les URL des images (seulement la première)
                List<String> imageUrls = book.getImageUrls();
                if (imageUrls != null && !imageUrls.isEmpty()) {
                    // Charger la première image dans imageView1 en utilisant Glide
                    Glide.with(this).load(imageUrls.get(0)).into(imageView1);
                } else {
                    // Afficher un message d'erreur si aucune URL d'image n'est disponible
                    Toast.makeText(this, "Aucune URL d'image disponible", Toast.LENGTH_SHORT).show();
                }
            } else {
                // Afficher un message d'erreur si l'objet DataClass est null
                Toast.makeText(this, "Données manquantes", Toast.LENGTH_SHORT).show();
            }
        } else {
            // Afficher un message d'erreur si l'intent est null
            Toast.makeText(this, "Intent null", Toast.LENGTH_SHORT).show();
        }

        // Gérer le clic sur le bouton d'ajout aux favoris
        button3.setOnClickListener(view -> {
            // Vérifier si l'objet book est null
            if (book != null) {
                // Récupérer les données de l'article
                String bookName = textView4.getText().toString();
                String description = textView6.getText().toString();
                String phoneNumber = textView8.getText().toString();
                String location = textView10.getText().toString();
                List<String> imageUrls = book.getImageUrls();

                // Créer un nouvel objet DataClass
                DataClass bookToAdd = new DataClass(bookName, description, phoneNumber, location, imageUrls, book.getCategory(), book.getImageUrl1());

                // Ajouter l'article aux favoris
                addToFavorites(bookToAdd);
            } else {
                // Afficher un message d'erreur si l'objet book est null
                Toast.makeText(this, "Objet book non initialisé", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Méthode pour ajouter l'article aux favoris
    private void addToFavorites(DataClass book) {
        // Récupérer une instance de la base de données en mode écriture
        SQLiteDatabase db = databaseHelper.getWritableDatabase();

        // Créer un objet ContentValues pour stocker les valeurs à insérer dans la base de données
        ContentValues values = new ContentValues();
        values.put(FavoritesDatabaseHelper.COLUMN_BOOK_NAME, book.getName());
        values.put(FavoritesDatabaseHelper.COLUMN_DESCRIPTION, book.getDescription());
        values.put(FavoritesDatabaseHelper.COLUMN_PHONE_NUMBER, book.getPhone());
        values.put(FavoritesDatabaseHelper.COLUMN_LOCATION, book.getLocation());
        values.put(FavoritesDatabaseHelper.COLUMN_IMAGE_URLS, book.getImageUrl1());

        // Insérer les valeurs dans la table des favoris
        long newRowId = db.insert(FavoritesDatabaseHelper.TABLE_FAVORITES, null, values);

        // Vérifier si l'insertion a réussi
        if (newRowId != -1) {
            // Afficher un message de succès
            Toast.makeText(this, "L'article a été ajouté aux favoris", Toast.LENGTH_SHORT).show();
        } else {
            // Afficher un message d'erreur en cas d'échec
            Toast.makeText(this, "Erreur lors de l'ajout aux favoris", Toast.LENGTH_SHORT).show();
        }

        // Fermer la connexion à la base de données
        db.close();
    }

    @Override
    protected void onDestroy() {
        // Fermer la base de données lorsque l'activité est détruite
        databaseHelper.close();
        super.onDestroy();
    }
}
