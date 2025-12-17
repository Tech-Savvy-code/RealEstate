package com.example.realestate;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class PropertyAdapter extends RecyclerView.Adapter<PropertyAdapter.PropertyViewHolder> {

    private final List<PropertyModel> propertyList;

    public PropertyAdapter(List<PropertyModel> propertyList) {
        this.propertyList = propertyList;
    }

    @NonNull
    @Override
    public PropertyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_property_card, parent, false);
        return new PropertyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PropertyViewHolder holder, int position) {
        PropertyModel property = propertyList.get(position);

        holder.title.setText(property.getName());
        holder.price.setText(property.getPrice());
        holder.image.setImageResource(property.getImageResId());
    }

    @Override
    public int getItemCount() {
        return propertyList.size();
    }

    static class PropertyViewHolder extends RecyclerView.ViewHolder {

        ImageView image;
        TextView title, price;

        public PropertyViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.propertyImage);
            title = itemView.findViewById(R.id.propertyTitle);
            price = itemView.findViewById(R.id.propertyPrice);
        }
    }
}
