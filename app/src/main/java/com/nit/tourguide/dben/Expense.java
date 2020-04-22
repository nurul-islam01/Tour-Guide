package com.nit.tourguide.dben;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "expense", foreignKeys = @ForeignKey(entity = Tour.class, parentColumns = "id", childColumns = "tourId"))
public class Expense {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private int tourId;

    private String comment;

    private float amount;

    public Expense(int tourId, String comment, float amount) {
        this.tourId = tourId;
        this.comment = comment;
        this.amount = amount;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public int getTourId() {
        return tourId;
    }

    public String getComment() {
        return comment;
    }

    public float getAmount() {
        return amount;
    }

    @Override
    public String toString() {
        return "Expense{" +
                "id=" + id +
                ", tourId=" + tourId +
                ", comment='" + comment + '\'' +
                ", amount=" + amount +
                '}';
    }
}
