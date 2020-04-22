package com.nit.tourguide.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.nit.tourguide.dben.Expense;

import java.util.List;

@Dao
public interface ExpenseDao {

    @Insert
    void insert(Expense expense);

    @Update
    void update(Expense expense);

    @Delete
    void delete(Expense expense);

    @Query("SELECT * FROM expense WHERE tourId=(:tourId)")
    LiveData<List<Expense>> getAllExpense(int tourId);

    @Query("DELETE FROM expense")
    void deleteAllExpense();
}
