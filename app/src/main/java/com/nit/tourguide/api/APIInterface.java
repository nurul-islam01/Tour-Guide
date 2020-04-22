package com.nit.tourguide.api;

import com.nit.tourguide.pojos.nearby.NearBy;
import com.nit.tourguide.pojos.place_details.PlaceDetails;
import com.nit.tourguide.pojos.selected_distance.SelectPlace;
import com.nit.tourguide.pojos.weather.current.Current;
import com.nit.tourguide.pojos.weather.hourly.Hourly;
import com.nit.tourguide.pojos.weather.weekly.Weekly;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Url;

public interface APIInterface {
    @GET()
    Call<NearBy> getNearby(@Url String url);

    @GET()
    Call<PlaceDetails> getPlaceDetails(@Url String url);

    @GET()
    Call<SelectPlace> selectedPlace(@Url String url);


    @Headers({
            "x-rapidapi-host: community-open-weather-map.p.rapidapi.com",
            "x-rapidapi-key: 59eb49e768msh48dfdf5c23d6ec4p1e7e5ejsnf7976c94cc4f"
    })
    @GET()
    Call<Current> current(@Url String url);

    @Headers({
            "x-rapidapi-host: community-open-weather-map.p.rapidapi.com",
            "x-rapidapi-key: 59eb49e768msh48dfdf5c23d6ec4p1e7e5ejsnf7976c94cc4f"
    })
    @GET()
    Call<Weekly> weekly(@Url String url);

    @Headers({
            "x-rapidapi-host: community-open-weather-map.p.rapidapi.com",
            "x-rapidapi-key: 59eb49e768msh48dfdf5c23d6ec4p1e7e5ejsnf7976c94cc4f"
    })
    @GET()
    Call<Hourly> hourly(@Url String url);

}
