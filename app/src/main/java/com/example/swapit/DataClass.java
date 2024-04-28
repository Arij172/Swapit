package com.example.swapit;

import android.net.Uri;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@IgnoreExtraProperties
public class DataClass {
    private String name;
    private String description;
    private String phone;
    private String location;
    private List<String> imageUrls;
    private String category;
    private String imageUrl1; // Changement de Uri en String pour une meilleure compatibilité avec Firebase

    // Constructeur par défaut nécessaire pour Firebase
    public DataClass() {
        // Default constructor required for Firebase
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public String getPhone() {
        return phone;
    }

    public String getLocation() {
        return location;
    }

    public List<String> getImageUrls() {
        return imageUrls;
    }

    public String getCategory() {
        return category;
    }

    public String getImageUrl1() {
        return imageUrl1;
    }

    public void setImageUrl1(String imageUrl1) {
        this.imageUrl1 = imageUrl1;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setImageUrls(List<String> imageUrls) {
        this.imageUrls = imageUrls;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setDescription(String description) {
        this.description = description;
    }
// Getters et setters
public DataClass(String name, String description, String phone, String location, List<String> imageUrls, String category, String imageUrl1) {
    this.name = name;
    this.description = description;
    this.phone = phone;
    this.location = location;
    this.imageUrls = imageUrls;
    this.category = category;
    this.imageUrl1 = imageUrl1;
}

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("name", name);
        result.put("description", description);
        result.put("phone", phone);
        result.put("location", location);
        result.put("imageUrls", imageUrls);
        result.put("category", category);
        result.put("imageUrl1", imageUrl1); // Ne pas convertir en chaîne, car il s'agit déjà d'une chaîne
        return result;
    }
}
