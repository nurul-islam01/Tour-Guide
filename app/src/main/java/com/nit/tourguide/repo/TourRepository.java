package com.nit.tourguide.repo;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;
import androidx.room.Update;

import com.nit.tourguide.dao.TourDao;
import com.nit.tourguide.db.TourDatabase;
import com.nit.tourguide.dben.Tour;

import java.util.List;

public class TourRepository {

    private TourDao tourDao;
    private LiveData<List<Tour>> allTours;

    public TourRepository(Application application) {
        TourDatabase database = TourDatabase.getInstance(application);
        tourDao = database.tourDao();
        allTours = tourDao.getAllTours();
    }

    public void insert(Tour tour) {
        new InsertTourAsyncTask(tourDao).execute(tour);
    }

    public void update(Tour tour) {
        new UpdateTourAsyncTask(tourDao).execute(tour);
    }

    public void delete(Tour tour) {
        new DeleteTourAsyncTask(tourDao).execute(tour);
    }

    public void deleteAllTours() {
        new DeleteAllTourAsyncTask(tourDao).execute();
    }

    public LiveData<List<Tour>> getAllTours() {
        return allTours;
    }

    private static class InsertTourAsyncTask extends AsyncTask<Tour, Void, Void> {

        private TourDao tourDao;

        public InsertTourAsyncTask(TourDao tourDao) {
            this.tourDao = tourDao;
        }

        @Override
        protected Void doInBackground(Tour... tours) {
            tourDao.insert(tours[0]);
            return null;
        }
    }
    private static class UpdateTourAsyncTask extends AsyncTask<Tour, Void, Void> {

        private TourDao tourDao;

        public UpdateTourAsyncTask(TourDao tourDao) {
            this.tourDao = tourDao;
        }

        @Override
        protected Void doInBackground(Tour... tours) {
            tourDao.update(tours[0]);
            return null;
        }
    }
    private static class DeleteTourAsyncTask extends AsyncTask<Tour, Void, Void> {

        private TourDao tourDao;

        public DeleteTourAsyncTask(TourDao tourDao) {
            this.tourDao = tourDao;
        }

        @Override
        protected Void doInBackground(Tour... tours) {
            tourDao.delete(tours[0]);
            return null;
        }
    }
    private static class DeleteAllTourAsyncTask extends AsyncTask<Tour, Void, Void> {

        private TourDao tourDao;

        public DeleteAllTourAsyncTask(TourDao tourDao) {
            this.tourDao = tourDao;
        }

        @Override
        protected Void doInBackground(Tour... tours) {
            tourDao.deleteAllTour();
            return null;
        }
    }

}
