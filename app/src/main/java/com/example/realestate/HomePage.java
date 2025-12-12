package com.example.realestate;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateDecelerateInterpolator;
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

    private CardView searchCardView, propertyCard1, propertyCard2;
    private LinearLayout buyLayout, rentLayout, sellLayout;
    private TextView headerTitle, searchHint, servicesTitle, featuredPropertiesTitle, welcomeText;
    private ImageView profileIcon;
    private ImageView micButton;   // ⭐ Voice button support

    private boolean isExpanded = false; // ⭐ For expand animation

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
        animateProfileIcon();
        animateSearchBarSlideIn();     // ⭐ Slide in from top
        setupGlassEffect();            // ⭐ Blur / glass effect on scroll
    }

    private void initializeViews() {
        searchCardView = findViewById(R.id.searchCardView);
        buyLayout = findViewById(R.id.buyLayout);
        rentLayout = findViewById(R.id.rentLayout);
        sellLayout = findViewById(R.id.sellLayout);
        propertyCard1 = findViewById(R.id.propertyCard1);
        propertyCard2 = findViewById(R.id.propertyCard2);

        headerTitle = findViewById(R.id.headerTitle);
        searchHint = findViewById(R.id.searchInput);
        servicesTitle = findViewById(R.id.servicesTitle);
        featuredPropertiesTitle = findViewById(R.id.featuredPropertiesTitle);
        welcomeText = findViewById(R.id.welcomeText);
        profileIcon = findViewById(R.id.profileIcon);

        // ⭐ Optional mic icon (no crash even if missing)
        micButton = findViewById(R.id.micButton);
    }

    private void setupClickListeners() {

        // ⭐ Expandable Search Bar
        searchCardView.setOnClickListener(v -> {
            pressBounce(searchCardView);
            toggleSearchExpand();
            Toast.makeText(this, "Opening search...", Toast.LENGTH_SHORT).show();
        });

        // ⭐ Voice search
        if (micButton != null) {
            micButton.setOnClickListener(v ->
                    Toast.makeText(this, "Voice search coming soon!", Toast.LENGTH_SHORT).show()
            );
        }

        buyLayout.setOnClickListener(v -> Toast.makeText(this, "Browse properties for sale", Toast.LENGTH_SHORT).show());
        rentLayout.setOnClickListener(v -> Toast.makeText(this, "Browse rental properties", Toast.LENGTH_SHORT).show());
        sellLayout.setOnClickListener(v -> Toast.makeText(this, "List your property for sale", Toast.LENGTH_SHORT).show());
        propertyCard1.setOnClickListener(v -> Toast.makeText(this, "Viewing Property 1 details", Toast.LENGTH_SHORT).show());
        propertyCard2.setOnClickListener(v -> Toast.makeText(this, "Viewing Property 2 details", Toast.LENGTH_SHORT).show());
        profileIcon.setOnClickListener(v -> Toast.makeText(this, "Profile clicked", Toast.LENGTH_SHORT).show());
    }

    private void setupUI() {
        servicesTitle.setText("Services");
        featuredPropertiesTitle.setText("Featured Properties");
        searchHint.setText("Search properties, locations...");
    }

    // ⭐ Expand animation (CardView version)
    private void toggleSearchExpand() {
        int startWidth = searchCardView.getWidth();
        int targetWidth = isExpanded ?
                startWidth / 2 :
                searchCardView.getRootView().getWidth() - 100;

        ValueAnimator anim = ValueAnimator.ofInt(startWidth, targetWidth);
        anim.setDuration(350);
        anim.addUpdateListener(valueAnimator -> {
            int val = (int) valueAnimator.getAnimatedValue();
            searchCardView.getLayoutParams().width = val;
            searchCardView.requestLayout();
        });
        anim.start();

        isExpanded = !isExpanded;
    }

    // ⭐ Slide-in from top
    private void animateSearchBarSlideIn() {
        searchCardView.setTranslationY(-120f);
        searchCardView.setAlpha(0f);

        searchCardView.animate()
                .translationY(0)
                .alpha(1f)
                .setDuration(650)
                .setInterpolator(new AccelerateDecelerateInterpolator())
                .start();
    }

    // ⭐ Glass / blur style effect on any movement
    private void setupGlassEffect() {
        searchCardView.getViewTreeObserver().addOnScrollChangedListener(() -> {
            searchCardView.setCardElevation(18f);
            searchCardView.setAlpha(0.92f);
        });
    }

    private void applyHeaderAnimations() {
        ObjectAnimator headerFloat = ObjectAnimator.ofFloat(headerTitle, "translationY", 0f, -15f, 0f);
        ObjectAnimator welcomeFloat = ObjectAnimator.ofFloat(welcomeText, "translationY", 0f, -10f, 0f);

        headerFloat.setDuration(4000);
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

        ValueAnimator headerColorAnim = ValueAnimator.ofFloat(0, 1);
        headerColorAnim.setDuration(7000);
        headerColorAnim.setRepeatCount(ValueAnimator.INFINITE);
        headerColorAnim.addUpdateListener(animator -> {
            float fraction = (float) animator.getAnimatedValue();
            int color = Color.HSVToColor(new float[]{fraction * 360f, 1f, 1f});
            headerTitle.setTextColor(color);
        });
        headerColorAnim.start();

        ValueAnimator welcomeColorAnim = ValueAnimator.ofFloat(0, 1);
        welcomeColorAnim.setDuration(5000);
        welcomeColorAnim.setRepeatCount(ValueAnimator.INFINITE);
        welcomeColorAnim.addUpdateListener(animator -> {
            float fraction = (float) animator.getAnimatedValue();
            int color = Color.HSVToColor(new float[]{fraction * 360f, 1f, 1f});
            welcomeText.setTextColor(color);
        });
        welcomeColorAnim.start();
    }

    private void animateProfileIcon() {
        ObjectAnimator translateY = ObjectAnimator.ofFloat(profileIcon, "translationY", 0f, -12f, 0f);
        translateY.setDuration(2000);
        translateY.setRepeatCount(ValueAnimator.INFINITE);
        translateY.setRepeatMode(ValueAnimator.REVERSE);

        ObjectAnimator scaleX = ObjectAnimator.ofFloat(profileIcon, "scaleX", 1f, 1.08f, 1f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(profileIcon, "scaleY", 1f, 1.08f, 1f);

        scaleX.setDuration(2000);
        scaleY.setDuration(2000);

        scaleX.setRepeatCount(ValueAnimator.INFINITE);
        scaleY.setRepeatCount(ValueAnimator.INFINITE);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(translateY, scaleX, scaleY);
        animatorSet.setInterpolator(new AccelerateDecelerateInterpolator());
        animatorSet.start();
    }

    // ⭐ Bounce press animation
    private void pressBounce(CardView view) {
        AnimatorSet set = new AnimatorSet();

        ObjectAnimator sx = ObjectAnimator.ofFloat(view, "scaleX", 1f, 0.95f, 1f);
        ObjectAnimator sy = ObjectAnimator.ofFloat(view, "scaleY", 1f, 0.95f, 1f);

        set.setDuration(150);
        set.playTogether(sx, sy);
        set.start();
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
