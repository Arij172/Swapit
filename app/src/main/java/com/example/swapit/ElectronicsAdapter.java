package com.example.swapit;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import java.util.List;

public class ElectronicsAdapter extends RecyclerView.Adapter<ElectronicsAdapter.ViewHolder> {
    private Context context;
    private List<DataClass> electronicsList;

    public ElectronicsAdapter(List<DataClass> electronicsList) {
        this.electronicsList = electronicsList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_electronics, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DataClass electronics = electronicsList.get(position);
        holder.textViewElectronicsName.setText(electronics.getName());

        // Charger l'image à partir de l'URL
        String imageUrl = electronics.getImageUrl1();
        Glide.with(context)
                .load(imageUrl)
                .placeholder(R.drawable.placeholder_image) // Image de remplacement pendant le chargement
                .error(R.drawable.placeholder_image) // Image de remplacement en cas d'erreur de chargement
                .into(holder.imageViewElectronics);
    }

    @Override
    public int getItemCount() {
        return electronicsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageViewElectronics;
        TextView textViewElectronicsName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewElectronics = itemView.findViewById(R.id.recyclerImage);
            textViewElectronicsName = itemView.findViewById(R.id.recyclerCaption);
        }
    }
}
