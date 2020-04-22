package com.nit.tourguide.db;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.nit.tourguide.dao.ExpenseDao;
import com.nit.tourguide.dao.TourDao;
import com.nit.tourguide.dben.Expense;
import com.nit.tourguide.dben.Tour;

import java.util.Date;

@Database(entities = {Tour.class, Expense.class}, version = 1)
public abstract class TourDatabase extends RoomDatabase {

    private static TourDatabase instance;

    public abstract TourDao tourDao();
    public abstract ExpenseDao expenseDao();

    public static synchronized TourDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(), TourDatabase.class , "tour_database")
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallback)
                    .build();

        }
        return instance;
    }

    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback(){
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateDBAsyncTask(instance).execute();
        }
    };

    private static class PopulateDBAsyncTask extends AsyncTask<Void, Void, Void> {

        TourDao tourDao;

        public PopulateDBAsyncTask(TourDatabase db) {
            this.tourDao = db.tourDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            this.tourDao.insert(new Tour("sample title", "test place", 2547, new Date().toLocaleString()));
            return null;
        }
    }
}
