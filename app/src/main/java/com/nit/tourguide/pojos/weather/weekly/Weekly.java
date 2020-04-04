package com.nit.tourguide.pojos.weather.weekly;

import java.util.*;
import com.fasterxml.jackson.annotation.*;

public class Weekly {
    private City city;
    private String cod;
    private double message;
    private long cnt;
    private java.util.List<List> list;

    @JsonProperty("city")
    public City getCity() { return city; }
    @JsonProperty("city")
    public void setCity(City value) { this.city = value; }

    @JsonProperty("cod")
    public String getCod() { return cod; }
    @JsonProperty("cod")
    public void setCod(String value) { this.cod = value; }

    @JsonProperty("message")
    public double getMessage() { return message; }
    @JsonProperty("message")
    public void setMessage(double value) { this.message = value; }

    @JsonProperty("cnt")
    public long getCnt() { return cnt; }
    @JsonProperty("cnt")
    public void setCnt(long value) { this.cnt = value; }

    @JsonProperty("list")
    public java.util.List<List> getList() { return list; }
    @JsonProperty("list")
    public void setList(java.util.List<List> value) { this.list = value; }

    @Override
    public String toString() {
        return "Weekly{" +
                "city=" + city +
                ", cod='" + cod + '\'' +
                ", message=" + message +
                ", cnt=" + cnt +
                ", list=" + list +
                '}';
    }
}

