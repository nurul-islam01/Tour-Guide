package com.nit.tourguide.ui.map;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;

import android.content.Intent;
import android.content.pm.PackageManager;

import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.maps.android.clustering.ClusterManager;
import com.nit.tourguide.activity.MainActivity;
import com.nit.tourguide.api.APIClient;
import com.nit.tourguide.api.APIInterface;
import com.nit.tourguide.listener.AppLocationListener;
import com.nit.tourguide.pojos.ClusterItem;
import com.nit.tourguide.pojos.nearby.NearBy;
import com.nit.tourguide.pojos.nearby.Result;
import com.nit.tourguide.R;

import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MapFragment extends Fragment implements OnMapReadyCallback, AppLocationListener {

    private String NEARBY_BASE_URL = "https://maps.googleapis.com/maps/api/place/nearbysearch/";

    private Context context;
    public static final String TAG = "MapFragment";
    private GoogleMap googleMap;
    private Location location;
    private MapView mMapView;
    private String selectedType;
    private int selectedDistance;
    private APIInterface apiInterface;

    private SweetAlertDialog alertDialog;
    private RelativeLayout search_bar;
    private NestedScrollView bottomSheet;
    private RecyclerView nearbyRC;

    public MapFragment() {}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getLastLocation();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.map_fragment, container, false);
        ((MainActivity) getActivity()).setAppLocationListener(this);

        mMapView = (MapView) root.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);
        mMapView.onResume(); // needed to get the map to display immediately
        apiInterface = APIClient.getClient(NEARBY_BASE_URL).create(APIInterface.class);
        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }
        mMapView.getMapAsync(this);

        alertDialog = new SweetAlertDialog(context);
        search_bar = root.findViewById(R.id.search_bar);

        bottomSheet = root.findViewById(R.id.bottomSheet);
        nearbyRC = root.findViewById(R.id.nearbyRC);

        String[] types = getResources().getStringArray(R.array.search_type);
        ArrayAdapter<String> placeTypeAdapter = new ArrayAdapter<String>(getActivity(),R.layout.simple_spinner_item, types);
        Spinner areasSpinner = (Spinner) root.findViewById(R.id.placeType);
        areasSpinner.setAdapter(placeTypeAdapter);

        areasSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedType = areasSpinner.getSelectedItem().toString().replaceAll(" ", "_").toLowerCase();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        String[] distance = getResources().getStringArray(R.array.distance);
        ArrayAdapter<String> placeDistance = new ArrayAdapter<String>(getActivity(),R.layout.simple_spinner_item, distance);
        Spinner distanceSpinner = (Spinner) root.findViewById(R.id.placeDistance);
        distanceSpinner.setAdapter(placeDistance);
        distanceSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedDistance = (position + 1) * 1000;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        search_bar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nearbySearch(selectedType, selectedDistance);
            }
        });


        return root;
    }
