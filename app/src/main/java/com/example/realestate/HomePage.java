package com.example.realestate;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.EditText;
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
    private TextView headerTitle, servicesTitle, featuredPropertiesTitle, welcomeText;
    private EditText searchHint;
    private ImageView profileIcon, micButton;

    private boolean isExpanded = false;
    private ValueAnimator glowAnimator;
    private GradientDrawable glowDrawable;

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
        animateSearchBarSlideIn();
        setupGlassEffect();
        setupTypingGlow();
    }

    private void initializeViews() {
        searchCardView = findViewById(R.id.searchCardView);
        buyLayout = findViewById(R.id.buyLayout);
        rentLayout = findViewById(R.id.rentLayout);
        sellLayout = findViewById(R.id.sellLayout);
        propertyCard1 = findViewById(R.id.propertyCard1);
        propertyCard2 = findViewById(R.id.propertyCard2);

        headerTitle = findViewById(R.id.headerTitle); // "Find Your Dream Home"
        searchHint = findViewById(R.id.searchInput);
        servicesTitle = findViewById(R.id.servicesTitle);
        featuredPropertiesTitle = findViewById(R.id.featuredPropertiesTitle);
        welcomeText = findViewById(R.id.welcomeText); // "Welcome Back"
        profileIcon = findViewById(R.id.profileIcon);
        micButton = findViewById(R.id.micButton);

        // Set search bar background to white with thin border
        glowDrawable = new GradientDrawable();
        glowDrawable.setColor(Color.WHITE);
        glowDrawable.setCornerRadius(35f);
        glowDrawable.setStroke(3, Color.parseColor("#B0B0B0")); // thin gray border
        searchCardView.setBackground(glowDrawable);
    }

    private void setupClickListeners() {
        searchCardView.setOnClickListener(v -> {
            pressBounce(searchCardView);
            toggleSearchExpand();
            Toast.makeText(this, "Opening search...", Toast.LENGTH_SHORT).show();
        });

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
        searchHint.setHint("Search properties, locations...");
    }

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

    private void setupGlassEffect() {
        searchCardView.getViewTreeObserver().addOnScrollChangedListener(() -> {
            searchCardView.setCardElevation(18f);
            searchCardView.setAlpha(0.92f);
        });
    }

    private void applyHeaderAnimations() {
        // Float animations for header and welcome text
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

        // Optional: subtle color cycling for header text
        ValueAnimator headerColorAnim = ValueAnimator.ofFloat(0, 1);
        headerColorAnim.setDuration(7000);
        headerColorAnim.setRepeatCount(ValueAnimator.INFINITE);
        headerColorAnim.addUpdateListener(animator -> {
            float fraction = (float) animator.getAnimatedValue();
            int color = Color.HSVToColor(new float[]{fraction * 360f, 0.5f, 1f});
            headerTitle.setTextColor(color);
        });
        headerColorAnim.start();

        ValueAnimator welcomeColorAnim = ValueAnimator.ofFloat(0, 1);
        welcomeColorAnim.setDuration(5000);
        welcomeColorAnim.setRepeatCount(ValueAnimator.INFINITE);
        welcomeColorAnim.addUpdateListener(animator -> {
            float fraction = (float) animator.getAnimatedValue();
            int color = Color.HSVToColor(new float[]{fraction * 360f, 0.5f, 1f});
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

    private void pressBounce(CardView view) {
        AnimatorSet set = new AnimatorSet();

        ObjectAnimator sx = ObjectAnimator.ofFloat(view, "scaleX", 1f, 0.95f, 1f);
        ObjectAnimator sy = ObjectAnimator.ofFloat(view, "scaleY", 1f, 0.95f, 1f);

        set.setDuration(150);
        set.playTogether(sx, sy);
        set.start();
    }

    private void setupTypingGlow() {
        searchHint.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) startGlowAnimation();
                else stopGlowAnimation();
            }

            @Override
            public void afterTextChanged(Editable s) { }
        });
    }

    private void startGlowAnimation() {
        if (glowAnimator != null && glowAnimator.isRunning()) return;

        glowAnimator = ValueAnimator.ofInt(3, 12);
        glowAnimator.setDuration(800);
        glowAnimator.setRepeatMode(ValueAnimator.REVERSE);
        glowAnimator.setRepeatCount(ValueAnimator.INFINITE);

        glowAnimator.addUpdateListener(animation -> {
            int strokeWidth = (int) animation.getAnimatedValue();
            glowDrawable.setStroke(strokeWidth, Color.parseColor("#3F51B5"));
            searchCardView.setBackground(glowDrawable);
        });

        glowAnimator.start();
    }

    private void stopGlowAnimation() {
        if (glowAnimator != null) {
            glowAnimator.cancel();
            glowAnimator = null;
        }
        glowDrawable.setStroke(3, Color.parseColor("#B0B0B0"));
        searchCardView.setBackground(glowDrawable);
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
