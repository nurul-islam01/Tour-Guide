package com.nit.tourguide.pojos;

import com.google.android.gms.maps.model.LatLng;

public class ClusterItem {
    private LatLng latLng;
    private String title;
    private String Snippet;
    private String placeId;

    public ClusterItem(LatLng latLng, String title, String snippet, String placeId) {
        this.latLng = latLng;
        this.title = title;
        Snippet = snippet;
        this.placeId = placeId;
    }

    public LatLng getLatLng() {
        return latLng;
    }

    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSnippet() {
        return Snippet;
    }

    public void setSnippet(String snippet) {
        Snippet = snippet;
    }

    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }
}
