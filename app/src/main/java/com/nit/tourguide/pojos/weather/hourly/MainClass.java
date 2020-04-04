package com.nit.tourguide.pojos.weather.hourly;


import java.util.*;
import com.fasterxml.jackson.annotation.*;

public class MainClass {
    private double temp;
    private double feelsLike;
    private double tempMin;
    private double tempMax;
    private long pressure;
    private long seaLevel;
    private long grndLevel;
    private long humidity;
    private double tempKf;

    public int getTempInt() {
        return (int) Math.round(getTemp() - 273.15);
    }

    @JsonProperty("temp")
    public double getTemp() { return temp; }
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

    @JsonProperty("sea_level")
    public long getSeaLevel() { return seaLevel; }
    @JsonProperty("sea_level")
    public void setSeaLevel(long value) { this.seaLevel = value; }

    @JsonProperty("grnd_level")
    public long getGrndLevel() { return grndLevel; }
    @JsonProperty("grnd_level")
    public void setGrndLevel(long value) { this.grndLevel = value; }

    @JsonProperty("humidity")
    public long getHumidity() { return humidity; }
    @JsonProperty("humidity")
    public void setHumidity(long value) { this.humidity = value; }

    @JsonProperty("temp_kf")
    public double getTempKf() { return tempKf; }
    @JsonProperty("temp_kf")
    public void setTempKf(double value) { this.tempKf = value; }
}
