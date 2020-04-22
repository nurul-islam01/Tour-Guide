package com.nit.tourguide.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.nit.tourguide.dben.Tour;

import java.util.List;

@Dao
public interface TourDao {

    @Insert
    void insert(Tour tour);

    @Update
    void update(Tour tour);

    @Delete
    void delete(Tour tour);

    @Query("DELETE FROM tour_table")
    void deleteAllTour();

    @Query("DELETE FROM expense WHERE expense.tourId = (:id)")
    void deleteWithTours(int id);

    @Query("SELECT * FROM tour_table")
    LiveData<List<Tour>> getAllTours();
}
