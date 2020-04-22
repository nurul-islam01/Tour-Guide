package com.nit.tourguide.activity.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.nit.tourguide.R;
import com.nit.tourguide.activity.ExpenseActivity;
import com.nit.tourguide.dben.Expense;

import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class ExpenseAdapter extends RecyclerView.Adapter<ExpenseAdapter.ViewHolder> {

    private Context context;
    private List<Expense> expenses;
    private ExpenseListener listener;

    public ExpenseAdapter(Context context) {
        this.context = context;
    }

    public void setExpenses(List<Expense> expenses) {
        this.expenses = expenses;
        notifyDataSetChanged();
    }

    public void setExpenseListener(ExpenseListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.expense_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Expense expense = expenses.get(position);
        try {
            holder.comment.setText(expense.getComment());
            holder.money.setText(String.valueOf(expense.getAmount()));
        }catch (Exception e){
            e.printStackTrace();
        }

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                PopupMenu menu = new PopupMenu(context, v);
                menu.inflate(R.menu.tour_menu);
                menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.edit:
                                editExpense(expense);
                                break;
                            case R.id.delete:
                                listener.deleteExpense(expense);
                                break;
                        }
                        return true;
                    }
                });

                menu.show();
                return true;
            }
        });
    }

    private void editExpense(Expense expense) {

        SweetAlertDialog alertDialog = new SweetAlertDialog(context, SweetAlertDialog.NORMAL_TYPE);
        View view = LayoutInflater.from(context).inflate(R.layout.expense_add, null);

        EditText commentET, amountET;
        commentET = view.findViewById(R.id.commentET);
        amountET = view.findViewById(R.id.amountET);

        try {
            commentET.setText(expense.getComment());
            amountET.setText(String.valueOf(expense.getAmount()));
        }catch (Exception e){
            e.printStackTrace();
        }

        alertDialog.setCustomView(view);

        alertDialog.setConfirmButton("Save", new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                try {
                    String txt = commentET.getText().toString().trim();
                    String am = amountET.getText().toString().trim();
                    if (txt.isEmpty() || am.isEmpty()) {
                        alertDialog.changeAlertType(SweetAlertDialog.ERROR_TYPE);
                        alertDialog.setTitle("Input Error");
                        alertDialog.setContentText("Add amount and comment correctly");
                        alertDialog.setConfirmText("OK");
                        alertDialog.setConfirmClickListener(null);
                        return;
                    } else {
                        Expense exp = new Expense(expense.getTourId(), txt, Float.parseFloat(am));
                        exp.setId(expense.getId());
                        listener.editExpense(exp);
                        alertDialog.dismiss();
                    }

                }catch (Exception e){
                    e.printStackTrace();
                    alertDialog.dismiss();
                }

            }
        }).setCancelButton("Cancel", new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                Toast.makeText(context, "Cancel", Toast.LENGTH_SHORT).show();
                alertDialog.dismiss();
            }
        }).show();
    }

    @Override
    public int getItemCount() {
        return expenses.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView comment, money;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            comment = itemView.findViewById(R.id.comment);
            money = itemView.findViewById(R.id.money);
        }
    }

    public interface ExpenseListener {
        void editExpense(Expense expense);
        void deleteExpense(Expense expense);
    }
}
