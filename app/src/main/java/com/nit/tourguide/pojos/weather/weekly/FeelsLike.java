package com.nit.tourguide.pojos.weather.weekly;


import java.util.*;
import com.fasterxml.jackson.annotation.*;

public class FeelsLike {
    private double day;
    private double night;
    private double eve;
    private double morn;

    @JsonProperty("day")
    public double getDay() { return day; }
    @JsonProperty("day")
    public void setDay(double value) { this.day = value; }

    @JsonProperty("night")
    public double getNight() { return night; }
    @JsonProperty("night")
    public void setNight(double value) { this.night = value; }

    @JsonProperty("eve")
    public double getEve() { return eve; }
    @JsonProperty("eve")
    public void setEve(double value) { this.eve = value; }

    @JsonProperty("morn")
    public double getMorn() { return morn; }
    @JsonProperty("morn")
    public void setMorn(double value) { this.morn = value; }

    @Override
    public String toString() {
        return "FeelsLike{" +
                "day=" + day +
                ", night=" + night +
                ", eve=" + eve +
                ", morn=" + morn +
                '}';
    }
}
