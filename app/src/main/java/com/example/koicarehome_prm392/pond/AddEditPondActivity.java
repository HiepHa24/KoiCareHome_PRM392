// File: AddEditPondActivity.java
package com.example.koicarehome_prm392.pond;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.koicarehome_prm392.data.db.AppDatabase;
import com.example.koicarehome_prm392.data.entities.Pond;
import java.util.concurrent.Executors;

public class AddEditPondActivity extends AppCompatActivity {

    private EditText etPondName, etPondVolume;
    private Button btnSavePond;
    private long currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_pond);

        etPondName = findViewById(R.id.etPondName);
        etPondVolume = findViewById(R.id.etPondVolume);
        btnSavePond = findViewById(R.id.btnSavePond);

        // Lấy userId
        SharedPreferences prefs = getSharedPreferences("users", MODE_PRIVATE);
        currentUserId = prefs.getLong("current_user_id", -1);

        if (currentUserId == -1) {
            Toast.makeText(this, "Lỗi xác thực người dùng.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        btnSavePond.setOnClickListener(v -> savePond());
    }

    private void savePond() {
        String name = etPondName.getText().toString().trim();
        String volumeStr = etPondVolume.getText().toString().trim();

        if (name.isEmpty() || volumeStr.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            double volume = Double.parseDouble(volumeStr);
            Pond pond = new Pond(currentUserId, name, volume);

            Executors.newSingleThreadExecutor().execute(() -> {
                AppDatabase db = AppDatabase.getInstance(getApplicationContext());
                db.pondDao().insert(pond);

                runOnUiThread(() -> {
                    Toast.makeText(AddEditPondActivity.this, "Đã lưu hồ thành công!", Toast.LENGTH_SHORT).show();
                    // Hiển thị lượng muối cần thiết theo Flow 2
                    String saltMessage = String.format("Lượng khoáng (muối) cần cho hồ: %.2f kg", pond.requiredSaltAmount);
                    Toast.makeText(AddEditPondActivity.this, saltMessage, Toast.LENGTH_LONG).show();
                    finish(); // Quay lại màn hình danh sách
                });
            });
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Thể tích không hợp lệ", Toast.LENGTH_SHORT).show();
        }
    }
}