package com.example.swapit;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class Article implements Parcelable {
    private String name;
    private String description;
    private String location;
    private String phone;
    private List<String> imageUrls;

    public Article() {
        // Required empty constructor for Firebase deserialization
    }

    public Article(String name, String description, String location, String phone, List<String> imageUrls) {
        this.name = name;
        this.description = description;
        this.location = location;
        this.phone = phone;
        this.imageUrls = imageUrls;
    }

    protected Article(Parcel in) {
        name = in.readString();
        description = in.readString();
        location = in.readString();
        phone = in.readString();
        imageUrls = in.createStringArrayList();
    }

    public static final Creator<Article> CREATOR = new Creator<Article>() {
        @Override
        public Article createFromParcel(Parcel in) {
            return new Article(in);
        }

        @Override
        public Article[] newArray(int size) {
            return new Article[size];
        }
    };

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getLocation() {
        return location;
    }

    public String getPhone() {
        return phone;
    }

    public List<String> getImageUrls() {
        return imageUrls;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(description);
        dest.writeString(location);
        dest.writeString(phone);
        dest.writeStringList(imageUrls);
    }
}
