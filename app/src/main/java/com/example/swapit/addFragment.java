package com.example.swapit;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class addFragment extends Fragment {
    private static final int REQUEST_CODE_PICK_IMAGE = 1;
    private static final int MAX_IMAGES = 3;
    private final ArrayList<Uri> imageUris = new ArrayList<>();
    private DatabaseReference mDatabase;
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
        final CardView imageCardView = view.findViewById(R.id.cardView);
        final ImageView imageView1 = view.findViewById(R.id.imageView1);
        final ImageView imageView2 = view.findViewById(R.id.imageView2);
        final ImageView imageView3 = view.findViewById(R.id.imageView3);
        imageCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = nameEditText.getText().toString().trim();
                String description = descriptionEditText.getText().toString().trim();
                String location = locationEditText.getText().toString().trim();
                String phone = phoneEditText.getText().toString().trim();

                if (!name.isEmpty() && !description.isEmpty() && !location.isEmpty() && !phone.isEmpty()) {
                    addArticleToFirebase(name, description, location, phone);

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
            }
        });


        return view;
    }
       private void openGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        galleryIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        startActivityForResult(Intent.createChooser(galleryIntent, "Select Picture"), REQUEST_CODE_PICK_IMAGE);


         }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_PICK_IMAGE && resultCode == getActivity().RESULT_OK && data != null) {
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
    }
    private void displayImageInImageView(Uri imageUri, int index) {
        ImageView imageView = null;
        if (index == 0) {
            imageView = imageView.findViewById(R.id.imageView1);
        } else if (index == 1) {
            imageView = imageView.findViewById(R.id.imageView2);
        } else if (index == 2) {
            imageView = imageView.findViewById(R.id.imageView3);
        }
        if (imageView != null) {
            imageView.setImageURI(imageUri);
        }
    }
    private void addArticleToFirebase(String name, String description, String location, String phone) {
        // Générer une clé unique pour l'article
        String articleId = mDatabase.child("articles").push().getKey();

        // Créer une map contenant les données de l'article
        Map<String, Object> articleValues = new HashMap<>();
        articleValues.put("name", name);
        articleValues.put("description", description);
        articleValues.put("location", location);
        articleValues.put("phone", phone);

        // Ajouter les images à la map
        for (int i = 0; i < imageUris.size(); i++) {
            String imageName = "image" + (i + 1);
            articleValues.put(imageName, imageUris.get(i).toString());
        }

        // Ajouter l'article à la base de données Firebase
        mDatabase.child("articles").child(articleId).setValue(articleValues);
    }
    }


