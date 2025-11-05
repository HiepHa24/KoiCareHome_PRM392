package com.example.koicarehome_prm392.ViewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.koicarehome_prm392.data.entities.Pond;

import java.util.ArrayList;
import java.util.List;

public class PondViewModel extends ViewModel {

    // code tạm thời
    private MutableLiveData<List<Pond>> pondsLiveData = new MutableLiveData<>();

    public PondViewModel() {
        List<Pond> dummyPonds = new ArrayList<>();

        // Ví dụ 2 hồ tạm
        dummyPonds.add(new Pond(1, 2000, 6, System.currentTimeMillis()));
        dummyPonds.add(new Pond(1, 4000, 12, System.currentTimeMillis()));

        pondsLiveData.setValue(dummyPonds);
    }

    public LiveData<List<Pond>> getPondsByUserId(long userId) {
        return pondsLiveData;
    }
}
