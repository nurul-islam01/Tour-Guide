package com.nit.tourguide.pojos.weather.current;

import java.util.*;
import com.fasterxml.jackson.annotation.*;

public class Weather {
    private long id;
    private String main;
    private String description;
    private String icon;

    @JsonProperty("id")
    public long getID() { return id; }
    @JsonProperty("id")
    public void setID(long value) { this.id = value; }

    @JsonProperty("main")
    public String getMain() { return main; }
    @JsonProperty("main")
    public void setMain(String value) { this.main = value; }

    @JsonProperty("description")
    public String getDescription() { return description; }
    @JsonProperty("description")
    public void setDescription(String value) { this.description = value; }

    @JsonProperty("icon")
    public String getIcon() { return icon; }
    @JsonProperty("icon")
    public void setIcon(String value) { this.icon = value; }
}
