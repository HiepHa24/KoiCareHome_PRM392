package com.example.koicarehome_prm392.data.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.koicarehome_prm392.data.entities.Fish;
import java.util.List;

@Dao
public interface FishDao {
    @Insert
    long insert(Fish fish);

    @Query("SELECT * FROM fish WHERE pondId = :pondId")
    List<Fish> getFishForPond(long pondId);
}
