package com.nit.tourguide.pojos.weather.hourly;

import java.util.*;
import java.io.IOException;
import com.fasterxml.jackson.annotation.*;

public enum Description {
    CLEAR_SKY, FEW_CLOUDS, LIGHT_RAIN, MODERATE_RAIN, SCATTERED_CLOUDS;

    @JsonValue
    public String toValue() {
        switch (this) {
            case CLEAR_SKY: return "clear sky";
            case FEW_CLOUDS: return "few clouds";
            case LIGHT_RAIN: return "light rain";
            case MODERATE_RAIN: return "moderate rain";
            case SCATTERED_CLOUDS: return "scattered clouds";
        }
        return null;
    }

    @JsonCreator
    public static Description forValue(String value) throws IOException {
        if (value.equals("clear sky")) return CLEAR_SKY;
        if (value.equals("few clouds")) return FEW_CLOUDS;
        if (value.equals("light rain")) return LIGHT_RAIN;
        if (value.equals("moderate rain")) return MODERATE_RAIN;
        if (value.equals("scattered clouds")) return SCATTERED_CLOUDS;
        throw new IOException("Cannot deserialize Description");
    }
}
