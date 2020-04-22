package com.nit.tourguide.ui.tour;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nit.tourguide.R;
import com.nit.tourguide.activity.ExpenseActivity;
import com.nit.tourguide.dben.Tour;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static com.nit.tourguide.ui.map.MapFragment.TAG;

public class TourAdapter extends RecyclerView.Adapter<TourAdapter.ViewHolder> {
    private Context context;
    private List<Tour> tours;
    private TourListener listener;

    public TourAdapter(Context context) {
        this.context = context;
    }

    public void setTours(List<Tour> tours) {
        this.tours = tours;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.tour_item, parent, false));
    }

    public void setTourListener(TourListener listener) {
        this.listener = listener;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        try {
            Tour tour = tours.get(position);
            holder.tour_title.setText(tour.getTitle());
            holder.tour_date.setText(tour.getDate());
            holder.tour_destination.setText(tour.getPlace());
        }catch (Exception e){
            e.printStackTrace();
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Tour tour = tours.get(position);
                Intent data = new Intent(context, ExpenseActivity.class);
                data.putExtra("tour", tour);
                context.startActivity(data);
            }
        });

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
                                editTour(tours.get(position));
                                break;
                            case R.id.delete:
                                listener.deleteTour(tours.get(position));
                                break;
                        }
                        return false;
                    }
                });

                menu.show();
                return false;
            }
        });
    }

    private void editTour(Tour tour) {
        SweetAlertDialog alertDialog = new SweetAlertDialog(context, SweetAlertDialog.NORMAL_TYPE);
        View view = LayoutInflater.from(context).inflate(R.layout.add_edit_tour_dialog, null);
        EditText tour_name, tour_destination, tour_budget;
        DatePicker tour_date;
        tour_name = view.findViewById(R.id.tour_name);
        tour_destination = view.findViewById(R.id.tour_destination);
        tour_budget = view.findViewById(R.id.tour_budget);
        tour_date = view.findViewById(R.id.tour_date);
        tour_date.setMinDate(System.currentTimeMillis() - 1000);

        tour_name.setText(tour.getTitle());
        tour_budget.setText(String.valueOf(tour.getBudget()));
        tour_destination.setText(tour.getPlace());

        alertDialog.setCustomView(view);

       alertDialog.setConfirmButton("Save", new SweetAlertDialog.OnSweetClickListener() {
           @Override
           public void onClick(SweetAlertDialog sweetAlertDialog) {
               try {
                   String title = tour_name.getText().toString().trim();
                   String tour_d = tour_destination.getText().toString().trim();
                   int budget = Integer.valueOf(tour_budget.getText().toString().trim());
                   int day = tour_date.getDayOfMonth();
                   int month = tour_date.getMonth();
                   int year =  tour_date.getYear();

                   Calendar calendar = Calendar.getInstance();
                   calendar.set(year, month, day);

                   SimpleDateFormat simpleDateFormat = new SimpleDateFormat("E, dd MMM yyyy");
                   String date = simpleDateFormat.format(calendar.getTime());
                   Tour t = new Tour(title, tour_d, budget, date);
                   t.setId(tour.getId());
                   listener.editTour(t);

               }catch (Exception e){
                   Log.d(TAG, "onClick: "+ e.getMessage());
                   Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
               }
               alertDialog.dismiss();
           }
       }).setCancelButton("Cancel", new SweetAlertDialog.OnSweetClickListener() {
           @Override
           public void onClick(SweetAlertDialog sweetAlertDialog) {
               Toast.makeText(context, "Cancel", Toast.LENGTH_SHORT).show();
               alertDialog.dismiss();
           }
       });

       alertDialog.show();
    }

    @Override
    public int getItemCount() {
        return tours.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tour_title, tour_date, tour_destination;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tour_title = itemView.findViewById(R.id.tour_title);
            tour_date = itemView.findViewById(R.id.tour_date);
            tour_destination = itemView.findViewById(R.id.tour_destination);
        }
    }

    public interface TourListener {
        void editTour(Tour tour);
        void deleteTour(Tour tour);
    }
}
