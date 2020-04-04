package com.nit.tourguide.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.nit.tourguide.R;
import com.nit.tourguide.activity.adapter.DirectionStepsAdapter;
import com.nit.tourguide.api.APIClient;
import com.nit.tourguide.api.APIInterface;
import com.nit.tourguide.pojos.nearby.Result;
import com.nit.tourguide.pojos.place_details.PlaceDetails;
import com.nit.tourguide.pojos.selected_distance.SelectPlace;
import com.nit.tourguide.pojos.selected_distance.Step;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PlaceDetailsActivity extends AppCompatActivity implements OnMapReadyCallback {


    private int PERMISSION_ID = 44;
    public static final String TAG = PlaceDetailsActivity.class.getSimpleName();
    private GoogleMap googleMap;
    private Location location;
    private MapView mMapView;
    private FusedLocationProviderClient fusedLocationClient;
    private Result result;
    private String DESTINATION_BASE_URL = "https://maps.googleapis.com/maps/api/directions/";
    private String PLACE_DETAILS_BASE_URL = "https://maps.googleapis.com/maps/api/place/details/";

    private RelativeLayout search_bar;
    private NestedScrollView bottomSheet;
    private RecyclerView nearbyRC;
    private TextView formattedNameTV, formattedAddressTV,formattedPhoneTV,ratingAndOpenTV,distanceAndTimeTV;
    private Button weeklyOpenTV;
    private ImageView destinationImage;
    private RecyclerView directionDetailsContainer;
    private DirectionStepsAdapter directionStepsAdapter;
    private List<String> diresctionString;

    private String placeName,placeAddress;
    private double lat, lon;
    private String placeId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_AppCompat_Light_NoActionBar);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN
                , WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_place_details);

        formattedNameTV = findViewById(R.id.formattedName);
        formattedAddressTV = findViewById(R.id.formattedAddress);
        distanceAndTimeTV = findViewById(R.id.distanceAndTime);
        formattedPhoneTV = findViewById(R.id.formattedPhone);
        ratingAndOpenTV = findViewById(R.id.ratingAndOpen);
        weeklyOpenTV = findViewById(R.id.weeklyOpen);
        destinationImage = findViewById(R.id.destinationImage);
        directionDetailsContainer = findViewById(R.id.stepContainer);


        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        try {
            Intent intent = getIntent();
            placeId = intent.getStringExtra("placeId");
            placeName = intent.getStringExtra("placeName");
            placeAddress = intent.getStringExtra("placeAddress");
            lat = intent.getDoubleExtra("lat",0);
            lon = intent.getDoubleExtra("lon",0);

            Log.d(TAG, "onCreate: lat : " + lat + " lon : " + lon);

            placeDetails(placeId);


        }catch (Exception e){
            Log.d(TAG, "onCreate: "+e.getMessage());
        }
        getLastLocation();

        mMapView = (MapView) findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);
        mMapView.onResume(); // needed to get the map to display immediately
        try {
            MapsInitializer.initialize(getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }
        mMapView.getMapAsync(this);


    }


    private void getLastLocation(){
        if (checkPermissions()) {
            if (isLocationEnabled()) {
                fusedLocationClient.getLastLocation().addOnCompleteListener(
                        new OnCompleteListener<Location>() {
                            @Override
                            public void onComplete(@NonNull Task<Location> task) {
                                location = task.getResult();
                                if (location == null) {
                                    requestNewLocationData();
                                } else {
                                    try{
                                        selectPlace(lat,lon);
                                        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()),12.0f));
                                    }catch (Exception e){
                                        Log.d(TAG, "onMapReady: "+e.getMessage());
                                    }                                }
                            }
                        }
                );
            } else {
                Toast.makeText(this, "Turn on location", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        } else {
            requestPermissions();
        }


    }

    public  void  selectPlace(double lat, double lon){
        APIInterface apiInterface = APIClient.getClient(DESTINATION_BASE_URL).create(APIInterface.class);
        String customUrl = "json?origin="+location.getLatitude()+","+location.getLongitude()+"&destination="+lat+","+lon+"&key="+getString(R.string.google_maps_key);
        Log.d(TAG, "selectPlace: " + customUrl);
        Call<SelectPlace> call = apiInterface.selectedPlace(customUrl);
        call.enqueue(new Callback<SelectPlace>() {
            @Override
            public void onResponse(Call<SelectPlace> call, Response<SelectPlace> response) {
                Log.d(TAG, "onResponse: "+ call.request());

                if (response.isSuccessful()){
                    try {
                        SelectPlace selectPlace = response.body();
                        distanceAndTimeTV.setText("Distance : "+selectPlace.getRoutes().get(0).getLegs().get(0).getDistance().getText()+" | Duration : "+selectPlace.getRoutes().get(0).getLegs().get(0).getDuration().getText());
                        List<Step> steps =selectPlace.getRoutes().get(0).getLegs().get(0).getSteps();
                        googleMap.addMarker(new MarkerOptions().position(new LatLng(steps.get(0).getStartLocation().getLat(),steps.get(0).getStartLocation().getLng())).title("ME").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
                        googleMap.addMarker(new MarkerOptions().position(new LatLng(steps.get(steps.size()-1).getEndLocation().getLat(),steps.get(steps.size()-1).getEndLocation().getLng())).title(placeName).snippet(placeAddress).icon(BitmapDescriptorFactory.fromResource(R.drawable.destination)));
                        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(steps.get(0).getStartLocation().getLat(),steps.get(0).getStartLocation().getLng()),15));
                        diresctionString = new ArrayList<>();
                        for (int i = 0; i<steps.size(); i++){
                            double startLat = steps.get(i).getStartLocation().getLat();
                            double startLng = steps.get(i).getStartLocation().getLng();
                            LatLng startLatlng = new LatLng(startLat,startLng);
                            double endLat = steps.get(i).getEndLocation().getLat();
                            double endLng = steps.get(i).getEndLocation().getLng();
                            LatLng endLatlng = new LatLng(endLat,endLng);
                            if (i<steps.size()-1) {
                                LatLng end = new LatLng(steps.get(i).getEndLocation().getLat(), steps.get(i).getEndLocation().getLng());
                                googleMap.addMarker(new MarkerOptions().position(end).title(String.valueOf(Html.fromHtml(steps.get(i+1).getHtmlInstructions()))).snippet("" +
                                        " Distance : "+steps.get(i).getDistance().getText() + " | Duration : "+steps.get(i).getDuration().getText()).icon(BitmapDescriptorFactory.fromResource(R.drawable.stepmarker)));
                            }
                            Polyline polyline = googleMap.addPolyline(new PolylineOptions().add(startLatlng).add(endLatlng).color(Color.RED));
                            String direct = String.valueOf(Html.fromHtml(steps.get(i).getHtmlInstructions()))+"\n Distance : "+steps.get(i).getDistance().getText() + " | Duration : "+steps.get(i).getDuration().getText();
                            diresctionString.add(direct);
                            Log.d(TAG, "onResponse: " + selectPlace.toString());
                        }
                        directionStepsAdapter = new DirectionStepsAdapter(diresctionString);
                        directionDetailsContainer.setAdapter(directionStepsAdapter);
                    }catch (Exception e){
                        Log.d(TAG, "onResponse: "+e.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<SelectPlace> call, Throwable t) {
                Log.d(TAG, "onFailure: "+t.getMessage());
            }
        });

    }

    private void placeDetails(String placeId){
        APIInterface apiInterface = APIClient.getClient(PLACE_DETAILS_BASE_URL).create(APIInterface.class);
        String url = "json?placeid="+placeId+"&key="+getString(R.string.google_maps_key);
        Call<PlaceDetails> call = apiInterface.getPlaceDetails(url);
        call.enqueue(new Callback<PlaceDetails>() {
            @Override
            public void onResponse(Call<PlaceDetails> call, Response<PlaceDetails> response) {
                Log.d(TAG, "onResponse: "+ call.request());
                if (response.isSuccessful()){
                    try {
                        final PlaceDetails placeDetails = response.body();
                        Log.d(TAG, "onResponse: " + placeDetails.toString());
                        placeName = placeDetails.getResult().getName();
                        formattedNameTV.setText(placeName);
                        placeAddress = placeDetails.getResult().getFormattedAddress();
                        formattedAddressTV.setText(placeAddress);
                        formattedPhoneTV.setText(placeDetails.getResult().getInternationalPhoneNumber());
                        Picasso.get().load("https://maps.googleapis.com/maps/api/place/photo?maxwidth=140&photoreference=" + placeDetails.getResult().getPhotos().get(0).getPhotoReference()+ "&key="+getString(R.string.google_maps_key)).into(destinationImage);
                        String openOrNOt;
                        if (placeDetails.getResult().getOpeningHours().getOpenNow()){
                            openOrNOt = "Open";
                        }else {
                            openOrNOt = "Close";
                        }
                        ratingAndOpenTV.setText("Rating : "+placeDetails.getResult().getRating().toString()+"% | Now "+openOrNOt);
                        weeklyOpenTV.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                try {
                                    AlertDialog.Builder builder1 = new AlertDialog.Builder(PlaceDetailsActivity.this);
                                    builder1.setMessage(placeDetails.getResult().getOpeningHours().getWeekdayText().get(0)+"\n"
                                            +placeDetails.getResult().getOpeningHours().getWeekdayText().get(1)+"\n"+placeDetails.getResult().getOpeningHours().getWeekdayText().get(2)+"\n"+placeDetails.getResult().getOpeningHours().getWeekdayText().get(3)+"\n"
                                            +placeDetails.getResult().getOpeningHours().getWeekdayText().get(4)+"\n"+placeDetails.getResult().getOpeningHours().getWeekdayText().get(5)+"\n"
                                            +"\n"+placeDetails.getResult().getOpeningHours().getWeekdayText().get(6));
                                    builder1.setCancelable(true);
                                    builder1.setNegativeButton("Ok",null);
                                    AlertDialog alrt = builder1.create();
                                    alrt.show();

                                }catch (Exception e){
                                    Log.d(TAG, "onClick: "+e.getMessage());
                                }


                            }
                        });
                    }catch (Exception e){
                        Log.d(TAG, "onResponse: "+e.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<PlaceDetails> call, Throwable t) {
                Log.d(TAG, "onFailure: "+t.getMessage());
            }
        });
    }

    private void requestNewLocationData(){

        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(0);
        mLocationRequest.setFastestInterval(0);
        mLocationRequest.setNumUpdates(1);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        fusedLocationClient.requestLocationUpdates(
                mLocationRequest, mLocationCallback,
                Looper.myLooper()
        );

    }

    private LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            location = locationResult.getLastLocation();
        }
    };

    private boolean checkPermissions() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        return false;
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(
                this,
                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},
                PERMISSION_ID
        );
    }

    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
                LocationManager.NETWORK_PROVIDER
        );
    }


    @Override
    public void onMapReady(final GoogleMap googleMap) {
        this.googleMap = googleMap;
        this.googleMap.setMyLocationEnabled(true);
        try{
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()),15));
        }catch (Exception e){
            Log.d(TAG, "onMapReady: "+e.getMessage());
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
