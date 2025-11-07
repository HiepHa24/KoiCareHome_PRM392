package com.example.koicarehome_prm392.data.entities;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "fish",
        foreignKeys = @ForeignKey(entity = Pond.class,
                parentColumns = "pondId",
                childColumns = "pondId",
                onDelete = ForeignKey.CASCADE),
        indices = {@Index("pondId")})
public class Fish {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "fishId")
    public long fishId;

    @ColumnInfo(name = "pondId")
    public long pondId;
    @ColumnInfo(name = "fishName")
    public String fishName;
    @ColumnInfo(name = "fishColor")
    public String fishColor;
    @ColumnInfo(name = "lengthCm")
    public double length;
    @ColumnInfo(name = "weightGrams")
    public double weight;
    @ColumnInfo(name = "addDate")
    public long addDate;
    @ColumnInfo(name = "foodAmount")
    public double foodAmount;
    @ColumnInfo(name = "fishImg") // store URI/path as string
    public String fishImg;
    public Fish(long pondId, String fishName, String fishColor,
                double length, double weight, long addDate, double foodAmount, String fishImg) {
        this.pondId = pondId;
        this.fishName = fishName;
        this.fishColor = fishColor;
        this.length = length;
        this.weight = weight;
        this.addDate = addDate;
        this.foodAmount = foodAmount;
        this.fishImg = fishImg;
    }
}
