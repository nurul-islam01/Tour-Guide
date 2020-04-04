package com.nit.tourguide.pojos.weather.hourly;


import java.util.*;
import java.io.IOException;
import com.fasterxml.jackson.annotation.*;

public enum MainEnum {
    CLEAR, CLOUDS, RAIN;

    @JsonValue
    public String toValue() {
        switch (this) {
            case CLEAR: return "Clear";
            case CLOUDS: return "Clouds";
            case RAIN: return "Rain";
        }
        return null;
    }

    @JsonCreator
    public static MainEnum forValue(String value) throws IOException {
        if (value.equals("Clear")) return CLEAR;
        if (value.equals("Clouds")) return CLOUDS;
        if (value.equals("Rain")) return RAIN;
        throw new IOException("Cannot deserialize MainEnum");
    }
}
