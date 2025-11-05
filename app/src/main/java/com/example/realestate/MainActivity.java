package com.example.realestate;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private static final int SPLASH_DELAY = 3500; // 3.5 seconds
    private MediaPlayer splashSound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Handle window insets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize views
        ImageView logo = findViewById(R.id.logoImage);
        TextView topTitle = findViewById(R.id.topTitle);
        TextView tagline = findViewById(R.id.tagline);

        // Load animations
        Animation fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        Animation zoomIn = AnimationUtils.loadAnimation(this, R.anim.zoom_in);
        Animation slideUp = AnimationUtils.loadAnimation(this, R.anim.slide_up);

        // Apply animations
        logo.startAnimation(zoomIn);
        topTitle.startAnimation(fadeIn);
        tagline.startAnimation(slideUp);

        // Initialize and play sound
        playSplashSound();

        // Move to next activity after delay
        new Handler().postDelayed(() -> {
            stopSplashSound();
            Intent intent = new Intent(MainActivity.this, MainActivity.class); // Change this to your next screen
            startActivity(intent);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            finish();
        }, SPLASH_DELAY);
    }

    // 🎵 Play the splash sound with smooth fade-in
    private void playSplashSound() {
        splashSound = MediaPlayer.create(this, R.raw.homevista);
        if (splashSound != null) {
            splashSound.setVolume(0.5f, 0.5f); // Start at half volume
            splashSound.start();
            fadeInSound();
        }
    }

    // Smooth fade-in for sound
    private void fadeInSound() {
        final int fadeDuration = 1000; // 1 second
        final int fadeSteps = 20;
        final float startVolume = 0.5f;
        final float deltaVolume = (1f - startVolume) / fadeSteps;

        new Thread(() -> {
            try {
                for (int i = 1; i <= fadeSteps; i++) {
                    float volume = startVolume + deltaVolume * i;
                    splashSound.setVolume(volume, volume);
                    Thread.sleep(fadeDuration / fadeSteps);
                }
            } catch (InterruptedException ignored) {}
        }).start();
    }

    // Smooth fade-out before stopping
    private void stopSplashSound() {
        if (splashSound != null) {
            final int fadeDuration = 800;
            final int fadeSteps = 20;
            final float deltaVolume = 1f / fadeSteps;

            new Thread(() -> {
                try {
                    for (int i = fadeSteps; i >= 0; i--) {
                        float volume = i * deltaVolume;
                        splashSound.setVolume(volume, volume);
                        Thread.sleep(fadeDuration / fadeSteps);
                    }
                    splashSound.stop();
                    splashSound.release();
                } catch (Exception ignored) {}
            }).start();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopSplashSound();
    }
}
