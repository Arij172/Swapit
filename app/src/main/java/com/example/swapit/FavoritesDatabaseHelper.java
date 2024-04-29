package com.example.swapit;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FavoritesDatabaseHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "FavoritesDB";
  public static final String TABLE_FAVORITES = "favorites";
   public static final String COLUMN_ID = "id";
    public static final String COLUMN_BOOK_NAME = "Name";
   public static final String COLUMN_DESCRIPTION = "description";
    public static final String COLUMN_PHONE_NUMBER = "phoneNumber";
   public static final String COLUMN_LOCATION = "location";
   public static final String COLUMN_IMAGE_URLS = "imageUrls";

    public FavoritesDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_FAVORITES_TABLE = "CREATE TABLE " + TABLE_FAVORITES + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY,"
                + COLUMN_BOOK_NAME + " TEXT,"
                + COLUMN_DESCRIPTION + " TEXT,"
                + COLUMN_PHONE_NUMBER + " TEXT,"
                + COLUMN_LOCATION + " TEXT,"
                + COLUMN_IMAGE_URLS + " TEXT" + ")";
        db.execSQL(CREATE_FAVORITES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FAVORITES);
        // Create tables again
        onCreate(db);
    }

    public void addToFavorites(String bookName, String description, String phoneNumber, String location, List<String> imageUrls) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_BOOK_NAME, bookName);
        values.put(COLUMN_DESCRIPTION, description);
        values.put(COLUMN_PHONE_NUMBER, phoneNumber);
        values.put(COLUMN_LOCATION, location);
        values.put(COLUMN_IMAGE_URLS, String.join(",", imageUrls)); // Convert array to comma-separated string
        db.insert(TABLE_FAVORITES, null, values);
        db.close();
    }

    public List<DataClass> getAllFavorites() {
        List<DataClass> favoritesList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_FAVORITES;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                DataClass favorite = new DataClass();
                favorite.setName(cursor.getString(cursor.getColumnIndex(COLUMN_BOOK_NAME)));
                favorite.setDescription(cursor.getString(cursor.getColumnIndex(COLUMN_DESCRIPTION)));
                favorite.setPhone(cursor.getString(cursor.getColumnIndex(COLUMN_PHONE_NUMBER)));
                favorite.setLocation(cursor.getString(cursor.getColumnIndex(COLUMN_LOCATION)));
                // Récupérer la chaîne d'URLs d'images depuis le curseur
                String imageUrlsStr = cursor.getString(cursor.getColumnIndex(COLUMN_IMAGE_URLS));
                // Convertir la chaîne en une liste de chaînes
                List<String> imageUrlsList = Arrays.asList(imageUrlsStr.split(","));
                // Définir les URLs d'images pour l'objet DataClass
                favorite.setImageUrls(imageUrlsList);
                favoritesList.add(favorite);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return favoritesList;
    }



    public void removeFavorite(String bookName) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_FAVORITES, COLUMN_BOOK_NAME + " = ?", new String[]{bookName});
        db.close();
    }
}

