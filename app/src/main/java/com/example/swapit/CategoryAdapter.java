package com.example.swapit;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import java.util.ArrayList;
import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> implements Filterable {

    private final List<Article> dataList;
    private List<Article> dataListFiltered;

    public CategoryAdapter(List<Article> dataList) {
        this.dataList = dataList;
        this.dataListFiltered = dataList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Article article = dataListFiltered.get(position);
        holder.nom.setText(article.getName());
        holder.localisation.setText(article.getLocation());
        // Charger la première image de la liste d'URLs d'images avec Glide
        if (article.getImageUris() != null && !article.getImageUris().isEmpty()) {
            String firstImageUrl = String.valueOf(article.getImageUris().get(0));
            Glide.with(holder.itemView.getContext())
                    .load(firstImageUrl)
                    .placeholder(R.drawable.placeholder_image)
                    .into(holder.imageView);
        }
    }

    @Override
    public int getItemCount() {
        return dataListFiltered.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    dataListFiltered = dataList;
                } else {
                    List<Article> filteredList = new ArrayList<>();
                    for (Article article : dataList) {
                        if (article.getName().toLowerCase().startsWith(charString.toLowerCase())) {
                            filteredList.add(article);
                        }
                    }
                    dataListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = dataListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                dataListFiltered = (ArrayList<Article>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView nom, localisation;
        ImageView imageView;

        public ViewHolder(View itemView) {
            super(itemView);
            nom = itemView.findViewById(R.id.nom);
            localisation = itemView.findViewById(R.id.localisation);
            imageView = itemView.findViewById(R.id.imageView6);
        }
    }
}