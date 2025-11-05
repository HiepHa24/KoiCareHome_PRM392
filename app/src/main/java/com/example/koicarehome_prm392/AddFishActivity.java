package com.example.koicarehome_prm392;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.koicarehome_prm392.ViewModels.FishViewModel;
import com.example.koicarehome_prm392.ViewModels.PondViewModel;
import com.example.koicarehome_prm392.data.entities.Fish;
import com.example.koicarehome_prm392.data.entities.Pond;

import java.util.ArrayList;
import java.util.List;

public class AddFishActivity extends AppCompatActivity {
    private EditText etFishName, etFishColor, etLength, etWeight;
    private Spinner spinnerPond;
    private ImageView ivFishPreview;
    private Button btnSelectImage, btnSaveFish;
    private TextView tvFoodAmount;

    private FishViewModel fishViewModel;
    private PondViewModel pondViewModel;
    private List<Pond> pondList = new ArrayList<>();
    private String selectedImageUri = "";
    private long editFishId = -1;

    private ActivityResultLauncher<Intent> imagePickerLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_fish);

        initViews();
        initViewModels();
        setupImagePicker();
        setupListeners();
        loadPonds();

        // Check if editing
        if (getIntent().hasExtra("FISH_ID")) {
            editFishId = getIntent().getLongExtra("FISH_ID", -1);
            loadFishData(editFishId);
        }
    }

    private void initViews() {
        etFishName = findViewById(R.id.etFishName);
        etFishColor = findViewById(R.id.etFishColor);
        etLength = findViewById(R.id.etLength);
        etWeight = findViewById(R.id.etWeight);
        spinnerPond = findViewById(R.id.spinnerPond);
        ivFishPreview = findViewById(R.id.ivFishPreview);
        btnSelectImage = findViewById(R.id.btnSelectImage);
        btnSaveFish = findViewById(R.id.btnSaveFish);
        tvFoodAmount = findViewById(R.id.tvFoodAmount);
    }

    private void initViewModels() {
        fishViewModel = new ViewModelProvider(this).get(FishViewModel.class);
        pondViewModel = new ViewModelProvider(this).get(PondViewModel.class);
    }

    private void setupImagePicker() {
        imagePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Uri imageUri = result.getData().getData();
                        if (imageUri != null) {
                            selectedImageUri = imageUri.toString();
                            ivFishPreview.setImageURI(imageUri);
                        }
                    }
                }
        );
    }

    private void setupListeners() {
        // Tính lượng thức ăn khi nhập cân nặng
        etWeight.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                calculateFoodAmount();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        btnSelectImage.setOnClickListener(v -> openImagePicker());
        btnSaveFish.setOnClickListener(v -> saveFish());
    }

    private void calculateFoodAmount() {
        String weightStr = etWeight.getText().toString();
        if (!weightStr.isEmpty()) {
            try {
                double weight = Double.parseDouble(weightStr);
                double foodAmount = fishViewModel.calculateFoodAmount(weight);
                tvFoodAmount.setText(String.format("Lượng thức ăn: %.2f gram/ngày", foodAmount));
            } catch (NumberFormatException e) {
                tvFoodAmount.setText("Lượng thức ăn: 0 gram/ngày");
            }
        } else {
            tvFoodAmount.setText("Lượng thức ăn: 0 gram/ngày");
        }
    }

    private void loadPonds() {
        // Assuming userId = 1 for testing
        pondViewModel.getPondsByUserId(1).observe(this, ponds -> {
            if (ponds != null) {
                pondList = ponds;
                List<String> pondNames = new ArrayList<>();
                for (Pond pond : ponds) {
                    pondNames.add("Hồ " + pond.pondId + " - " + pond.volumeLiters + "L");
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<>(
                        this, android.R.layout.simple_spinner_item, pondNames);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerPond.setAdapter(adapter);
            }
        });
    }

    private void loadFishData(long fishId) {
        fishViewModel.getFishById(fishId).observe(this, fish -> {
            if (fish != null) {
                etFishName.setText(fish.fishName);
                etFishColor.setText(fish.fishColor);
                etLength.setText(String.valueOf(fish.length));
                etWeight.setText(String.valueOf(fish.weight));
                selectedImageUri = fish.fishImg != null ? fish.fishImg : "";
                if (!selectedImageUri.isEmpty()) {
                    ivFishPreview.setImageURI(Uri.parse(selectedImageUri));
                }

                // Select correct pond
                for (int i = 0; i < pondList.size(); i++) {
                    if (pondList.get(i).pondId == fish.pondId) {
                        spinnerPond.setSelection(i);
                        break;
                    }
                }
            }
        });
    }

    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        imagePickerLauncher.launch(intent);
    }

    private void saveFish() {
        String name = etFishName.getText().toString().trim();
        String color = etFishColor.getText().toString().trim();
        String lengthStr = etLength.getText().toString().trim();
        String weightStr = etWeight.getText().toString().trim();

        if (name.isEmpty() || color.isEmpty() || lengthStr.isEmpty() || weightStr.isEmpty()) {
            Toast.makeText(this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }

        //ràng buộc tạo hồ trước
        if (pondList.isEmpty()) {
            Toast.makeText(this, "Vui lòng tạo hồ trước", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            double length = Double.parseDouble(lengthStr);
            double weight = Double.parseDouble(weightStr);
            long pondId = pondList.get(spinnerPond.getSelectedItemPosition()).pondId;
            double foodAmount = fishViewModel.calculateFoodAmount(weight);

            if (editFishId == -1) {
                Fish fish = new Fish(pondId, name, color, length, weight,
                        System.currentTimeMillis(), foodAmount, selectedImageUri);
                fishViewModel.insert(fish);
                Toast.makeText(this, "Đã thêm cá thành công", Toast.LENGTH_SHORT).show();
            } else {
                Fish fish = new Fish(pondId, name, color, length, weight,
                        System.currentTimeMillis(), foodAmount, selectedImageUri);
                fish.fishId = editFishId;
                fishViewModel.update(fish);
                Toast.makeText(this, "Đã cập nhật cá thành công", Toast.LENGTH_SHORT).show();
            }

            // ✅ Gửi kết quả OK về cho FishListActivity
            setResult(RESULT_OK);
            finish();

        } catch (NumberFormatException e) {
            Toast.makeText(this, "Vui lòng nhập số hợp lệ", Toast.LENGTH_SHORT).show();
        }
    }
}