package com.nit.tourguide.ui.weather;

import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.lifecycle.ViewModelProviders;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.nit.tourguide.R;
import com.nit.tourguide.activity.MainActivity;

import com.nit.tourguide.api.APIClient;
import com.nit.tourguide.api.APIInterface;
import com.nit.tourguide.api.RecentWeatherListener;
import com.nit.tourguide.api.WeeklyListener;
import com.nit.tourguide.listener.AppLocationListener;
import com.nit.tourguide.pojos.weather.current.Current;
import com.nit.tourguide.pojos.weather.hourly.Hourly;
import com.nit.tourguide.pojos.weather.weekly.Weekly;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class WeatherFragment extends Fragment implements AppLocationListener {

    private static final String TAG = WeatherFragment.class.getSimpleName();


    public static String units = "metric";

    private static String currentCity;

    private SweetAlertDialog alertDialog;

    private WeatherViewModel mViewModel;
    private Context context;
    private RecentWeatherListener recentWeatherListener;
    private WeeklyListener weeklyListener;

    public static WeatherFragment newInstance() {
        return new WeatherFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.weather_fragment, container, false);


        ViewPager viewPager = root.findViewById(R.id.viewPager);
        TabLayout tabLayout = root.findViewById(R.id.tabLayout);
        alertDialog = new SweetAlertDialog(getActivity());
        tabLayout.addTab(tabLayout.newTab().setText("Current"));
        tabLayout.addTab(tabLayout.newTab().setText("10 Days"));
        WeatherPagerAdapter adapter = new WeatherPagerAdapter(getChildFragmentManager());
        viewPager.setAdapter(adapter);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        ((MainActivity) getActivity()).setAppLocationListener(this);

        return root;
    }

    private void getCity(Location location) throws IOException {

        alertDialog.changeAlertType(SweetAlertDialog.PROGRESS_TYPE);
        alertDialog.setTitle("Loading....");
        alertDialog.show();

        Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            Address obj = addresses.get(0);

//            String add = obj.getAddressLine(0);
//            add = add + "\n" + obj.getCountryName();
//            add = add + "\n" + obj.getCountryCode();
//            add = add + "\n" + obj.getAdminArea();

//            add = add + "\n" + obj.getPostalCode();
//            add = add + "\n" + obj.getSubAdminArea();
//            add = add + "\n" + obj.getLocality();
//            add = add + "\n" + obj.getSubThoroughfare();

            String city =  obj.getSubAdminArea();
            city = city.replace("Division", "");
            city = city.replace("District", "");
            searchByCity(city);
        } catch (IOException e) {
            e.printStackTrace();
            alertDialog.changeAlertType(SweetAlertDialog.ERROR_TYPE);
            alertDialog.setTitle("City Search Problem");
        }
    }

    private void searchByCity(String city) {

        recentWeather(city);
        weekly(city);
        hourly(city);
    }

    private void recentWeather(String city) {

        APIInterface api = APIClient.getClient("https://community-open-weather-map.p.rapidapi.com/").create(APIInterface.class);
        Call<Current> call = api.current("weather?units=%2522metric&mode=xml%252C%20html&q="+city);
        call.enqueue(new Callback<Current>() {
            @Override
            public void onResponse(Call<Current> call, Response<Current> response) {
                if (response.isSuccessful()) {
                    Current current = response.body();
                    recentWeatherListener.onCurrent(current);
                    Log.d(TAG, "onResponse: "+ response.body());
                } else {
                    Log.d(TAG, "onResponse: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<Current> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        });
    }

    private void weekly(String city) {

        APIInterface api = APIClient.getClient("https://community-open-weather-map.p.rapidapi.com/forecast/").create(APIInterface.class);
        Call<Weekly> call = api.weekly("daily?q="+city+"&units=metric");
        call.enqueue(new Callback<Weekly>() {
            @Override
            public void onResponse(Call<Weekly> call, Response<Weekly> response) {
                if (response.isSuccessful()) {
                    Weekly weekly = response.body();
                    weeklyListener.onWeakly(weekly);
                    Log.d(TAG, "onResponse: "+ response.body());
                } else {
                    Log.d(TAG, "onResponse: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<Weekly> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        });
    }

    private void hourly(String city) {

        APIInterface api = APIClient.getClient("https://community-open-weather-map.p.rapidapi.com/").create(APIInterface.class);
        Call<Hourly> call = api.hourly("forecast?q="+city);
        call.enqueue(new Callback<Hourly>() {
            @Override
            public void onResponse(Call<Hourly> call, Response<Hourly> response) {
                if (response.isSuccessful()) {
                    Hourly hourly = response.body();
                    recentWeatherListener.onHourly(hourly);
                    Log.d(TAG, "onResponse: "+ response.body());
                    alertDialog.dismiss();
                } else {
                    alertDialog.changeAlertType(SweetAlertDialog.ERROR_TYPE);
                    alertDialog.setTitle("ERROR");
                    alertDialog.setConfirmText("OK");
                    alertDialog.setConfirmClickListener(null);
                    Log.d(TAG, "onResponse: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<Hourly> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
                alertDialog.changeAlertType(SweetAlertDialog.ERROR_TYPE);
                alertDialog.setTitle("ERROR");
                alertDialog.setConfirmText("OK");
            }
        });
    }

    public void setRecentWeatherListener(RecentWeatherListener recentWeatherListener){
        this.recentWeatherListener = recentWeatherListener;
    }
    public void setWeaklyWeatherListener(WeeklyListener weaklyWeatherListener){
        this.weeklyListener = weaklyWeatherListener;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(WeatherViewModel.class);
        // TODO: Use the ViewModel
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;

    }


    @Override
    public void onAttachFragment(@NonNull Fragment childFragment) {
        super.onAttachFragment(childFragment);
        try {
            this.recentWeatherListener = (RecentWeatherListener) childFragment;
        }catch (Exception e){
            Log.d(TAG, "onAttachFragment: " + e.getMessage());
        }try {
            this.weeklyListener = (WeeklyListener) childFragment;
        }catch (Exception e){
            Log.d(TAG, "onAttachFragment: " + e.getMessage());
        }
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu. weather_menu, menu);
        MenuItem item = menu.findItem(R.id.action_search);
        SearchView searchView = new SearchView(((MainActivity) context).getSupportActionBar().getThemedContext());
        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW | MenuItem.SHOW_AS_ACTION_IF_ROOM);
        item.setActionView(searchView);
        searchView.setSubmitButtonEnabled(true);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                if (query != null) {
                    searchByCity(query.trim());
                } else {
                    Toast.makeText(context, "Enter place name", Toast.LENGTH_SHORT).show();
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Here is where we are going to implement the filter logic
                return true;
            }

        });
    }



    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.farenheit_menu:
                units = "imperial";
//                searchByeCity(currentCity, units);
                break;
            case R.id.celcius_menu:
                units ="metric";
//                searchByeCity(currentCity, units);
                break;

        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onLocation(Location location) {
        try {
            getCity(location);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private class WeatherPagerAdapter extends FragmentStatePagerAdapter {

        public WeatherPagerAdapter(@NonNull FragmentManager fm) {
            super(fm);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0: {
                    return new RecentRecentWeatherFragment();
                }
                case 1: {
                    return new WeeklyWeatherFragment();
                }
            }
            return null;
        }

        @Override
        public int getCount() {
            return 2;
        }
    }

}
