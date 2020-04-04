package com.nit.tourguide.pojos.weather.current;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import com.fasterxml.jackson.annotation.*;

public class Sys {
    private long type;
    private long id;
    private String country;
    private long sunrise;
    private long sunset;

    public String getSunriseString() {
        DateFormat df = new SimpleDateFormat("hh:mm aa");
        TimeZone timeZone = TimeZone.getDefault();
        df.setTimeZone(TimeZone.getTimeZone(timeZone.getID()));
        Date d = new Date(getSunrise() * 1000L);
        return df.format(d);
    }
    public String getSunSetString() {
        DateFormat df = new SimpleDateFormat("hh:mm aa");
        TimeZone timeZone = TimeZone.getDefault();
        df.setTimeZone(TimeZone.getTimeZone(timeZone.getID()));
        Date d = new Date(getSunset() * 1000L);
        return df.format(d);
    }


    @JsonProperty("type")
    public long getType() { return type; }
    @JsonProperty("type")
    public void setType(long value) { this.type = value; }

    @JsonProperty("id")
    public long getID() { return id; }
    @JsonProperty("id")
    public void setID(long value) { this.id = value; }

    @JsonProperty("country")
    public String getCountry() { return country; }
    @JsonProperty("country")
    public void setCountry(String value) { this.country = value; }

    @JsonProperty("sunrise")
    public long getSunrise() {
        return sunrise; }
    @JsonProperty("sunrise")
    public void setSunrise(long value) { this.sunrise = value; }

    @JsonProperty("sunset")
    public long getSunset() { return sunset; }
    @JsonProperty("sunset")
    public void setSunset(long value) { this.sunset = value; }
}
