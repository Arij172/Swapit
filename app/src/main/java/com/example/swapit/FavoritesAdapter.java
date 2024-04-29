package com.example.swapit;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.swapit.DataClass;
import java.util.List;

public class FavoritesAdapter extends RecyclerView.Adapter<FavoritesAdapter.ViewHolder> {
    private List<DataClass> favoritesList;

    public FavoritesAdapter(List<DataClass> favoritesList) {
        this.favoritesList = favoritesList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_favorite, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DataClass favorite = favoritesList.get(position);
        holder.bind(favorite);
    }

    @Override
    public int getItemCount() {
        return favoritesList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewBookName;
        private ImageView imageViewBook;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewBookName = itemView.findViewById(R.id.recyclerCaption);
            imageViewBook = itemView.findViewById(R.id.recyclerImage);
        }

        public void bind(DataClass favorite) {
            textViewBookName.setText(favorite.getName());
            String imageUrl = favorite.getImageUrls().get(0); // Obtenir l'URL de l'image à l'index 0
            Glide.with(itemView.getContext()).load(imageUrl).into(imageViewBook);
        }
    }
}
