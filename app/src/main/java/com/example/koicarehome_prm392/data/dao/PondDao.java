package com.example.koicarehome_prm392.data.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.koicarehome_prm392.data.entities.Pond;

import java.util.List;

@Dao
public interface PondDao {
    @Insert
    long insert(Pond pond);
    @Query("SELECT * FROM ponds WHERE userId = :userId")
    List<Pond> getPondsForUser(long userId);
}
