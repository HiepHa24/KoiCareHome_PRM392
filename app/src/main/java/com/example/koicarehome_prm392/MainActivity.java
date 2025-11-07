package com.example.koicarehome_prm392;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.koicarehome_prm392.data.db.AppDatabase;
import com.example.koicarehome_prm392.data.entities.User;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Check if user is logged in
        SharedPreferences prefs = getSharedPreferences("users", MODE_PRIVATE);
        long currentUserId = prefs.getLong("current_user_id", -1);
        if (currentUserId == -1) {
            // Not logged in → redirect to LoginActivity
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // ---- Buttons ----
        Button btnFishList = findViewById(R.id.btnFishList);
        Button btnGoToPonds = findViewById(R.id.btn_go_to_ponds);

        btnFishList.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, FishListActivity.class);
            startActivity(intent);
        });

        btnGoToPonds.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, PondListActivity.class);
            startActivity(intent);
        });

        // ---- Initialize DB in background ----
        new Thread(() -> {
            try {
                Log.d("DB", "Attempting to initialize Room DB...");
                AppDatabase db = AppDatabase.getInstance(getApplicationContext());
                // Trigger DB file creation
                User user = db.userDao().findById(1);
                Log.d("DB", "Room DB initialized and connection opened.");
            } catch (Exception e) {
                Log.e("DB", "DB init failed", e);
            }
        }).start();
    }
}
