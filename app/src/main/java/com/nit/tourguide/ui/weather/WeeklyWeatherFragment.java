package com.nit.tourguide.ui.weather;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nit.tourguide.R;
import com.nit.tourguide.api.WeeklyListener;
import com.nit.tourguide.pojos.weather.weekly.Weekly;
import com.nit.tourguide.ui.weather.adapter.ForecastAdapter;


public class WeeklyWeatherFragment extends Fragment implements WeeklyListener {

    private static final String TAG = WeeklyWeatherFragment.class.getSimpleName();
    private Context context;

    private Weekly weekly;
    RecyclerView weeklyRecycler;

    public WeeklyWeatherFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_weekly_weather, container, false);
        weeklyRecycler = root.findViewById(R.id.weeklyRecycler);

        return root;
    }

    @Override
    public void onAttachFragment(@NonNull Fragment childFragment) {
        super.onAttachFragment(childFragment);
        if (childFragment instanceof WeatherFragment) {
            ((WeatherFragment)getParentFragment()).setWeaklyWeatherListener(this);
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onWeakly(Weekly weekly) {
        this.weekly = weekly;
        ForecastAdapter adapter = new ForecastAdapter(weekly.getList(), context);
        weeklyRecycler.setAdapter(adapter);
        Log.d(TAG, "onWeekly: " + weekly.toString());
    }
}
