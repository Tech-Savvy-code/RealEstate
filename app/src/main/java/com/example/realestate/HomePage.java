package com.example.realestate;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.HorizontalScrollView;
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
    private TextView headerTitle, searchHint, servicesTitle, featuredPropertiesTitle, welcomeText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home_page);

        // Edge-to-edge padding
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initializeViews();
        setupClickListeners();
        setupUI();
        applyHeaderAnimations();
    }

    private void initializeViews() {
        searchCardView = findViewById(R.id.searchCardView);
        buyLayout = findViewById(R.id.buyLayout);
        rentLayout = findViewById(R.id.rentLayout);
        sellLayout = findViewById(R.id.sellLayout);
        propertyCard1 = findViewById(R.id.propertyCard1);
        propertyCard2 = findViewById(R.id.propertyCard2);

        headerTitle = findViewById(R.id.headerTitle);
        searchHint = findViewById(R.id.searchHint);
        servicesTitle = findViewById(R.id.servicesTitle);
        featuredPropertiesTitle = findViewById(R.id.featuredPropertiesTitle);
        welcomeText = findViewById(R.id.welcomeText); // ensure this exists in XML
    }

    private void setupClickListeners() {
        searchCardView.setOnClickListener(v -> Toast.makeText(this, "Opening search...", Toast.LENGTH_SHORT).show());
        buyLayout.setOnClickListener(v -> Toast.makeText(this, "Browse properties for sale", Toast.LENGTH_SHORT).show());
        rentLayout.setOnClickListener(v -> Toast.makeText(this, "Browse rental properties", Toast.LENGTH_SHORT).show());
        sellLayout.setOnClickListener(v -> Toast.makeText(this, "List your property for sale", Toast.LENGTH_SHORT).show());
        propertyCard1.setOnClickListener(v -> Toast.makeText(this, "Viewing Property 1 details", Toast.LENGTH_SHORT).show());
        propertyCard2.setOnClickListener(v -> Toast.makeText(this, "Viewing Property 2 details", Toast.LENGTH_SHORT).show());
    }

    private void setupUI() {
        servicesTitle.setText("Services");
        featuredPropertiesTitle.setText("Featured Properties");
        searchHint.setText("Search properties, locations...");
    }

    private void applyHeaderAnimations() {
        // --- Floating animation (up & down) ---
        ObjectAnimator headerFloat = ObjectAnimator.ofFloat(headerTitle, "translationY", 0f, -20f, 0f);
        ObjectAnimator welcomeFloat = ObjectAnimator.ofFloat(welcomeText, "translationY", 0f, -10f, 0f);
        headerFloat.setDuration(3000);
        welcomeFloat.setDuration(3000);
        headerFloat.setRepeatCount(ValueAnimator.INFINITE);
        welcomeFloat.setRepeatCount(ValueAnimator.INFINITE);
        headerFloat.setRepeatMode(ValueAnimator.REVERSE);
        welcomeFloat.setRepeatMode(ValueAnimator.REVERSE);
        headerFloat.setInterpolator(new AccelerateDecelerateInterpolator());
        welcomeFloat.setInterpolator(new AccelerateDecelerateInterpolator());

        AnimatorSet floatSet = new AnimatorSet();
        floatSet.playTogether(headerFloat, welcomeFloat);
        floatSet.start();

        // --- Continuous rainbow color animation ---
        ValueAnimator colorAnim = ValueAnimator.ofFloat(0, 1);
        colorAnim.setDuration(6000);
        colorAnim.setRepeatCount(ValueAnimator.INFINITE);
        colorAnim.addUpdateListener(animator -> {
            float fraction = (float) animator.getAnimatedValue();
            int color = Color.HSVToColor(new float[]{
                    fraction * 360f, 1f, 1f
            });
            headerTitle.setTextColor(color);
            welcomeText.setTextColor(color);
        });
        colorAnim.start();
    }

    @SuppressLint("GestureBackNavigation")
    @Override
    public void onBackPressed() {
        if (isTaskRoot()) showExitConfirmation();
        else super.onBackPressed();
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
