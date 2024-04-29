package com.example.swapit;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.database.*;
import java.util.ArrayList;
import java.util.List;

public class ElectronicsActivity extends AppCompatActivity implements ElectronicsAdapter.OnItemClickListener {
    private DatabaseReference databaseReference;
    private List<DataClass> electronicsList;
    private ElectronicsAdapter electronicsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_electronics);

        RecyclerView recyclerView = findViewById(R.id.searchResultsListView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        electronicsList = new ArrayList<>();
        electronicsAdapter = new ElectronicsAdapter(electronicsList);
        recyclerView.setAdapter(electronicsAdapter);

        // Définir l'écouteur de clic sur l'adaptateur
        electronicsAdapter.setOnItemClickListener(this);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Electronics");

        fetchElectronicsDataFromFirebase();
    }

    private void fetchElectronicsDataFromFirebase() {
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                electronicsList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    DataClass electronicItem = dataSnapshot.getValue(DataClass.class);
                    electronicsList.add(electronicItem);
                }
                electronicsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ElectronicsActivity.this, "Failed to fetch Electronics data: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onItemClick(int position) {
        // Récupérer l'article cliqué
        DataClass clickedElectronicItem = electronicsList.get(position);
        // Passer les données de l'article à l'activité ElectronicItemDetailsActivity
        Intent intent = new Intent(this, ElectronicItemDetailsActivity.class);
        intent.putExtra("itemName", clickedElectronicItem.getName());
        intent.putExtra("description", clickedElectronicItem.getDescription());
        intent.putExtra("phone", clickedElectronicItem.getPhone());
        intent.putExtra("location", clickedElectronicItem.getLocation());
        intent.putExtra("imageUrls", clickedElectronicItem.getImageUrls().toArray(new String[0])); // Convertir la liste en un tableau
        // Démarrer l'activité
        startActivity(intent);
    }
}
