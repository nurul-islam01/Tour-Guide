package com.nit.tourguide.api;

import com.nit.tourguide.pojos.weather.weekly.Weekly;

public interface WeeklyListener {
    void onWeakly(Weekly weekly);
}