//
//    private void getLastLocation(){
//        if (checkPermissions()) {
//            if (isLocationEnabled()) {
//                fusedLocationClient.getLastLocation().addOnCompleteListener(
//                        new OnCompleteListener<Location>() {
//                            @Override
//                            public void onComplete(@NonNull Task<Location> task) {
//                               Location loc = task.getResult();
//                                if (location == null) {
//                                    requestNewLocationData();
//                                } else {
//                                    try{
//                                        location = loc;
//                                        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()),15));
//                                    }catch (Exception e){
//                                        Log.d(TAG, "onMapReady: "+e.getMessage());
//                                    }                                }
//                            }
//                        }
//                );
//            } else {
//                Toast.makeText(context, "Turn on location", Toast.LENGTH_LONG).show();
//                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
//                startActivity(intent);
//            }
//        } else {
//            requestPermissions();
//        }
//
//
//    }
//
//    private void requestNewLocationData(){
//
//        LocationRequest mLocationRequest = new LocationRequest();
//        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
//        mLocationRequest.setInterval(0);
//        mLocationRequest.setFastestInterval(0);
//        mLocationRequest.setNumUpdates(1);
//
//        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context);
//        fusedLocationClient.requestLocationUpdates(
//                mLocationRequest, mLocationCallback,
//                Looper.myLooper()
//        );
//
//    }
//
//    private LocationCallback mLocationCallback = new LocationCallback() {
//        @Override
//        public void onLocationResult(LocationResult locationResult) {
//             location = locationResult.getLastLocation();
//        }
//    };
//
//    private boolean checkPermissions() {
//        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
//                ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
//            return true;
//        }
//        return false;
//    }
//
//    private void requestPermissions() {
//        ActivityCompat.requestPermissions(
//                getActivity(),
//                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},
//                PERMISSION_ID
//        );
//    }
//
//    private boolean isLocationEnabled() {
//        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
//        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
//                LocationManager.NETWORK_PROVIDER
//        );
//    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }


    @Override
    public void onMapReady(final GoogleMap googleMap) {
        this.googleMap = googleMap;
        this.googleMap.setMyLocationEnabled(true);
        try {
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()),15));
        }catch (Exception e){
            Log.d(TAG, "onMapReady: " + e.getMessage());
        }
    }

    private void nearbySearch(String typeName, int distanceArea) {
        alertDialog.changeAlertType(SweetAlertDialog.PROGRESS_TYPE);
        alertDialog.setTitle("Loading...");
        alertDialog.show();
        String nearbyApiKey = getString(R.string.google_maps_key);
        String customUrl = "json?location="+location.getLatitude()+","+location.getLongitude()+"&radius="+distanceArea+"&type="+typeName+"&key="+nearbyApiKey;

        Call<NearBy> call = apiInterface.getNearby(customUrl);
        call.enqueue(new Callback<NearBy>() {
            @Override
            public void onResponse(Call<NearBy> call, Response<NearBy> response) {
                if (response.isSuccessful()) {
                    NearBy nearBy = response.body();
                    if (nearBy.getResults().size() > 0) {
                        putAllMarker(nearBy.getResults());
                        bottomSheet.setVisibility(View.VISIBLE);
                        NearbyPlacesesAdapter adapter = new NearbyPlacesesAdapter(nearBy.getResults());
                        nearbyRC.setAdapter(adapter);
                    } else {
                        alertDialog.changeAlertType(SweetAlertDialog.ERROR_TYPE);
                        alertDialog.setTitle("Not Found");
                        alertDialog.setConfirmText("OK");
                        alertDialog.setConfirmClickListener(null);
                        Toast.makeText(context, "Not Found", Toast.LENGTH_SHORT).show();
                        bottomSheet.setVisibility(View.GONE);
                    }
                } else {
                    Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
                    alertDialog.changeAlertType(SweetAlertDialog.WARNING_TYPE);
                    alertDialog.setTitle("Error");
                    alertDialog.setConfirmText("OK");
                    alertDialog.setConfirmClickListener(null);
                }
            }

            @Override
            public void onFailure(Call<NearBy> call, Throwable t) {
                Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
                alertDialog.changeAlertType(SweetAlertDialog.WARNING_TYPE);
                alertDialog.setTitle("Error");
                alertDialog.setConfirmText("OK");
                alertDialog.setConfirmClickListener(null);
            }
        });

    }


    private void putAllMarker(List<Result> results){
        List<ClusterItem> clusterItems = new ArrayList<ClusterItem>();
            googleMap.clear();
        for (int i=0; i < results.size(); i++){
            try {
                final Result result = results.get(i);
                LatLng latLng = new LatLng(result.getGeometry().getLocation().getLat(),result.getGeometry().getLocation().getLng());
                ClusterItem allItemCluster = new ClusterItem(latLng,result.getName(),result.getPlaceId(),result.getVicinity());
                clusterItems.add(allItemCluster);
                String snipt = "";
                if (selectedType.equals("restaurant") || selectedType.equals("cafe")|| selectedType.equals("hospital")|| selectedType.equals("supermarket")){
                    snipt = "Rating : "+String.valueOf(result.getRating())+"%";
                }else {
                    snipt = result.getVicinity();
                }
                googleMap.addMarker(new MarkerOptions().position(latLng).title(result.getName().toString()).snippet(snipt));

                ClusterManager<com.google.maps.android.clustering.ClusterItem> clusterManager= new ClusterManager<com.google.maps.android.clustering.ClusterItem>(context, googleMap);
                googleMap.setOnCameraIdleListener(clusterManager);


                alertDialog.dismiss();

            }catch (Exception e){
                Log.d(TAG, "putAllMarker: "+e.getMessage());
                alertDialog.dismiss();
            }
        }

        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 12.0f));
    }
    
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

    }

    @Override
    public void onLocation(Location location) {
        this.location = location;
//        Toast.makeText(context, ""+location.getLatitude(), Toast.LENGTH_SHORT).show();
    }
}
