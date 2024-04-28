package com.example.swapit;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class SettingFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.settings_fragment, container, false);

        // Trouver le bouton par son ID
        Button button = rootView.findViewById(R.id.button6);

        // Ajouter un écouteur de clic sur le bouton
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Créer un Intent pour ouvrir la nouvelle activité
                Intent intent = new Intent(getActivity(), AboutUs.class);

                // Démarrer la nouvelle activité en utilisant l'Intent
                startActivity(intent);
            }
        });
        Button button1 = rootView.findViewById(R.id.button5);

        // Ajouter un écouteur de clic sur le bouton
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Créer un Intent pour ouvrir la nouvelle activité
                Intent intent = new Intent(getActivity(),MainActivity.class);

                // Démarrer la nouvelle activité en utilisant l'Intent
                startActivity(intent);
            }
        });

        Button button2 = rootView.findViewById(R.id.button8);

        // Ajouter un écouteur de clic sur le bouton
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Créer un Intent pour ouvrir la nouvelle activité
                Intent intent = new Intent(getActivity(),SendAsAMessage.class);

                // Démarrer la nouvelle activité en utilisant l'Intent
                startActivity(intent);
            }
        });

        Button button3 = rootView.findViewById(R.id.button10);

        // Ajouter un écouteur de clic sur le bouton
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Créer un Intent pour ouvrir la nouvelle activité
                Intent intent = new Intent(getActivity(),confidentialit.class);

                // Démarrer la nouvelle activité en utilisant l'Intent
                startActivity(intent);
            }
        });

        return rootView;
    }
}
