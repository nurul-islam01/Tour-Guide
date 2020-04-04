package com.nit.tourguide.pojos.weather.weekly;


import java.util.*;
import java.io.IOException;
import com.fasterxml.jackson.annotation.*;

public enum Icon {
    THE_01_D, THE_03_D, THE_10_D;

    @JsonValue
    public String toValue() {
        switch (this) {
            case THE_01_D: return "01d";
            case THE_03_D: return "03d";
            case THE_10_D: return "10d";
        }
        return null;
    }

    @JsonCreator
    public static Icon forValue(String value) throws IOException {
        if (value.equals("01d")) return THE_01_D;
        if (value.equals("03d")) return THE_03_D;
        if (value.equals("10d")) return THE_10_D;
        throw new IOException("Cannot deserialize Icon");
    }
}

