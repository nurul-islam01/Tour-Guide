package com.nit.tourguide.pojos.weather.current;


import java.util.*;
import com.fasterxml.jackson.annotation.*;

public class Main {
    private double temp;
    private double feelsLike;
    private double tempMin;
    private double tempMax;
    private long pressure;
    private long humidity;

    @JsonProperty("temp")
    public double getTemp() { return temp; }

    public int getTempInt() {
        double d = (getTemp() - 273.15);
        return (int) Math.round(d);
    }
    @JsonProperty("temp")
    public void setTemp(double value) { this.temp = value; }

    @JsonProperty("feels_like")
    public double getFeelsLike() { return feelsLike; }
    @JsonProperty("feels_like")
    public void setFeelsLike(double value) { this.feelsLike = value; }

    @JsonProperty("temp_min")
    public double getTempMin() { return tempMin; }
    @JsonProperty("temp_min")
    public void setTempMin(double value) { this.tempMin = value; }

    @JsonProperty("temp_max")
    public double getTempMax() { return tempMax; }
    @JsonProperty("temp_max")
    public void setTempMax(double value) { this.tempMax = value; }

    @JsonProperty("pressure")
    public long getPressure() { return pressure; }
    @JsonProperty("pressure")
    public void setPressure(long value) { this.pressure = value; }

    @JsonProperty("humidity")
    public long getHumidity() { return humidity; }
    @JsonProperty("humidity")
    public void setHumidity(long value) { this.humidity = value; }
}

