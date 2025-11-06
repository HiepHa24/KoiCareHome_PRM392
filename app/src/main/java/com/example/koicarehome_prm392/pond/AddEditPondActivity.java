// File: com/example/koicarehome_prm392/pond/AddEditPondActivity.java
package com.example.koicarehome_prm392.pond;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.koicarehome_prm392.R;
import com.example.koicarehome_prm392.data.entities.Pond;
import com.example.koicarehome_prm392.viewmodel.PondViewModel;

public class AddEditPondActivity extends AppCompatActivity {

    private EditText etPondVolume;
    private Button btnSavePond;
    private PondViewModel pondViewModel; // Sử dụng ViewModel để insert
    private long currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_pond);

        etPondVolume = findViewById(R.id.etPondVolume);
        btnSavePond = findViewById(R.id.btnSavePond);

        // Khởi tạo ViewModel
        pondViewModel = new ViewModelProvider(this).get(PondViewModel.class);

        // Lấy userId từ SharedPreferences
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
        String volumeStr = etPondVolume.getText().toString().trim();

        if (volumeStr.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập thể tích hồ", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            double volume = Double.parseDouble(volumeStr);
            if (volume <= 0) {
                Toast.makeText(this, "Thể tích phải lớn hơn 0", Toast.LENGTH_SHORT).show();
                return;
            }

            // Tính toán lượng khoáng theo công thức
            // Lượng muối (kg) = Thể tích (lít) * 0.3%
            double mineralAmount = volume * 0.003;
            long currentTime = System.currentTimeMillis();

            // Tạo đối tượng Pond mới theo đúng constructor của bạn
            Pond newPond = new Pond(currentUserId, volume, mineralAmount, currentTime);

            // Sử dụng ViewModel để thực hiện việc insert
            pondViewModel.insert(newPond);

            // Thông báo và kết thúc Activity
            Toast.makeText(AddEditPondActivity.this, "Đã lưu hồ thành công!", Toast.LENGTH_SHORT).show();
            String saltMessage = String.format("Lượng khoáng (muối) gợi ý: %.2f kg", mineralAmount);
            Toast.makeText(AddEditPondActivity.this, saltMessage, Toast.LENGTH_LONG).show();

            finish(); // Quay lại màn hình danh sách

        } catch (NumberFormatException e) {
            Toast.makeText(this, "Thể tích không hợp lệ", Toast.LENGTH_SHORT).show();
        }
    }
}