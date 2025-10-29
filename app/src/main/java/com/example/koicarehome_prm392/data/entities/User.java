package com.example.koicarehome_prm392.data.entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "users")
public class User {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "userId")
    public long userId;
    @ColumnInfo(name = "userName")
    @NonNull
    public String userName;
    @ColumnInfo(name = "passwordHash")
    @NonNull
    public String passwordHash;
    @ColumnInfo(name = "createdAt")
    public long createdAt;

    public User(@NonNull String userName, @NonNull String passwordHash, long createdAt) {
        this.userName = userName;
        this.passwordHash = passwordHash;
        this.createdAt = createdAt;
    }
}
