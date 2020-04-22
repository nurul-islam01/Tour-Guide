package com.nit.tourguide.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nit.tourguide.R;
import com.nit.tourguide.activity.adapter.ExpenseAdapter;
import com.nit.tourguide.dben.Expense;
import com.nit.tourguide.dben.Tour;

import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class ExpenseActivity extends AppCompatActivity implements ExpenseAdapter.ExpenseListener {
    private static final String TAG = ExpenseActivity.class.getSimpleName();

    private Tour tour;
    private float totalAmount = 0 ;
    private ExpenseViewModel viewModel;
    private ExpenseAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        viewModel = ViewModelProviders.of(this).get(ExpenseViewModel.class);
        RecyclerView expense_recycler = findViewById(R.id.expense_recycler);
        ImageView add_budget = findViewById(R.id.add_budget);
        ImageView add_expense = findViewById(R.id.add_expense);
        TextView budget_status = findViewById(R.id.budget_status);

        adapter = new ExpenseAdapter(this);
        adapter.setExpenseListener(this);
        try {
            Intent data = getIntent();
            tour = (Tour) data.getSerializableExtra("tour");
            getSupportActionBar().setTitle(tour.getTitle());
        }catch (Exception e){
            Log.d(TAG, "onCreate: " + e.getMessage());
        }

        add_budget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addBudget();
            }
        });

        add_expense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tour.getBudget() <= totalAmount) {
                    Toast.makeText(ExpenseActivity.this, "Budget is complete", Toast.LENGTH_SHORT).show();
                    Toast.makeText(ExpenseActivity.this, "Please increase budget", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    addExpense();
                }
            }
        });

        viewModel.getAllExpense(tour.getId()).observe(this, new Observer<List<Expense>>() {
            @Override
            public void onChanged(List<Expense> expenses) {
                if (expenses != null && expenses.size() > 0) {
                    float amount = 0;
                    for (Expense expense: expenses) {
                        amount = amount + expense.getAmount();
                    }
                    totalAmount = amount;
                    int percent = (int) ((totalAmount * 100) / tour.getBudget());
                    budget_status.setText( "Status : " + tour.getBudget() + "/" + amount + ", " + percent + "%");
                    adapter.setExpenses(expenses);
                    expense_recycler.setAdapter(adapter);
                }
            }
        });
    }

    private void addBudget() {
        SweetAlertDialog alertDialog = new SweetAlertDialog(this, SweetAlertDialog.NORMAL_TYPE);
        alertDialog.setTitle("Add Budget");
        EditText editText = new EditText(this);
        editText.setHint("Add Budget");
        editText.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
        alertDialog.setCustomView(editText);
        alertDialog.setConfirmButton("Add", new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                int amount = Integer.parseInt(editText.getText().toString().trim());
                int addedAmount = tour.getBudget() + amount;
                Tour t = new Tour(tour.getTitle(), tour.getPlace(), addedAmount, tour.getDate());
                t.setId(tour.getId());
                viewModel.updateTour(t);
                Toast.makeText(ExpenseActivity.this, "Added", Toast.LENGTH_SHORT).show();
                alertDialog.dismiss();
            }
        }).setCancelButton("Cancel", new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                Toast.makeText(ExpenseActivity.this, "Cancel", Toast.LENGTH_SHORT).show();
                alertDialog.dismiss();
            }
        }).show();
    }

    private void addExpense() {

        SweetAlertDialog alertDialog = new SweetAlertDialog(ExpenseActivity.this, SweetAlertDialog.NORMAL_TYPE);
        View view = getLayoutInflater().inflate(R.layout.expense_add, null);

        alertDialog.setCustomView(view);

        EditText commentET, amountET;
        commentET = view.findViewById(R.id.commentET);
        amountET = view.findViewById(R.id.amountET);

        alertDialog.setConfirmButton("Save", new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                try {
                    String txt = commentET.getText().toString().trim();
                    String am = amountET.getText().toString().trim();
                    float at = totalAmount + Float.parseFloat(am);
                    if (txt.isEmpty() || am.isEmpty()) {
                        alertDialog.changeAlertType(SweetAlertDialog.ERROR_TYPE);
                        alertDialog.setTitle("Input Error");
                        alertDialog.setContentText("Add amount and comment correctly");
                        alertDialog.setConfirmText("OK");
                        alertDialog.setConfirmClickListener(null);

                        return;
                    } else if (tour.getBudget() < at) {
                        alertDialog.changeAlertType(SweetAlertDialog.ERROR_TYPE);
                        alertDialog.setTitle("Over amount");
                        alertDialog.setContentText("Added over amount");
                        alertDialog.setConfirmText("OK");
                        alertDialog.setConfirmClickListener(null);
                    } else {
                        Expense expense = new Expense(tour.getId(), txt, Float.parseFloat(am));
                        viewModel.insert(expense);
                        Toast.makeText(ExpenseActivity.this, "saved", Toast.LENGTH_SHORT).show();
                        alertDialog.dismiss();

                    }
                }catch (Exception e){
                    e.printStackTrace();
                    Toast.makeText(ExpenseActivity.this, "Error", Toast.LENGTH_SHORT).show();
                    alertDialog.dismiss();
                }
            }
        }).setCancelButton("Cancel", new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                Toast.makeText(ExpenseActivity.this, "Cancel", Toast.LENGTH_SHORT).show();
                alertDialog.dismiss();
            }
        });
        alertDialog.show();
    }

    @Override
    public void editExpense(Expense expense) {
        viewModel.update(expense);
        Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void deleteExpense(Expense expense) {
        viewModel.delete(expense);
        Toast.makeText(this, "Delete", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        NavUtils.navigateUpFromSameTask(this);
    }
}
