package org.code.parentsplashscreen.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class LocationRealTime {

    @SerializedName("trip_id")
    private String tripId;

    @SerializedName("lng")
    private String lng;

    @SerializedName("lit")
    private String lit;

    @SerializedName("parent_id")
    private String parent_id;


    public LocationRealTime(String lng, String lit) {
        this.lng = lng;
        this.lit = lit;
    }

    public void setTripId(String tripId) {
        this.tripId = tripId;
    }

    public String getTripId() {
        return tripId;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getLng() {
        return lng;
    }

    public void setLit(String lit) {
        this.lit = lit;
    }

    public String getLit() {
        return lit;
    }

    public String getParent_id() {
        return parent_id;
    }
}