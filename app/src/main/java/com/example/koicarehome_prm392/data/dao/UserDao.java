package com.example.koicarehome_prm392.data.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.koicarehome_prm392.data.entities.User;

@Dao
public interface UserDao {
    @Insert
    long insert(User user);
    @Query("SELECT * FROM users WHERE userName = :name LIMIT 1")
    User findByName(String name);
    @Query("SELECT * FROM users WHERE userId = :id")
    User findById(long id);
}
