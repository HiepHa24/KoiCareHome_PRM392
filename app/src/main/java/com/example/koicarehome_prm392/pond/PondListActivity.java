// File: D:/PRM392/KoiCareHome_PRM392/app/src/main/java/com/example/koicarehome_prm392/pond/PondListActivity.javapackage com.example.koicarehome_prm392.pond;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.koicarehome_prm392.R; // Đảm bảo import R từ đúng package
import com.example.koicarehome_prm392.adapter.PondAdapter;
import com.example.koicarehome_prm392.data.entities.Pond;
import com.example.koicarehome_prm392.pond.AddEditPondActivity;
import com.example.koicarehome_prm392.viewmodel.PondViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class PondListActivity extends AppCompatActivity {

    private PondViewModel pondViewModel;
    private long currentUserId;
    private PondAdapter adapter; // Đưa adapter ra làm biến thành viên

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Bạn cần tạo layout R.layout.activity_pond_list
        setContentView(R.layout.activity_pond_list);

        // Lấy userId của người dùng đang đăng nhập từ SharedPreferences
        SharedPreferences prefs = getSharedPreferences("users", MODE_PRIVATE);
        currentUserId = prefs.getLong("current_user_id", -1);

        if (currentUserId == -1) {
            // Xử lý trường hợp không tìm thấy user id (quay về màn hình login)
            Toast.makeText(this, "Lỗi xác thực người dùng", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Thiết lập RecyclerView
        RecyclerView recyclerView = findViewById(R.id.recyclerViewPonds); // Đảm bảo có ID này trong layout
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        adapter = new PondAdapter(); // Khởi tạo adapter
        recyclerView.setAdapter(adapter);

        // Khởi tạo ViewModel
        pondViewModel = new ViewModelProvider(this).get(PondViewModel.class);

        // Quan sát dữ liệu và cập nhật UI khi có thay đổi
        pondViewModel.getPondsByUserId(currentUserId).observe(this, ponds -> {
            adapter.setPonds(ponds);
        });

        // Nút thêm mới hồ
        FloatingActionButton fabAddPond = findViewById(R.id.fabAddPond); // Đảm bảo có ID này trong layout
        fabAddPond.setOnClickListener(v -> {
            Intent intent = new Intent(PondListActivity.this, AddEditPondActivity.class);
            startActivity(intent);
        });

        // *** THÊM CHỨC NĂNG VUỐT ĐỂ XÓA ***
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false; // Không dùng cho chức năng kéo-thả
            }

            // Trong phương thức onSwiped của ItemTouchHelper trong PondListActivity.java

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                Pond pondToDelete = adapter.getPondAt(position);

                pondViewModel.delete(pondToDelete);

                // Thay đổi ở đây: Hiển thị ID của hồ thay vì tên
                Toast.makeText(PondListActivity.this, "Đã xóa hồ số: " + pondToDelete.pondId, Toast.LENGTH_SHORT).show();
            }
        }).attachToRecyclerView(recyclerView); // Gắn ItemTouchHelper vào RecyclerView
    }
}