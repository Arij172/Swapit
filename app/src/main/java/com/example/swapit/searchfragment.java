package com.example.swapit;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;

public class searchfragment extends Fragment {

    private EditText editText;
    private ListView searchResultsListView;
    private DatabaseReference booksRef;
    private DatabaseReference electronicsRef;
    private DatabaseReference carsRef;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.search_fragment, container, false);

        // Références aux nœuds de la base de données
        booksRef = FirebaseDatabase.getInstance().getReference().child("Books");
        electronicsRef = FirebaseDatabase.getInstance().getReference().child("Electronics");
        carsRef = FirebaseDatabase.getInstance().getReference().child("Cars");

        // Récupérer les références des vues
        editText = rootView.findViewById(R.id.searchEditText);
        searchResultsListView = rootView.findViewById(R.id.searchResultsListView);
        Button searchButton = rootView.findViewById(R.id.searchButton);

        // Définir un écouteur de clic sur le bouton de recherche
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchTerm = editText.getText().toString().trim();
                performSearch(searchTerm);
            }
        });

        // Rendre l'EditText cliquable
        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Action à effectuer lorsque l'EditText est cliqué
            }
        });

        return rootView;
    }

    // Méthode pour effectuer la recherche
    private void performSearch(final String searchTerm) {
        final ArrayList<String> searchResults = new ArrayList<>();
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, searchResults);
        searchResultsListView.setAdapter(adapter);

        // Recherche dans le nœud Books
        Query booksQuery = booksRef.orderByChild("name").equalTo(searchTerm);
        booksQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String articleName = snapshot.child("name").getValue(String.class);
                    searchResults.add(articleName);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Gérer l'erreur
            }
        });

        // Recherche dans le nœud Electronics
        Query electronicsQuery = electronicsRef.orderByChild("name").equalTo(searchTerm);
        electronicsQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String articleName = snapshot.child("name").getValue(String.class);
                    searchResults.add(articleName);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Gérer l'erreur
            }
        });

        // Recherche dans le nœud Cars
        Query carsQuery = carsRef.orderByChild("name").equalTo(searchTerm);
        carsQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String articleName = snapshot.child("name").getValue(String.class);
                    searchResults.add(articleName);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Gérer l'erreur
            }
        });
    }
}
