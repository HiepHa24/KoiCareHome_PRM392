// File: com/example/koicarehome_prm392/viewmodel/PondViewModel.java
package com.example.koicarehome_prm392.viewmodel;import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.example.koicarehome_prm392.data.db.AppDatabase;
import com.example.koicarehome_prm392.data.dao.PondDao;
import com.example.koicarehome_prm392.data.entities.Pond;

import java.util.List;
import java.util.concurrent.Executors; // Import Executors

public class PondViewModel extends AndroidViewModel {

    private PondDao pondDao;

    public PondViewModel(@NonNull Application application) {
        super(application);
        AppDatabase database = AppDatabase.getInstance(application);
        pondDao = database.pondDao();
    }

    public LiveData<List<Pond>> getPondsByUserId(long userId) {
        return pondDao.getPondsByUserId(userId);
    }

    // *** PHƯƠNG THỨC DELETE CẦN THÊM ***
    public void delete(Pond pond) {
        // Thực thi việc xóa trên một luồng riêng để không chặn luồng UI
        Executors.newSingleThreadExecutor().execute(() -> {
            pondDao.delete(pond);
        });
    }

    // Các phương thức khác như insert, update có thể được thêm ở đây
    public void insert(Pond pond) {
        Executors.newSingleThreadExecutor().execute(() -> {
            pondDao.insert(pond);
        });
    }

    public void update(Pond pond) {
        Executors.newSingleThreadExecutor().execute(() -> {
            pondDao.update(pond);
        });
    }
}