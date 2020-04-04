package com.nit.tourguide.pojos.weather.hourly;


import java.util.*;
import java.io.IOException;
import com.fasterxml.jackson.annotation.*;

public enum Pod {
    D, N;

    @JsonValue
    public String toValue() {
        switch (this) {
            case D: return "d";
            case N: return "n";
        }
        return null;
    }

    @JsonCreator
    public static Pod forValue(String value) throws IOException {
        if (value.equals("d")) return D;
        if (value.equals("n")) return N;
        throw new IOException("Cannot deserialize Pod");
    }
}