package com.nit.tourguide.pojos.weather.weekly;


import com.fasterxml.jackson.annotation.*;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class List {
    private long dt;
    private long sunrise;
    private long sunset;
    private Temp temp;
    private FeelsLike feelsLike;
    private long pressure;
    private long humidity;
    private java.util.List<Weather> weather;
    private double speed;
    private long deg;
    private long clouds;
    private Double rain;

    private Date getDate() {
//        Timestamp ts = new Timestamp(getDt());
//        Timestamp ts =
        Date date = new Date(getDt() * 1000L);
        return date;
    }

    public String getDayString() {
        DateFormat df = new SimpleDateFormat("dd MMMM yyyy");
        TimeZone timeZone = TimeZone.getDefault();
        df.setTimeZone(TimeZone.getTimeZone(timeZone.getID()));
        return df.format(getDate());
    }

    public String getDay() {
        DateFormat df = new SimpleDateFormat("EEE", Locale.ENGLISH);
        TimeZone timeZone = TimeZone.getDefault();
        df.setTimeZone(TimeZone.getTimeZone(timeZone.getID()));
        return df.format(getDate());
    }

    @JsonProperty("dt")
    public long getDt() { return dt; }
    @JsonProperty("dt")
    public void setDt(long value) { this.dt = value; }

    @JsonProperty("sunrise")
    public long getSunrise() { return sunrise; }
    @JsonProperty("sunrise")
    public void setSunrise(long value) { this.sunrise = value; }

    @JsonProperty("sunset")
    public long getSunset() { return sunset; }
    @JsonProperty("sunset")
    public void setSunset(long value) { this.sunset = value; }

    @JsonProperty("temp")
    public Temp getTemp() { return temp; }
    @JsonProperty("temp")
    public void setTemp(Temp value) { this.temp = value; }

    @JsonProperty("feels_like")
    public FeelsLike getFeelsLike() { return feelsLike; }
    @JsonProperty("feels_like")
    public void setFeelsLike(FeelsLike value) { this.feelsLike = value; }

    @JsonProperty("pressure")
    public long getPressure() { return pressure; }
    @JsonProperty("pressure")
    public void setPressure(long value) { this.pressure = value; }

    @JsonProperty("humidity")
    public long getHumidity() { return humidity; }
    @JsonProperty("humidity")
    public void setHumidity(long value) { this.humidity = value; }

    @JsonProperty("weather")
    public java.util.List<Weather> getWeather() { return weather; }
    @JsonProperty("weather")
    public void setWeather(java.util.List<Weather> value) { this.weather = value; }

    @JsonProperty("speed")
    public double getSpeed() { return speed; }
    @JsonProperty("speed")
    public void setSpeed(double value) { this.speed = value; }

    @JsonProperty("deg")
    public long getDeg() { return deg; }
    @JsonProperty("deg")
    public void setDeg(long value) { this.deg = value; }

    @JsonProperty("clouds")
    public long getClouds() { return clouds; }
    @JsonProperty("clouds")
    public void setClouds(long value) { this.clouds = value; }

    @JsonProperty("rain")
    public Double getRain() { return rain; }
    @JsonProperty("rain")
    public void setRain(Double value) { this.rain = value; }

    @Override
    public String toString() {
        return "List{" +
                "dt=" + dt +
                ", sunrise=" + sunrise +
                ", sunset=" + sunset +
                ", temp=" + temp +
                ", feelsLike=" + feelsLike +
                ", pressure=" + pressure +
                ", humidity=" + humidity +
                ", weather=" + weather +
                ", speed=" + speed +
                ", deg=" + deg +
                ", clouds=" + clouds +
                ", rain=" + rain +
                '}';
    }
}
