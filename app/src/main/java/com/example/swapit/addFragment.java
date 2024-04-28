package com.example.swapit;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class addFragment extends Fragment {

    private ImageView imageView1, imageView2, imageView3;
    private EditText nameEditText, descriptionEditText, phoneEditText, locationEditText;
    private Button addButton;
    private ProgressBar progressBar;
    private DatabaseReference databaseReference;
    private StorageReference storageReference;
    private Spinner categorySpinner;
    private final Uri[] imageUris = new Uri[3]; // Array to store 3 image URIs

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_fragment, container, false);

        // Initialize views
        imageView1 = view.findViewById(R.id.imageView1);
        imageView2 = view.findViewById(R.id.imageView2);
        imageView3 = view.findViewById(R.id.imageView3);
        nameEditText = view.findViewById(R.id.nameEditText);
        descriptionEditText = view.findViewById(R.id.descriptionEditText);
        phoneEditText = view.findViewById(R.id.phoneEditText);
        locationEditText = view.findViewById(R.id.locationEditText);
        addButton = view.findViewById(R.id.addButton);
        progressBar = view.findViewById(R.id.progressBar);
        categorySpinner = view.findViewById(R.id.categorySpinner);

        // Firebase references
        databaseReference = FirebaseDatabase.getInstance().getReference();
        storageReference = FirebaseStorage.getInstance().getReference();

        // Set up spinner with categories
        ArrayAdapter        <CharSequence> categoryAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.categories, android.R.layout.simple_spinner_item);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(categoryAdapter);

        // Set click listeners for image views
        imageView1.setOnClickListener(v -> selectImage(0)); // Pass index 0 for imageView1
        imageView2.setOnClickListener(v -> selectImage(1)); // Pass index 1 for imageView2
        imageView3.setOnClickListener(v -> selectImage(2)); // Pass index 2 for imageView3

        // Set click listener for add button
        addButton.setOnClickListener(v -> {
            String selectedCategory = categorySpinner.getSelectedItem().toString();
            // Upload images to Firebase
            uploadImagesToFirebase(selectedCategory);
        });

        return view;
    }

    private void selectImage(int index) {
        Intent photoPicker = new Intent(Intent.ACTION_GET_CONTENT);
        photoPicker.setType("image/*");
        startActivityForResult(photoPicker, index); // Pass index to identify which imageView was clicked
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            int index = requestCode; // Get index from requestCode
            imageUris[index] = data.getData();
            // Show a preview of the selected image based on index
            if (index == 0) {
                Glide.with(this).load(imageUris[0]).into(imageView1);
            } else if (index == 1) {
                Glide.with(this).load(imageUris[1]).into(imageView2);
            } else if (index == 2) {
                Glide.with(this).load(imageUris[2]).into(imageView3);
            }
        }
    }

    private void uploadImagesToFirebase(String category) {
        List<String> imageUrls = new ArrayList<>();
        AtomicInteger uploadCount = new AtomicInteger(0); // Utiliser AtomicInteger pour la compteur

        for (int i = 0; i < 3; i++) {
            if (imageUris[i] != null) {
                StorageReference imageRef = storageReference.child("article_images").child(System.currentTimeMillis() + getFileExtension(imageUris[i]));
                UploadTask uploadTask = imageRef.putFile(imageUris[i]);
                int finalI = i;
                uploadTask.addOnSuccessListener(taskSnapshot -> {
                    imageRef.getDownloadUrl().addOnSuccessListener(downloadUri -> {
                        imageUrls.add(downloadUri.toString());
                        int count = uploadCount.incrementAndGet(); // Incrémenter et obtenir la nouvelle valeur du compteur
                        if (count == 3 || count == getNonNullCount(imageUris)) { // Vérifier si toutes les images sont téléchargées
                            // Toutes les images téléchargées, procéder à l'opération de base de données
                            saveDataToDatabase(imageUrls, category);
                        }
                    });
                }).addOnFailureListener(e -> {
                    // Gérer l'échec
                    Toast.makeText(getActivity(), "Failed to upload image: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
            }
        }
    }

    private int getNonNullCount(Uri[] uris) {
        int count = 0;
        for (Uri uri : uris) {
            if (uri != null) {
                count++;
            }
        }
        return count;
    }





    private void saveDataToDatabase(List<String> imageUrls, String category) {
        String name = nameEditText.getText().toString();
        String description = descriptionEditText.getText().toString();
        String phone = phoneEditText.getText().toString();
        String location = locationEditText.getText().toString();

        // Vérifiez si tous les champs requis sont initialisés
        if (name.isEmpty() || description.isEmpty() || phone.isEmpty() || location.isEmpty() || imageUrls.isEmpty()) {
            Toast.makeText(getActivity(), "Please fill in all required fields", Toast.LENGTH_SHORT).show();
            return; // Arrêtez l'exécution de la méthode si l'un des champs requis est vide
        }

        // Prenez la première URL d'image de la liste
        String imageUrl = imageUrls.get(0);

        // Tous les champs sont initialisés, créez l'objet DataClass et enregistrez les données dans la base de données
        DataClass articleData = new DataClass(name, description, phone, location, imageUrls, category, imageUrl);

        // Convertir l'objet DataClass en un objet Map
        Map<String, Object> articleValues = articleData.toMap();

        // Créer un nouvel objet pour stocker les valeurs mises à jour
        Map<String, Object> childUpdates = new HashMap<>();
        String articleKey = databaseReference.child(category).push().getKey();
        childUpdates.put("/" + category + "/" + articleKey, articleValues);

        // Enregistrer les données dans Firebase
        databaseReference.updateChildren(childUpdates)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(getActivity(), "Article uploaded successfully", Toast.LENGTH_SHORT).show();
                    clearFields();
                })
                .addOnFailureListener(e -> Toast.makeText(getActivity(), "Failed to upload article: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }




    private void clearFields() {
        nameEditText.getText().clear();
        descriptionEditText.getText().clear();
        phoneEditText.getText().clear();
        locationEditText.getText().clear();
        imageView1.setImageResource(android.R.color.transparent); // Clear the preview images
        imageView2.setImageResource(android.R.color.transparent);
        imageView3.setImageResource(android.R.color.transparent);
        Arrays.fill(imageUris, null); // Clear the image URIs
    }

    private String getFileExtension(Uri uri) {
        ContentResolver contentResolver = getActivity().getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }
}

