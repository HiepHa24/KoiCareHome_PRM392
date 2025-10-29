package com.example.koicarehome_prm392.data.entities;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "ponds",
        foreignKeys = @ForeignKey(entity = User.class,
                parentColumns = "userId",
                childColumns = "userId",
                onDelete = ForeignKey.CASCADE),
        indices = {@Index("userId")})
public class Pond {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "pondId")
    public long pondId;

    @ColumnInfo(name = "userId")
    public long userId;

    @ColumnInfo(name = "volumeLiters")
    public double volumeLiters;

    @ColumnInfo(name = "mineralAmount")
    public double mineralAmount;

    @ColumnInfo(name = "createdAt")
    public long createdAt;

    public Pond(long userId, double volumeLiters, double mineralAmount, long createdAt) {
        this.userId = userId;
        this.volumeLiters = volumeLiters;
        this.mineralAmount = mineralAmount;
        this.createdAt = createdAt;
    }
}
