package com.nit.tourguide.ui.tour;

import android.app.Application;

import androidx.annotation.Nullable;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.nit.tourguide.dben.Tour;
import com.nit.tourguide.repo.TourRepository;

import java.util.List;

public class TourViewModel extends AndroidViewModel {

    private TourRepository repository;
    private LiveData<List<Tour>> allTours;

    public TourViewModel(@Nullable Application application) {
        super(application);
        repository = new TourRepository(application);
        allTours = repository.getAllTours();
    }

    public void insert(Tour tour) {
        repository.insert(tour);
    }

    public void update(Tour tour) {
        repository.update(tour);
    }

    public void delete(Tour tour) {
        repository.delete(tour);
    }

    public void deleteAllTours() {
        repository.deleteAllTours();
    }

    public LiveData<List<Tour>> getAllTours() {
        return allTours;
    }
}
