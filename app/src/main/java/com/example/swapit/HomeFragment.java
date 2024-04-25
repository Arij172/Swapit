package com.example.swapit;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class HomeFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        // Récupérer la référence de l'image imageView9
        ImageView imageView9 = rootView.findViewById(R.id.imageView9);

        // Ajouter un OnClickListener à l'image imageView9
        imageView9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Créer une intention pour démarrer CategoryActivity
                Intent intent = new Intent(getActivity(), CategoryActivity.class);

                // Démarrer CategoryActivity
                startActivity(intent);
            }
        });

        return rootView;
    }
}
