package com.nit.tourguide.dben;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

@Entity(tableName = "tour_table")
public class Tour implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private int id;

    private String title;
    private String place;
    private int budget;
    private String date;

    public Tour(String title, String place, int budget, String date) {
        this.title = title;
        this.place = place;
        this.budget = budget;
        this.date = date;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getPlace() {
        return place;
    }

    public int getBudget() {
        return budget;
    }

    public String getDate() {
        return date;
    }

    @Override
    public String toString() {
        return "Tour{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", place='" + place + '\'' +
                ", budget=" + budget +
                ", date='" + date + '\'' +
                '}';
    }
}
