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

public class CarsActivity extends AppCompatActivity implements CarsAdapter.OnItemClickListener {
    private DatabaseReference databaseReference;
    private List<DataClass> carsList;
    private CarsAdapter carsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cars);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        carsList = new ArrayList<>();
        carsAdapter = new CarsAdapter(carsList);
        recyclerView.setAdapter(carsAdapter);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Cars");

        fetchCarsDataFromFirebase();
    }

    private void fetchCarsDataFromFirebase() {
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                carsList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    DataClass car = dataSnapshot.getValue(DataClass.class);
                    carsList.add(car);
                }
                carsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(CarsActivity.this, "Failed to fetch Cars data: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onItemClick(int position) {
        // Récupérer la voiture cliquée
        DataClass clickedCar = carsList.get(position);
        // Passer les données de la voiture à l'activité CarDetailsActivity
        Intent intent = new Intent(this, CarDetailsActivity.class); // Changer pour CarDetailsActivity
        intent.putExtra("carName", clickedCar.getName());
        intent.putExtra("description", clickedCar.getDescription());
        intent.putExtra("phone", clickedCar.getPhone());
        intent.putExtra("location", clickedCar.getLocation());
        intent.putExtra("imageUrls", clickedCar.getImageUrls().toArray(new String[0])); // Convertir la liste en un tableau
        // Démarrer l'activité
        startActivity(intent);
    }}
