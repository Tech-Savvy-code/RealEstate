package com.example.realestate;

import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;

public class SignInActivity extends AppCompatActivity {

    private TextInputEditText emailInput, passwordInput;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_in);

        mAuth = FirebaseAuth.getInstance();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.signInLayout), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        emailInput = findViewById(R.id.emailInput);
        passwordInput = findViewById(R.id.passwordInput);
        Button signInButton = findViewById(R.id.signInButton);
        ImageButton goBackButton = findViewById(R.id.goBackButton);
        TextView signUpLink = findViewById(R.id.signUpLink);
        TextView forgotPassword = findViewById(R.id.forgotPassword);

        goBackButton.setOnClickListener(v -> onBackPressed());

        signInButton.setOnClickListener(v -> validateAndSignIn());

        signUpLink.setOnClickListener(v ->
                startActivity(new Intent(SignInActivity.this, SignUpActivity.class))
        );

        // Forgot Password (Styled Dialog)
        forgotPassword.setOnClickListener(v -> showForgotPasswordDialog());
    }

    private void validateAndSignIn() {
        String email = emailInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailInput.setError("Enter a valid email");
            return;
        }

        if (password.length() < 6) {
            passwordInput.setError("Password must be at least 6 characters");
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(this, "Login successful!", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(SignInActivity.this, HomePage.class));
                        finish();
                    } else {
                        Toast.makeText(this,
                                task.getException().getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void showForgotPasswordDialog() {

        // Email input field
        TextInputEditText input = new TextInputEditText(this);
        input.setHint("Enter your registered email");
        input.setInputType(android.text.InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        input.setTextColor(getResources().getColor(android.R.color.black));
        input.setHintTextColor(getResources().getColor(android.R.color.darker_gray));
        input.setPadding(40, 40, 40, 40);

        // Rounded background
        GradientDrawable bg = new GradientDrawable();
        bg.setCornerRadius(24);
        bg.setColor(0xFFFFFFFF);
        input.setBackground(bg);

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Reset Password")
                .setMessage("We will send a password reset link to your email.")
                .setView(input)
                .setPositiveButton("Send", null)
                .setNegativeButton("Cancel", (d, w) -> d.dismiss())
                .create();

        dialog.setOnShowListener(d -> {
            Button sendBtn = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            sendBtn.setOnClickListener(v -> {
                String email = input.getText().toString().trim();

                if (email.isEmpty()) {
                    Toast.makeText(this, "Email is required", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    Toast.makeText(this, "Enter a valid email", Toast.LENGTH_SHORT).show();
                    return;
                }

                mAuth.sendPasswordResetEmail(email)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Toast.makeText(this,
                                        "Reset link sent! Check your email.",
                                        Toast.LENGTH_LONG).show();
                                dialog.dismiss();
                            } else {
                                Toast.makeText(this,
                                        task.getException().getMessage(),
                                        Toast.LENGTH_LONG).show();
                            }
                        });
            });
        });

        dialog.show();
    }
}
 