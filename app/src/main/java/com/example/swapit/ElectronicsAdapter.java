package com.example.swapit;
import android.content.Context;
import android.content.Intent;
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
    private OnItemClickListener listener;

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
        DataClass electronicItem = electronicsList.get(position);
        holder.textViewItemName.setText(electronicItem.getName());

        // Charger l'image à partir de l'URL
        String imageUrl = electronicItem.getImageUrl1();
        Glide.with(context)
                .load(imageUrl)
                .placeholder(R.drawable.placeholder_image) // Image de remplacement pendant le chargement
                .error(R.drawable.placeholder_image) // Image de remplacement en cas d'erreur de chargement
                .into(holder.imageViewItem);

        // Ajouter un écouteur de clic sur le nom de l'article
        holder.textViewItemName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    int position = holder.getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(position);
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return electronicsList.size();
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageViewItem;
        TextView textViewItemName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewItem = itemView.findViewById(R.id.recyclerImage);
            textViewItemName = itemView.findViewById(R.id.recyclerCaption);
        }
    }
}
