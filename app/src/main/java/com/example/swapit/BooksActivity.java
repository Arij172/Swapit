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

public class BooksActivity extends AppCompatActivity implements BooksAdapter.OnItemClickListener {
    private DatabaseReference databaseReference;
    private List<DataClass> booksList;
    private BooksAdapter booksAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_books);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        booksList = new ArrayList<>();
        booksAdapter = new BooksAdapter(booksList);
        recyclerView.setAdapter(booksAdapter);

        // Définir l'écouteur de clic sur l'adaptateur
        booksAdapter.setOnItemClickListener(this);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Books");

        fetchBooksDataFromFirebase();
    }

    private void fetchBooksDataFromFirebase() {
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                booksList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    DataClass book = dataSnapshot.getValue(DataClass.class);
                    booksList.add(book);
                }
                booksAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(BooksActivity.this, "Failed to fetch Books data: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onItemClick(int position) {
        // Récupérer l'article cliqué
        DataClass clickedBook = booksList.get(position);
        // Passer les données de l'article à l'activité BookDetailsActivity
        Intent intent = new Intent(this, BookDetailsActivity.class);
        intent.putExtra("bookName", clickedBook.getName());
        intent.putExtra("description", clickedBook.getDescription());
        intent.putExtra("phone", clickedBook.getPhone());
        intent.putExtra("location", clickedBook.getLocation());
        intent.putExtra("imageUrls", clickedBook.getImageUrls().toArray(new String[0])); // Convertir la liste en un tableau
        // Démarrer l'activité
        startActivity(intent);
    }
}
