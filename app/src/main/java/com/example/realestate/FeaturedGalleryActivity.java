package com.example.realestate;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class FeaturedGalleryActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private PropertyAdapter adapter;
    private List<PropertyModel> propertyList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_featured_gallery);

        recyclerView = findViewById(R.id.recyclerViewProperties);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        propertyList = new ArrayList<>();
        loadProperties();

        adapter = new PropertyAdapter(propertyList);
        recyclerView.setAdapter(adapter);
    }

    private void loadProperties() {
        propertyList.add(new PropertyModel("Sunny Villa", "$120,000", R.drawable.villa));
        propertyList.add(new PropertyModel("Modern Apartment", "$85,000", R.drawable.icon1));
        propertyList.add(new PropertyModel("Cozy Cottage", "$95,000", R.drawable.icon2));
        propertyList.add(new PropertyModel("Luxury Mansion", "$450,000", R.drawable.icon3));
        propertyList.add(new PropertyModel("Beach House", "$230,000", R.drawable.logo));
        propertyList.add(new PropertyModel("City Loft", "$150,000", R.drawable.ic_home));
    }

}
