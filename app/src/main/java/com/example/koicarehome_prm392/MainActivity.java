package com.example.koicarehome_prm392;

import android.os.Bundle;
import android.util.Log;

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
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        new Thread(() -> {
            try {
                Log.d("DB", "Attempting to initialize Room DB...");
                AppDatabase db = AppDatabase.getInstance(getApplicationContext());
                // Run any query to force the .db file to be created.
                User user = db.userDao().findById(1);
                Log.d("DB", "Room DB initialized and connection opened.");
            } catch (Exception e) {
                Log.e("DB", "DB init failed", e);
            }
        }).start();
    }
}