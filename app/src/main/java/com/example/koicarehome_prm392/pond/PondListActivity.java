// File: D:/PRM392/KoiCareHome_PRM392/app/src/main/java/com/example/koicarehome_prm392/pond/PondListActivity.java

package com.example.koicarehome_prm392.pond;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast; // *** THIẾU IMPORT NÀY ***

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.koicarehome_prm392.R; // *** THIẾU IMPORT NÀY ***
import com.example.koicarehome_prm392.adapter.PondAdapter; // *** THIẾU IMPORT NÀY ***
import com.example.koicarehome_prm392.data.entities.Pond; // *** THIẾU IMPORT NÀY ***
import com.example.koicarehome_prm392.viewmodel.PondViewModel; // *** THIẾU IMPORT NÀY ***
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class PondListActivity extends AppCompatActivity {

    // *** THIẾU KHAI BÁO CÁC BIẾN NÀY ***
    private PondViewModel pondViewModel;
    private long currentUserId;
    private PondAdapter adapter;
    // *** KẾT THÚC PHẦN THIẾU ***

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pond_list);

        // Đoạn code còn lại của bạn đã chính xác, không cần sửa
        SharedPreferences prefs = getSharedPreferences("users", MODE_PRIVATE);
        currentUserId = prefs.getLong("current_user_id", -1);

        if (currentUserId == -1) {
            Toast.makeText(this, "Lỗi xác thực người dùng", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        RecyclerView recyclerView = findViewById(R.id.recyclerViewPonds);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        adapter = new PondAdapter();
        recyclerView.setAdapter(adapter);

        pondViewModel = new ViewModelProvider(this).get(PondViewModel.class);

        pondViewModel.getPondsByUserId(currentUserId).observe(this, ponds -> {
            adapter.setPonds(ponds);
        });

        FloatingActionButton fabAddPond = findViewById(R.id.fabAddPond);
        fabAddPond.setOnClickListener(v -> {
            Intent intent = new Intent(PondListActivity.this, AddEditPondActivity.class);
            startActivity(intent);
        });

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                Pond pondToDelete = adapter.getPondAt(position);
                pondViewModel.delete(pondToDelete);
                Toast.makeText(PondListActivity.this, "Đã xóa hồ số: " + pondToDelete.pondId, Toast.LENGTH_SHORT).show();
            }
        }).attachToRecyclerView(recyclerView);

        adapter.setOnItemClickListener(pond -> {
            Intent intent = new Intent(PondListActivity.this, AddEditPondActivity.class);
            intent.putExtra(AddEditPondActivity.EXTRA_POND_ID, pond.pondId);
            intent.putExtra(AddEditPondActivity.EXTRA_POND_VOLUME, pond.volumeLiters);
            startActivity(intent);
        });
    }
}