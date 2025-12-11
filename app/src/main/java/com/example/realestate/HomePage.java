package com.example.realestate;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class HomePage extends AppCompatActivity {

    // UI Components
    private CardView searchCardView;
    private LinearLayout buyLayout, rentLayout, sellLayout;
    private CardView propertyCard1, propertyCard2;
    private TextView headerTitle, searchHint, servicesTitle, featuredPropertiesTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home_page);

        // Apply window insets for edge-to-edge
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initializeViews();
        setupClickListeners();
        setupUI();
    }

    private void initializeViews() {
        // Main layouts
        searchCardView = findViewById(R.id.searchCardView);
        buyLayout = findViewById(R.id.buyLayout);
        rentLayout = findViewById(R.id.rentLayout);
        sellLayout = findViewById(R.id.sellLayout);
        propertyCard1 = findViewById(R.id.propertyCard1);
        propertyCard2 = findViewById(R.id.propertyCard2);

        // TextViews
        headerTitle = findViewById(R.id.headerTitle);
        searchHint = findViewById(R.id.searchHint);
        servicesTitle = findViewById(R.id.servicesTitle);
        featuredPropertiesTitle = findViewById(R.id.featuredPropertiesTitle);
    }

    private void setupClickListeners() {
        // Search bar click listener
        searchCardView.setOnClickListener(v -> {
            Toast.makeText(this, "Opening search...", Toast.LENGTH_SHORT).show();
            openSearchActivity();
        });

        // Quick action click listeners
        buyLayout.setOnClickListener(v -> {
            Toast.makeText(this, "Browse properties for sale", Toast.LENGTH_SHORT).show();
            openBuyProperties();
        });

        rentLayout.setOnClickListener(v -> {
            Toast.makeText(this, "Browse rental properties", Toast.LENGTH_SHORT).show();
            openRentProperties();
        });

        sellLayout.setOnClickListener(v -> {
            Toast.makeText(this, "List your property for sale", Toast.LENGTH_SHORT).show();
            openSellProperty();
        });

        // Property card click listeners
        propertyCard1.setOnClickListener(v -> {
            Toast.makeText(this, "Viewing Property 1 details", Toast.LENGTH_SHORT).show();
            openPropertyDetails(1);
        });

        propertyCard2.setOnClickListener(v -> {
            Toast.makeText(this, "Viewing Property 2 details", Toast.LENGTH_SHORT).show();
            openPropertyDetails(2);
        });
    }

    private void setupUI() {
        servicesTitle.setText("Services");
        featuredPropertiesTitle.setText("Featured Properties");
        searchHint.setText("Search properties, locations...");
    }

    // Navigation methods (to be implemented)
    private void openSearchActivity() {
        // startActivity(new Intent(HomePage.this, SearchActivity.class));
    }

    private void openBuyProperties() {
        // startActivity(new Intent(HomePage.this, BuyPropertiesActivity.class));
    }

    private void openRentProperties() {
        // startActivity(new Intent(HomePage.this, RentPropertiesActivity.class));
    }

    private void openSellProperty() {
        // startActivity(new Intent(HomePage.this, SellPropertyActivity.class));
    }

    private void openPropertyDetails(int propertyId) {
        // Intent intent = new Intent(HomePage.this, PropertyDetailsActivity.class);
        // intent.putExtra("PROPERTY_ID", propertyId);
        // startActivity(intent);
    }

    private void scrollToFeaturedProperties() {
        HorizontalScrollView slider = findViewById(R.id.propertySlider);
        if (slider != null) {
            slider.smoothScrollTo(0, 0);
        }
    }

    // Handle back button press
    @SuppressLint("GestureBackNavigation")
    @Override
    public void onBackPressed() {
        if (isTaskRoot()) {
            showExitConfirmation();
        } else {
            super.onBackPressed();
        }
    }

    private void showExitConfirmation() {
        new androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("Exit App")
                .setMessage("Are you sure you want to exit?")
                .setPositiveButton("Yes", (dialog, which) -> finish())
                .setNegativeButton("No", null)
                .show();
    }
}
