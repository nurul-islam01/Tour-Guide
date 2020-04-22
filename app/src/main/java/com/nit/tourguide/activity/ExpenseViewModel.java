package com.nit.tourguide.activity;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.nit.tourguide.dben.Expense;
import com.nit.tourguide.dben.Tour;
import com.nit.tourguide.repo.ExpenseRepository;
import com.nit.tourguide.repo.TourRepository;

import java.util.List;

public class ExpenseViewModel extends AndroidViewModel {

    private ExpenseRepository repository;
    private TourRepository tourRepository;
    private LiveData<List<Expense>> allExpense;
    public ExpenseViewModel(@NonNull Application application) {
        super(application);
        repository = new ExpenseRepository(application);
        tourRepository = new TourRepository(application);
    }


    public LiveData<List<Expense>> getAllExpense(int tourId) {
        repository.setAllExpense(tourId);
        allExpense = repository.getAllExpense();
        return allExpense;
    }

    public void updateTour(Tour tour) {
        tourRepository.update(tour);
    }

    public void insert(Expense expense) {
        repository.insert(expense);
    }

    public void update(Expense expense) {
        repository.update(expense);
    }

    public void delete(Expense expense) {
        repository.delete(expense);
    }

    public void deleteAllExpense() {
        repository.deleteAllExpense();
    }
}
