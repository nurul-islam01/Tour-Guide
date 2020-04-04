package com.nit.tourguide.api;


import com.nit.tourguide.pojos.weather.current.Current;
import com.nit.tourguide.pojos.weather.hourly.Hourly;

public interface RecentWeatherListener {

    public void onHourly(Hourly hourly);

    public void onCurrent(Current current);

}
