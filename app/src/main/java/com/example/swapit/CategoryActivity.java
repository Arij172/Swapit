package com.example.swapit;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CategoryActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private CategoryAdapter categoryAdapter;
    private List<Article> articleList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        // Initialisation de la liste d'articles
        articleList = new ArrayList<>();

        // Initialisation du RecyclerView
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Référence à la base de données Firebase
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("articles");

        // Écouteur pour récupérer les données de la base de données
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                articleList.clear(); // Efface la liste actuelle d'articles
                // Parcours des données de la base de données et ajout dans la liste d'articles
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Article article = snapshot.getValue(Article.class);
                    articleList.add(article);
                }
                // Mise à jour de l'adaptateur avec les nouvelles données
                categoryAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Gestion des erreurs de la base de données
                Toast.makeText(CategoryActivity.this, "Erreur de chargement des données", Toast.LENGTH_SHORT).show();
            }
        });

        // Initialisation de l'adaptateur avec la liste d'articles
        categoryAdapter = new CategoryAdapter(articleList);

        // Définir l'adaptateur pour le RecyclerView
        recyclerView.setAdapter(categoryAdapter);
    }
}
