package com.example.koicarehome_prm392;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.koicarehome_prm392.Adapters.FishAdapter;
import com.example.koicarehome_prm392.ViewModels.FishViewModel;
import com.example.koicarehome_prm392.data.entities.Fish;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class FishListActivity extends AppCompatActivity implements FishAdapter.OnFishClickListener {
    private RecyclerView rvFishList;
    private FishAdapter adapter;
    private FishViewModel fishViewModel;
    private FloatingActionButton fabAddFish;
    private ActivityResultLauncher<Intent> addFishLauncher;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fish_list);

        initViews();
        setupRecyclerView();
        setupLauncher();
        observeData();
    }

    private void initViews() {
        rvFishList = findViewById(R.id.rvFishList);
        fabAddFish = findViewById(R.id.fabAddFish);
        fishViewModel = new ViewModelProvider(this).get(FishViewModel.class);
    }

    private void setupRecyclerView() {
        adapter = new FishAdapter(this);
        rvFishList.setLayoutManager(new LinearLayoutManager(this));
        rvFishList.setAdapter(adapter);

        fabAddFish.setOnClickListener(v -> {
            Intent intent = new Intent(this, AddFishActivity.class);
            startActivity(intent);
        });
    }

    private void setupLauncher() {
        addFishLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        Toast.makeText(this, "Cập nhật danh sách cá...", Toast.LENGTH_SHORT).show();
                        // Không cần observe lại, Room LiveData sẽ tự cập nhật
                    }
                }
        );
    }

    private void observeData() {
        fishViewModel.getAllFish().observe(this, fishList -> {
            if (fishList != null) {
                adapter.setFishList(fishList);
            }
        });
    }

    @Override
    public void onEditClick(Fish fish) {
        Intent intent = new Intent(this, AddFishActivity.class);
        intent.putExtra("FISH_ID", fish.fishId);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        fishViewModel.getAllFish().observe(this, fishList -> {
            if (fishList != null) {
                adapter.setFishList(fishList);
            }
        });
    }

    @Override
    public void onDeleteClick(Fish fish) {
        new AlertDialog.Builder(this)
                .setTitle("Xóa cá")
                .setMessage("Bạn có chắc muốn xóa con cá này không?")
                .setPositiveButton("Xóa", (dialog, which) -> {
                    fishViewModel.delete(fish);
                    Toast.makeText(this, "Đã xóa cá thành công", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Hủy", null)
                .show();
    }
}

