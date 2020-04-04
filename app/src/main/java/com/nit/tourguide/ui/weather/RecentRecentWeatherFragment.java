package com.nit.tourguide.ui.weather;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.nit.tourguide.R;
import com.nit.tourguide.api.RecentWeatherListener;
import com.nit.tourguide.pojos.weather.current.Current;
import com.nit.tourguide.pojos.weather.hourly.Hourly;
import com.nit.tourguide.pojos.weather.weekly.Weekly;
import com.squareup.picasso.Picasso;


public class RecentRecentWeatherFragment extends Fragment implements RecentWeatherListener {

    private static final String TAG = RecentRecentWeatherFragment.class.getSimpleName();

    private Hourly hourly;
    private Current current;

    private TextView cityTV,dateTime, tempText,cloude,cloudePercent,sunriseTV,sunsetTV;
    private GridView gridView;
    private ImageView iconImage;

    private TextView hourDeg1,hourDeg2,hourDeg3,hourDeg4,hourTime1,hourTime2,hourTime3,hourTime4;
    private ImageView hourIcon1,hourIcon2, hourIcon3, hourIcon4;

    public RecentRecentWeatherFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_recent_weather, container, false);

        cityTV = v.findViewById(R.id.cityTextView);
        dateTime = v.findViewById(R.id.dateTime);
        tempText = v.findViewById(R.id.tempText);
        cloude = v.findViewById(R.id.cloude);
        cloudePercent = v.findViewById(R.id.cloudePercent);
        iconImage = v.findViewById(R.id.iconImage);
        sunriseTV = v.findViewById(R.id.sunrise);
        sunsetTV = v.findViewById(R.id.sunset);

        //hourly
        hourDeg1 = v.findViewById(R.id.hourDeg1);
        hourDeg2 = v.findViewById(R.id.hourDeg2);
        hourDeg3 = v.findViewById(R.id.hourDeg3);
        hourDeg4 = v.findViewById(R.id.hourDeg4);
        hourTime1 = v.findViewById(R.id.hourTime1);
        hourTime2 = v.findViewById(R.id.hourTime2);
        hourTime3 = v.findViewById(R.id.hourTime3);
        hourTime4 = v.findViewById(R.id.hourTime4);

        //hour Icons
        hourIcon1 = v.findViewById(R.id.hourIcon1);
        hourIcon2 = v.findViewById(R.id.hourIcon2);
        hourIcon3 = v.findViewById(R.id.hourIcon3);
        hourIcon4 = v.findViewById(R.id.hourIcon4);

        return v;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    @Override
    public void onAttachFragment(@NonNull Fragment childFragment) {
        super.onAttachFragment(childFragment);
        if (childFragment instanceof WeatherFragment) {
            ((WeatherFragment)getParentFragment()).setRecentWeatherListener(this);
        }
    }

    @Override
    public void onHourly(Hourly hourly) {
        this.hourly = hourly;

        try {
            hourDeg1.setText(hourly.getList().get(0).getMain().getTempInt()  + "\u00B0" + "C" );
            hourDeg2.setText(hourly.getList().get(1).getMain().getTempInt()  + "\u00B0" + "C");
            hourDeg3.setText(hourly.getList().get(2).getMain().getTempInt()  + "\u00B0" + "C");
            hourDeg4.setText(hourly.getList().get(3).getMain().getTempInt()  + "\u00B0" + "C");

            hourTime1.setText(hourly.getList().get(0).getHourString());
            hourTime2.setText(hourly.getList().get(1).getHourString());
            hourTime3.setText(hourly.getList().get(2).getHourString());
            hourTime4.setText(hourly.getList().get(3).getHourString());

            Picasso.get().load("http://openweathermap.org/img/wn/"+hourly.getList().get(0).getWeather().get(0).getIcon()+"@2x.png")
                    .into(hourIcon1);
            Picasso.get().load("http://openweathermap.org/img/wn/"+hourly.getList().get(1).getWeather().get(0).getIcon()+"@2x.png")
                    .into(hourIcon2);
            Picasso.get().load("http://openweathermap.org/img/wn/"+hourly.getList().get(2).getWeather().get(0).getIcon()+"@2x.png")
                    .into(hourIcon3);
            Picasso.get().load("http://openweathermap.org/img/wn/"+hourly.getList().get(3).getWeather().get(0).getIcon()+"@2x.png")
                    .into(hourIcon4);


        }catch (Exception e){
            Log.d(TAG, "onHourly: " + e.getMessage());
        }


        Log.d(TAG, "onHourly: " + hourly.toString());
    }

    @Override
    public void onCurrent(Current current) {
        this.current = current;
        try {
            cityTV.setText(current.getName());
        }catch (Exception e){
            Log.d(TAG, "onCurrent: " + e.getMessage());
        }try {
            dateTime.setText(current.getDateString());
        }catch (Exception e){
            Log.d(TAG, "onCurrent: " + e.getMessage());
        }try {
            sunriseTV.setText(current.getSys().getSunriseString());
        }catch (Exception e){
            Log.d(TAG, "onCurrent: " + e.getMessage());
        }try {
            sunsetTV.setText(current.getSys().getSunSetString());
        }catch (Exception e){
            Log.d(TAG, "onCurrent: " + e.getMessage());
        }try {
            Picasso.get().load("http://openweathermap.org/img/wn/"+current.getWeather().get(0).getIcon()+"@2x.png")
                    .into(iconImage);
        }catch (Exception e){
            Log.d(TAG, "onCurrent: " + e.getMessage());
        }try {
            tempText.setText(current.getMain().getTempInt() + "\u00B0" + "C");
        }catch (Exception e){
            Log.d(TAG, "onCurrent: " + e.getMessage());
        }try {
            cloudePercent.setText(current.getClouds().getAll() + "%");
        }catch (Exception e){
            Log.d(TAG, "onCurrent: " + e.getMessage());
        }try {
            cloude.setText(current.getWeather().get(0).getMain());
        }catch (Exception e){
            Log.d(TAG, "onCurrent: " + e.getMessage());
        }

        Log.d(TAG, "onCurrent: " + current.toString());
    }

}
