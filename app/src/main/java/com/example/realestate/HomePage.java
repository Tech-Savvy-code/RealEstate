package com.example.realestate;

import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
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

    // UI Components
    private CardView searchCardView, propertyCard1, propertyCard2;
    private LinearLayout buyLayout, rentLayout, sellLayout;
    private TextView headerTitle, servicesTitle, featuredPropertiesTitle, welcomeText, seeAllText;
    private EditText searchHint;
    private ImageView profileIcon, micButton, iconBuy, iconRent, iconSell;

    // Hearts
    private ImageView heart1, heart2;
    private boolean heart1Liked = false;
    private boolean heart2Liked = false;

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
        animateQuickActionIcons();
        animatePropertyCards();
        setupTypingGlow();
        setupGlassEffect();
        setupHeartInteractivity();
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

        iconBuy = findViewById(R.id.iconBuy);
        iconRent = findViewById(R.id.iconRent);
        iconSell = findViewById(R.id.iconSell);

        micButton = findViewById(R.id.micButton);

        // Hearts
        heart1 = findViewById(R.id.heart1);
        heart2 = findViewById(R.id.heart2);

        // See All Text
        seeAllText = findViewById(R.id.seeAllText);

        // Search bar glow background
        glowDrawable = new GradientDrawable();
        glowDrawable.setColor(Color.WHITE);
        glowDrawable.setCornerRadius(35f);
        glowDrawable.setStroke(3, Color.parseColor("#B0B0B0"));
        searchCardView.setBackground(glowDrawable);
    }

    private void setupClickListeners() {
        searchCardView.setOnClickListener(v -> {
            pressBounce(searchCardView);
            toggleSearchExpand();
        });

        if (micButton != null) {
            micButton.setOnClickListener(v ->
                    Toast.makeText(this, "Voice search coming soon!", Toast.LENGTH_SHORT).show()
            );
        }

        buyLayout.setOnClickListener(v -> Toast.makeText(this, "Browse properties for sale", Toast.LENGTH_SHORT).show());
        rentLayout.setOnClickListener(v -> Toast.makeText(this, "Browse rental properties", Toast.LENGTH_SHORT).show());
        sellLayout.setOnClickListener(v -> Toast.makeText(this, "List your property for sale", Toast.LENGTH_SHORT).show());
        propertyCard1.setOnClickListener(v -> Toast.makeText(this, "Viewing Property 1", Toast.LENGTH_SHORT).show());
        propertyCard2.setOnClickListener(v -> Toast.makeText(this, "Viewing Property 2", Toast.LENGTH_SHORT).show());
        profileIcon.setOnClickListener(v -> Toast.makeText(this, "Profile clicked", Toast.LENGTH_SHORT).show());

        // --- NEW: See All click opens FeaturedGalleryActivity ---
        seeAllText.setOnClickListener(v -> {
            Intent intent = new Intent(HomePage.this, FeaturedGalleryActivity.class);
            startActivity(intent);
        });
    }

    private void setupUI() {
        servicesTitle.setText("Services");
        featuredPropertiesTitle.setText("Featured Properties");
        searchHint.setHint("Search properties, locations...");
    }

    private void setupHeartInteractivity() {
        heart1.setOnClickListener(v -> toggleHeart(heart1, 1));
        heart2.setOnClickListener(v -> toggleHeart(heart2, 2));
    }

    private void toggleHeart(ImageView heart, int heartNumber) {
        boolean liked = (heartNumber == 1) ? heart1Liked : heart2Liked;

        if (!liked) {
            heart.setColorFilter(Color.parseColor("#FF4081"));
            playHeartAnimation(heart);
        } else {
            heart.setColorFilter(Color.parseColor("#B0B0B0"));
        }

        if (heartNumber == 1) heart1Liked = !heart1Liked;
        else heart2Liked = !heart2Liked;
    }

    private void playHeartAnimation(ImageView heart) {
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(heart, "scaleX", 1f, 1.4f, 1f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(heart, "scaleY", 1f, 1.4f, 1f);
        scaleX.setDuration(400);
        scaleY.setDuration(400);
        AnimatorSet set = new AnimatorSet();
        set.playTogether(scaleX, scaleY);
        set.start();

        ValueAnimator colorAnim = ValueAnimator.ofObject(new ArgbEvaluator(),
                Color.parseColor("#FF4081"),
                Color.parseColor("#FF80AB"),
                Color.parseColor("#FF4081"));
        colorAnim.setDuration(700);
        colorAnim.setRepeatCount(1);
        colorAnim.addUpdateListener(animation -> heart.setColorFilter((int) animation.getAnimatedValue()));
        colorAnim.start();
    }

    // --- Existing animation and utility methods below ---
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

    private void applyHeaderAnimations() {
        ObjectAnimator headerFloatY = ObjectAnimator.ofFloat(headerTitle, "translationY", 0f, -15f, 0f);
        headerFloatY.setDuration(4000);
        headerFloatY.setRepeatCount(ValueAnimator.INFINITE);
        headerFloatY.setRepeatMode(ValueAnimator.REVERSE);

        ValueAnimator headerColor = ValueAnimator.ofFloat(0f, 1f);
        headerColor.setDuration(7000);
        headerColor.setRepeatCount(ValueAnimator.INFINITE);
        headerColor.addUpdateListener(a -> {
            float f = (float) a.getAnimatedValue();
            int c = android.graphics.Color.HSVToColor(new float[]{f * 360f, 0.45f, 1f});
            headerTitle.setTextColor(c);
        });

        welcomeText.setScaleX(1f);
        welcomeText.setScaleY(1f);
        welcomeText.setTypeface(Typeface.DEFAULT);
        welcomeText.setPivotX(0);
        welcomeText.setPivotY(0);

        ObjectAnimator welcomeFloatY = ObjectAnimator.ofFloat(welcomeText, "translationY", 0f, -20f, 0f);
        ObjectAnimator welcomeScaleX = ObjectAnimator.ofFloat(welcomeText, "scaleX", 1f, 1.35f, 1f);
        ObjectAnimator welcomeScaleY = ObjectAnimator.ofFloat(welcomeText, "scaleY", 1f, 1.35f, 1f);

        welcomeFloatY.setDuration(4000);
        welcomeScaleX.setDuration(4000);
        welcomeScaleY.setDuration(4000);

        welcomeFloatY.setRepeatCount(ValueAnimator.INFINITE);
        welcomeScaleX.setRepeatCount(ValueAnimator.INFINITE);
        welcomeScaleY.setRepeatCount(ValueAnimator.INFINITE);

        welcomeFloatY.setRepeatMode(ValueAnimator.REVERSE);
        welcomeScaleX.setRepeatMode(ValueAnimator.REVERSE);
        welcomeScaleY.setRepeatMode(ValueAnimator.REVERSE);

        welcomeFloatY.addUpdateListener(animation -> {
            float y = (float) animation.getAnimatedValue();
            if (y < -5f) welcomeText.setTypeface(Typeface.DEFAULT_BOLD);
            else welcomeText.setTypeface(Typeface.DEFAULT);
        });

        AnimatorSet welcomeSet = new AnimatorSet();
        welcomeSet.playTogether(welcomeFloatY, welcomeScaleX, welcomeScaleY);

        AnimatorSet allSet = new AnimatorSet();
        allSet.playTogether(headerFloatY, welcomeSet);
        allSet.start();

        headerColor.start();
    }

    private void animateProfileIcon() {
        ObjectAnimator floatY = ObjectAnimator.ofFloat(profileIcon, "translationY", 0f, -12f, 0f);
        floatY.setDuration(2500);
        floatY.setRepeatCount(ValueAnimator.INFINITE);
        floatY.setRepeatMode(ValueAnimator.REVERSE);

        ObjectAnimator scaleX = ObjectAnimator.ofFloat(profileIcon, "scaleX", 1f, 1.07f, 1f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(profileIcon, "scaleY", 1f, 1.07f, 1f);

        scaleX.setDuration(2500);
        scaleY.setDuration(2500);
        scaleX.setRepeatCount(ValueAnimator.INFINITE);
        scaleY.setRepeatCount(ValueAnimator.INFINITE);

        AnimatorSet set = new AnimatorSet();
        set.playTogether(floatY, scaleX, scaleY);
        set.start();
    }

    private void animateQuickActionIcons() {
        animateIcon(iconBuy, 3000);
        animateIcon(iconRent, 3300);
        animateIcon(iconSell, 3100);
    }

    private void animateIcon(ImageView icon, int duration) {
        ObjectAnimator floatY = ObjectAnimator.ofFloat(icon, "translationY", 0f, -12f, 0f);
        floatY.setDuration(duration);
        floatY.setRepeatCount(ValueAnimator.INFINITE);
        floatY.setRepeatMode(ValueAnimator.REVERSE);

        ObjectAnimator scale = ObjectAnimator.ofFloat(icon, "scaleX", 1f, 1.06f, 1f);
        ObjectAnimator scale2 = ObjectAnimator.ofFloat(icon, "scaleY", 1f, 1.06f, 1f);
        scale.setDuration(duration);
        scale2.setDuration(duration);
        scale.setRepeatCount(ValueAnimator.INFINITE);
        scale2.setRepeatCount(ValueAnimator.INFINITE);

        AnimatorSet set = new AnimatorSet();
        set.playTogether(floatY, scale, scale2);
        set.setInterpolator(new AccelerateDecelerateInterpolator());
        set.start();
    }

    private void animatePropertyCards() {
        animateCard(propertyCard1);
        animateCard(propertyCard2);
    }

    private void animateCard(CardView card) {
        ObjectAnimator anim = ObjectAnimator.ofFloat(card, "translationY", 0f, -8f, 0f);
        anim.setDuration(4500);
        anim.setRepeatMode(ValueAnimator.REVERSE);
        anim.setRepeatCount(ValueAnimator.INFINITE);
        anim.start();
    }

    private void pressBounce(CardView view) {
        AnimatorSet set = new AnimatorSet();
        ObjectAnimator sx = ObjectAnimator.ofFloat(view, "scaleX", 1f, 0.94f, 1f);
        ObjectAnimator sy = ObjectAnimator.ofFloat(view, "scaleY", 1f, 0.94f, 1f);
        set.setDuration(150);
        set.playTogether(sx, sy);
        set.start();
    }

    private void setupTypingGlow() {
        searchHint.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) startGlowAnimation();
                else stopGlowAnimation();
            }
            @Override public void afterTextChanged(Editable s) { }
        });
    }

    private void startGlowAnimation() {
        if (glowAnimator != null && glowAnimator.isRunning()) return;
        glowAnimator = ValueAnimator.ofInt(3, 12);
        glowAnimator.setDuration(800);
        glowAnimator.setRepeatMode(ValueAnimator.REVERSE);
        glowAnimator.setRepeatCount(ValueAnimator.INFINITE);
        glowAnimator.addUpdateListener(a -> {
            int stroke = (int) a.getAnimatedValue();
            glowDrawable.setStroke(stroke, Color.parseColor("#3F51B5"));
        });
        glowAnimator.start();
    }

    private void stopGlowAnimation() {
        if (glowAnimator != null) glowAnimator.cancel();
        glowDrawable.setStroke(3, Color.parseColor("#B0B0B0"));
    }

    private void setupGlassEffect() {
        searchCardView.setAlpha(0.95f);
        searchCardView.setElevation(18f);
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
