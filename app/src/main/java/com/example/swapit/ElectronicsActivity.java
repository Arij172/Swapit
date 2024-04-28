package com.example.swapit;

import android.os.Bundle;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.database.*;
import java.util.ArrayList;
import java.util.List;

public class ElectronicsActivity extends AppCompatActivity {
    private DatabaseReference databaseReference;
    private List<DataClass> electronicsList;
    private ElectronicsAdapter electronicsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_electronics);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        electronicsList = new ArrayList<>();
        electronicsAdapter = new ElectronicsAdapter(electronicsList);
        recyclerView.setAdapter(electronicsAdapter);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Electronics");

        fetchElectronicsDataFromFirebase();
    }

    private void fetchElectronicsDataFromFirebase() {
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                electronicsList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    DataClass electronics = dataSnapshot.getValue(DataClass.class);
                    electronicsList.add(electronics);
                }
                electronicsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ElectronicsActivity.this, "Failed to fetch Electronics data: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
