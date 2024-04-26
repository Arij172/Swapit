package com.example.swapit;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import androidx.fragment.app.Fragment;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import android.util.Log;


public class addFragment extends Fragment {

    private static final int MAX_IMAGES = 3;
    private final ArrayList<Uri> imageUris = new ArrayList<>();
    private DatabaseReference mDatabase;
    private ImageView imageView1;
    private ImageView imageView2;
    private ImageView imageView3;
    private final ActivityResultLauncher<Intent> galleryLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                    handleGalleryResult(result.getData());
                }
            }
    );

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_fragment, container, false);
        mDatabase = FirebaseDatabase.getInstance().getReference();

        final EditText nameEditText = view.findViewById(R.id.nameEditText);
        final EditText descriptionEditText = view.findViewById(R.id.descriptionEditText);
        final EditText locationEditText = view.findViewById(R.id.locationEditText);
        final EditText phoneEditText = view.findViewById(R.id.phoneEditText);
        Button addButton = view.findViewById(R.id.addButton);
        final Button imageCardView = view.findViewById(R.id.button7);

        imageView1 = view.findViewById(R.id.imageView1);
        imageView2 = view.findViewById(R.id.imageView2);
        imageView3 = view.findViewById(R.id.imageView3);

        // Initialisation du Spinner pour les catégories
        Spinner categorySpinner = view.findViewById(R.id.categorySpinner);
        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, new String[]{"Cars", "Books", "Electronics"});
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(categoryAdapter);

        imageCardView.setOnClickListener(v -> openGallery());

        addButton.setOnClickListener(v -> {
            String name = nameEditText.getText().toString().trim();
            String description = descriptionEditText.getText().toString().trim();
            String location = locationEditText.getText().toString().trim();
            String phone = phoneEditText.getText().toString().trim();
            // Récupérer la catégorie sélectionnée
            String category = categorySpinner.getSelectedItem().toString();
            if (!name.isEmpty() && !description.isEmpty() && !location.isEmpty() && !phone.isEmpty()) {
                addArticleToFirebase(name, description, location, phone,category);

                nameEditText.setText("");
                descriptionEditText.setText("");
                locationEditText.setText("");
                phoneEditText.setText("");
                // Réinitialiser les images sélectionnées
                imageUris.clear();
                // Réinitialiser les ImageView
                imageView1.setImageResource(android.R.color.transparent);
                imageView2.setImageResource(android.R.color.transparent);
                imageView3.setImageResource(android.R.color.transparent);
            }
        });

        return view;
    }

    private void openGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        galleryIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        galleryLauncher.launch(Intent.createChooser(galleryIntent, "Select Picture"));
    }

    private void handleGalleryResult(Intent data) {
        if (data.getClipData() != null) {
            int count = data.getClipData().getItemCount();
            for (int i = 0; i < count; i++) {
                Uri imageUri = data.getClipData().getItemAt(i).getUri();
                if (imageUris.size() < MAX_IMAGES) {
                    imageUris.add(imageUri);
                    // Afficher l'image dans l'ImageView correspondant
                    displayImageInImageView(imageUri, i);
                } else {
                    // Limite d'images atteinte
                    Toast.makeText(getActivity(), "Maximum number of images reached", Toast.LENGTH_SHORT).show();
                    break;
                }
            }
        } else if (data.getData() != null) {
            Uri imageUri = data.getData();
            if (imageUris.size() < MAX_IMAGES) {
                imageUris.add(imageUri);
                // Afficher l'image dans le premier ImageView
                displayImageInImageView(imageUri, 0);
            } else {
                // Limite d'images atteinte
                Toast.makeText(getActivity(), "Maximum number of images reached", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void displayImageInImageView(Uri imageUri, int index) {
        ImageView imageView;
        if (index == 0) {
            imageView = imageView1;
        } else if (index == 1) {
            imageView = imageView2;
        } else if (index == 2) {
            imageView = imageView3;
        } else {
            return;
        }
        imageView.setImageURI(imageUri);
    }

    private void addArticleToFirebase(String name, String description, String location, String phone,String category) {
        // Générer une clé unique pour l'article
        String articleId = mDatabase.child("articles").child(category).push().getKey();
        // Créer une map contenant les données de l'article
        Map<String, Object> articleValues = new HashMap<>();
        articleValues.put("name", name);
        articleValues.put("description", description);
        articleValues.put("location", location);
        articleValues.put("phone", phone);

        // Référence à l'emplacement dans Firebase Storage où vous souhaitez stocker les images
        StorageReference storageRef = FirebaseStorage.getInstance().getReference().child("article_images").child(articleId);

        // Compteur pour suivre le nombre d'images téléchargées avec succès
        AtomicInteger uploadCount = new AtomicInteger(0);

        // Ajouter les images sélectionnées à Firebase Storage
        for (int i = 0; i < imageUris.size(); i++) {
            Uri imageUri = imageUris.get(i);
            String imageName = "image" + (i + 1);
            articleValues.put(imageName, imageUris.get(i).toString());
            // Obtenir une référence à l'emplacement où vous souhaitez stocker l'image dans Firebase Storage
            StorageReference imageRef = storageRef.child(imageName);

            // Télécharger l'image sur Firebase Storage
            imageRef.putFile(imageUri)
                    .addOnSuccessListener(taskSnapshot -> {
                        // Une fois l'image téléchargée avec succès, obtenir son URL de téléchargement
                        imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                            // Ajouter l'URL de téléchargement de l'image à la map des valeurs de l'article
                            articleValues.put(imageName, uri.toString());

                            // Incrémenter le compteur d'images téléchargées
                            int count = uploadCount.incrementAndGet();

                            // Vérifier si toutes les images ont été téléchargées
                            if (count == imageUris.size()) {
                                // Toutes les images ont été téléchargées, ajouter l'article à la base de données Firebase
                                mDatabase.child("articles").child(category).child(articleId).setValue(articleValues)
                                        .addOnSuccessListener(aVoid -> {
                                            // Données stockées avec succès dans la base de données en temps réel
                                            Toast.makeText(getActivity(), "Article added successfully", Toast.LENGTH_SHORT).show();
                                        })
                                        .addOnFailureListener(e -> {
                                            // En cas d'échec du stockage dans la base de données en temps réel
                                            Toast.makeText(getActivity(), "Failed to add article to database", Toast.LENGTH_SHORT).show();
                                        });
                            }
                        });
                    })
                    .addOnFailureListener(e -> {
                        // En cas d'échec du téléchargement de l'image, afficher un message d'erreur
                        Toast.makeText(getActivity(), "Failed to upload image", Toast.LENGTH_SHORT).show();
                    });
        }

        mDatabase.child(category).child(articleId).setValue(articleValues);
        Toast.makeText(getActivity(), "Article added successfully", Toast.LENGTH_SHORT).show();
    }
}