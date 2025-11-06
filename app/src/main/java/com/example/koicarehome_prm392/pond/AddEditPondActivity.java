// File: com/example/koicarehome_prm392/pond/AddEditPondActivity.java
package com.example.koicarehome_prm392.pond;

import android.content.Intent; // *** THÊM MỚI ***
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

    // *** THÊM MỚI: Các hằng số để truyền dữ liệu qua Intent ***
    public static final String EXTRA_POND_ID = "com.example.koicarehome_prm392.EXTRA_POND_ID";
    public static final String EXTRA_POND_VOLUME = "com.example.koicarehome_prm392.EXTRA_POND_VOLUME";

    private EditText etPondVolume;
    private Button btnSavePond;
    private PondViewModel pondViewModel;
    private long currentUserId;

    private long pondIdToEdit = -1; // Biến để xác định chế độ Sửa. Mặc định là -1 (Thêm mới).

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_pond);

        etPondVolume = findViewById(R.id.etPondVolume);
        btnSavePond = findViewById(R.id.btnSavePond);
        pondViewModel = new ViewModelProvider(this).get(PondViewModel.class);

        SharedPreferences prefs = getSharedPreferences("users", MODE_PRIVATE);
        currentUserId = prefs.getLong("current_user_id", -1);

        if (currentUserId == -1) {
            Toast.makeText(this, "Lỗi xác thực người dùng.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // *** THÊM MỚI: Kiểm tra xem đây là chế độ Sửa hay Thêm mới ***
        Intent intent = getIntent();
        if (intent.hasExtra(EXTRA_POND_ID)) {
            // Đây là chế độ Sửa
            setTitle("Sửa thông tin hồ"); // Đổi tiêu đề của Activity
            pondIdToEdit = intent.getLongExtra(EXTRA_POND_ID, -1);
            double volume = intent.getDoubleExtra(EXTRA_POND_VOLUME, 0);
            etPondVolume.setText(String.valueOf(volume)); // Hiển thị thể tích cũ lên EditText
        } else {
            // Đây là chế độ Thêm mới
            setTitle("Thêm hồ mới");
        }
        // *** KẾT THÚC PHẦN THÊM MỚI ***

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

            double mineralAmount = volume * 0.003;
            long currentTime = System.currentTimeMillis();

            // *** THAY ĐỔI LOGIC LƯU ***
            if (pondIdToEdit == -1) {
                // CHẾ ĐỘ THÊM MỚI: Tạo Pond mới và insert
                Pond newPond = new Pond(currentUserId, volume, mineralAmount, currentTime);
                pondViewModel.insert(newPond);
                Toast.makeText(this, "Đã thêm hồ thành công!", Toast.LENGTH_SHORT).show();
            } else {
                // CHẾ ĐỘ SỬA: Tạo Pond mới, gán lại ID cũ và update
                Pond updatedPond = new Pond(currentUserId, volume, mineralAmount, currentTime);
                updatedPond.pondId = pondIdToEdit; // *** CỰC KỲ QUAN TRỌNG: Gán lại ID cũ ***
                pondViewModel.update(updatedPond);
                Toast.makeText(this, "Đã cập nhật hồ thành công!", Toast.LENGTH_SHORT).show();
            }

            finish(); // Quay lại màn hình danh sách

        } catch (NumberFormatException e) {
            Toast.makeText(this, "Thể tích không hợp lệ", Toast.LENGTH_SHORT).show();
        }
    }
}