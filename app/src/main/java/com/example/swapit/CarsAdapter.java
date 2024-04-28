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

public class CarsAdapter extends RecyclerView.Adapter<CarsAdapter.ViewHolder> {
    private Context context;
    private List<DataClass> carsList;
    private OnItemClickListener listener;

    public CarsAdapter(List<DataClass> carsList) {
        this.carsList = carsList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.item_car, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DataClass car = carsList.get(position);
        holder.textViewCarName.setText(car.getName());

        // Charger l'image à partir de l'URL
        String imageUrl = car.getImageUrl1();
        Glide.with(context)
                .load(imageUrl)
                .placeholder(R.drawable.placeholder_image) // Image de remplacement pendant le chargement
                .error(R.drawable.placeholder_image) // Image de remplacement en cas d'erreur de chargement
                .into(holder.imageViewCar);

        // Ajouter un écouteur de clic sur le nom de la voiture
        holder.textViewCarName.setOnClickListener(new View.OnClickListener() {
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
        return carsList.size();
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageViewCar;
        TextView textViewCarName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewCar = itemView.findViewById(R.id.recyclerImage);
            textViewCarName = itemView.findViewById(R.id.recyclerCaption);
        }
    }
}
