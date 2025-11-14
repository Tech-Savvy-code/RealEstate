package com.example.realestate;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class HomePage extends AppCompatActivity {

    TextView greetingText, userStatus;
    Button logoutBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home_page);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        greetingText = findViewById(R.id.greetingText);
        userStatus = findViewById(R.id.userStatus);
        logoutBtn = findViewById(R.id.logoutBtn);

        // Receive user type from sign-in/sign-up/guest
        String type = getIntent().getStringExtra("USER_TYPE");

        if (type == null) type = "GUEST";

        switch (type) {
            case "SIGN_IN":
                greetingText.setText("Welcome Back");
                userStatus.setText("Signed-in User");
                break;

            case "SIGN_UP":
                greetingText.setText("Account Created!");
                userStatus.setText("New User");
                break;

            default:
                greetingText.setText("Welcome");
                userStatus.setText("Guest User");
                break;
        }

        logoutBtn.setOnClickListener(v -> {
            Intent i = new Intent(HomePage.this, WelcomeActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
            finish();
        });
    }
}
