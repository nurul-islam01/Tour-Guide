package com.nit.tourguide.ui.tour;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.nit.tourguide.R;
import com.nit.tourguide.dben.Tour;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class TourFragment extends Fragment implements TourAdapter.TourListener {

    private static final String TAG = TourFragment.class.getSimpleName();
    private Context context;
    private TourViewModel mViewModel;
    private List<Tour> tours = new ArrayList<>();
    private TourAdapter adapter;
    private RecyclerView tour_recycler;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.tour_fragment, container, false);
        mViewModel = ViewModelProviders.of(getActivity()).get(TourViewModel.class);

        tour_recycler = root.findViewById(R.id.tour_recycler);
        FloatingActionButton actionButton = root.findViewById(R.id.add_btn);
        adapter = new TourAdapter(context);
        adapter.setTourListener(this);

        mViewModel.getAllTours().observe(getActivity(), new Observer<List<Tour>>() {
            @Override
            public void onChanged(List<Tour> allTours) {
                tours = new ArrayList<>();
                if (allTours != null && allTours.size() > 0) {
                    tours = allTours;
                    adapter.setTours(tours);
                    tour_recycler.setAdapter(adapter);
                    Log.d(TAG, "onChanged: " + tours.get(0).toString());
                } else {
                    Toast.makeText(context, "empty", Toast.LENGTH_SHORT).show();
                }
            }
        });


        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SweetAlertDialog alertDialog = new SweetAlertDialog(context, SweetAlertDialog.NORMAL_TYPE);

                View view = getLayoutInflater().inflate(R.layout.add_edit_tour_dialog, null);
                alertDialog.setCustomView(view);

                EditText tour_name, tour_destination, tour_budget;
                DatePicker tour_date;
                tour_name = view.findViewById(R.id.tour_name);
                tour_destination = view.findViewById(R.id.tour_destination);
                tour_budget = view.findViewById(R.id.tour_budget);
                tour_date = view.findViewById(R.id.tour_date);
                tour_date.setMinDate(System.currentTimeMillis() - 1000);

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

                            SimpleDateFormat simpleDateFormat =new SimpleDateFormat("E, dd MMM yyyy");
                            String date = simpleDateFormat.format(calendar.getTime());
                            mViewModel.insert(new Tour(title, tour_d, budget, date));
                            Toast.makeText(context, "Saved", Toast.LENGTH_SHORT).show();

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
                }).show();
            }
        });


        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void editTour(Tour tour) {
        mViewModel.update(tour);
        Toast.makeText(context, "Edited", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void deleteTour(Tour tour) {
        mViewModel.delete(tour);
        Toast.makeText(context, "Deleted", Toast.LENGTH_SHORT).show();
    }
}
