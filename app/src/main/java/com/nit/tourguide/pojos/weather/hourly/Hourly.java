package com.nit.tourguide.pojos.weather.hourly;

import java.util.*;
import com.fasterxml.jackson.annotation.*;

public class Hourly {
    private String cod;
    private long message;
    private long cnt;
    private java.util.List<List> list;
    private City city;

    @JsonProperty("cod")
    public String getCod() { return cod; }
    @JsonProperty("cod")
    public void setCod(String value) { this.cod = value; }

    @JsonProperty("message")
    public long getMessage() { return message; }
    @JsonProperty("message")
    public void setMessage(long value) { this.message = value; }

    @JsonProperty("cnt")
    public long getCnt() { return cnt; }
    @JsonProperty("cnt")
    public void setCnt(long value) { this.cnt = value; }

    @JsonProperty("list")
    public java.util.List<List> getList() { return list; }
    @JsonProperty("list")
    public void setList(java.util.List<List> value) { this.list = value; }

    @JsonProperty("city")
    public City getCity() { return city; }
    @JsonProperty("city")
    public void setCity(City value) { this.city = value; }

    @Override
    public String toString() {
        return "Hourly{" +
                "cod='" + cod + '\'' +
                ", message=" + message +
                ", cnt=" + cnt +
                ", list=" + list +
                ", city=" + city +
                '}';
    }
}

