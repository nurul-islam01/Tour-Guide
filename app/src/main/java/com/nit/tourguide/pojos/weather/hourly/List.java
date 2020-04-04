package com.nit.tourguide.pojos.weather.hourly;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import com.fasterxml.jackson.annotation.*;

public class List {
    private long dt;
    private MainClass main;
    private java.util.List<Weather> weather;
    private Clouds clouds;
    private Wind wind;
    private Sys sys;
    private String dtTxt;
    private Rain rain;

    private Date getDate() {
//        Timestamp ts = new Timestamp(getDt());
        Date date = new Date(getDt() * 1000L);
        return date;
    }

    public String getHourString() {
        DateFormat df = new SimpleDateFormat("hh aa");
        TimeZone timeZone = TimeZone.getDefault();
        df.setTimeZone(TimeZone.getTimeZone(timeZone.getID()));
        return df.format(getDate());
    }


    @JsonProperty("dt")
    public long getDt() { return dt; }
    @JsonProperty("dt")
    public void setDt(long value) { this.dt = value; }

    @JsonProperty("main")
    public MainClass getMain() { return main; }
    @JsonProperty("main")
    public void setMain(MainClass value) { this.main = value; }

    @JsonProperty("weather")
    public java.util.List<Weather> getWeather() { return weather; }
    @JsonProperty("weather")
    public void setWeather(java.util.List<Weather> value) { this.weather = value; }

    @JsonProperty("clouds")
    public Clouds getClouds() { return clouds; }
    @JsonProperty("clouds")
    public void setClouds(Clouds value) { this.clouds = value; }

    @JsonProperty("wind")
    public Wind getWind() { return wind; }
    @JsonProperty("wind")
    public void setWind(Wind value) { this.wind = value; }

    @JsonProperty("sys")
    public Sys getSys() { return sys; }
    @JsonProperty("sys")
    public void setSys(Sys value) { this.sys = value; }

    @JsonProperty("dt_txt")
    public String getDtTxt() { return dtTxt; }
    @JsonProperty("dt_txt")
    public void setDtTxt(String value) { this.dtTxt = value; }

    @JsonProperty("rain")
    public Rain getRain() { return rain; }
    @JsonProperty("rain")
    public void setRain(Rain value) { this.rain = value; }
}