package com.example.swapit;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class favoritesFragment extends Fragment {
    private RecyclerView recyclerView;
    private FavoritesAdapter favoritesAdapter;
    private List<DataClass> favoritesList;
    private FavoritesDatabaseHelper databaseHelper;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Initialiser la classe d'aide à la base de données
        databaseHelper = new FavoritesDatabaseHelper(getContext());
        // Récupérer les favoris depuis la base de données
        favoritesList = databaseHelper.getAllFavorites();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.favourites_fragment, container, false);
        recyclerView = rootView.findViewById(R.id.searchResultsListView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        favoritesAdapter = new FavoritesAdapter(favoritesList);
        recyclerView.setAdapter(favoritesAdapter);
        return rootView;
    }
}
