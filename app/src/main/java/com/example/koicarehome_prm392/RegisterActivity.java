package com.example.koicarehome_prm392;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.koicarehome_prm392.data.db.AppDatabase;
import com.example.koicarehome_prm392.data.entities.User;

import java.util.concurrent.Executors;

public class RegisterActivity extends AppCompatActivity {
    EditText etUser, etPass;
    Button btnRegister, btnBackToLogin;;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etUser = findViewById(R.id.etUsername);
        etPass = findViewById(R.id.etPassword);
        btnRegister = findViewById(R.id.btnRegister);
        btnBackToLogin = findViewById(R.id.btnBackToLogin);

        btnBackToLogin.setOnClickListener(v -> finish());

        btnRegister.setOnClickListener(v -> {
            final String username = etUser.getText().toString().trim();
            final String password = etPass.getText().toString();

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Enter username & password", Toast.LENGTH_SHORT).show();
                return;
            }

            Executors.newSingleThreadExecutor().execute(() -> {
                AppDatabase db = AppDatabase.getInstance(getApplicationContext());
                User existing = db.userDao().findByName(username);
                if (existing != null) {
                    runOnUiThread(() -> Toast.makeText(RegisterActivity.this, "Username already exists", Toast.LENGTH_SHORT).show());
                    return;
                }

                String hash = HashUtils.sha256(password);
                long id = db.userDao().insert(new User(username, hash, System.currentTimeMillis()));

                // save session and go to main
                // SharedPreferences prefs = getSharedPreferences("users", MODE_PRIVATE);
                // prefs.edit().putLong("current_user_id", id).apply();

                runOnUiThread(() -> {
                    Toast.makeText(RegisterActivity.this, "Registered successfully. Remember your username and password.", Toast.LENGTH_LONG).show();
                    // Return to login screen — user must manually login
                    finish();
                });
            });
        });
    }
}
