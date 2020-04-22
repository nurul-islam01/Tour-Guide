package com.nit.tourguide.repo;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.nit.tourguide.dao.ExpenseDao;
import com.nit.tourguide.db.TourDatabase;
import com.nit.tourguide.dben.Expense;

import java.util.List;

public class ExpenseRepository {

    private ExpenseDao expenseDao;
    private LiveData<List<Expense>> allExpense;

    public ExpenseRepository(Application application) {
        TourDatabase database = TourDatabase.getInstance(application);
        expenseDao = database.expenseDao();
    }

    public void setAllExpense(int tourId) {
        allExpense = expenseDao.getAllExpense(tourId);
    }

    public LiveData<List<Expense>> getAllExpense() {
        return allExpense;
    }

    public void insert(Expense expense) {
        new InsertExpenseAsyncTask(expenseDao).execute(expense);
    }

    public void update(Expense expense) {
        new UpdateExpenseAsyncTask(expenseDao).execute(expense);
    }

    public void delete(Expense expense) {
        new DeleteExpenseAsyncTask(expenseDao).execute(expense);
    }

    public void deleteAllExpense() {
        new DeleteAllExpenseAsyncTask(expenseDao).execute();
    }

    private static class InsertExpenseAsyncTask extends AsyncTask<Expense, Void, Void> {
        private ExpenseDao expenseDao;

        public InsertExpenseAsyncTask(ExpenseDao expenseDao) {
            this.expenseDao = expenseDao;
        }

        @Override
        protected Void doInBackground(Expense... expenses) {
            expenseDao.insert(expenses[0]);
            return null;
        }
    }
    private static class UpdateExpenseAsyncTask extends AsyncTask<Expense, Void, Void> {
        private ExpenseDao expenseDao;

        public UpdateExpenseAsyncTask(ExpenseDao expenseDao) {
            this.expenseDao = expenseDao;
        }

        @Override
        protected Void doInBackground(Expense... expenses) {
            expenseDao.update(expenses[0]);
            return null;
        }
    }

    private static class DeleteExpenseAsyncTask extends AsyncTask<Expense, Void, Void> {
        private ExpenseDao expenseDao;

        public DeleteExpenseAsyncTask(ExpenseDao expenseDao) {
            this.expenseDao = expenseDao;
        }

        @Override
        protected Void doInBackground(Expense... expenses) {
            expenseDao.delete(expenses[0]);
            return null;
        }
    }

    private static class DeleteAllExpenseAsyncTask extends AsyncTask<Void, Void, Void> {
        private ExpenseDao expenseDao;

        public DeleteAllExpenseAsyncTask(ExpenseDao expenseDao) {
            this.expenseDao = expenseDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            expenseDao.deleteAllExpense();
            return null;
        }
    }



}
