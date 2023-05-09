package org.code.parentsplashscreen.models;

import com.google.gson.annotations.SerializedName;

public class VehicleItem {

    @SerializedName("driver_id")
    private int driverId;

    @SerializedName("licensePlate")
    private String licensePlate;

    @SerializedName("school_id")
    private int schoolId;

    @SerializedName("color")
    private String color;

    @SerializedName("updated_at")
    private String updatedAt;

    @SerializedName("created_at")
    private String createdAt;

    @SerializedName("model")
    private String model;

    @SerializedName("id")
    private int id;

    public void setDriverId(int driverId) {
        this.driverId = driverId;
    }

    public int getDriverId() {
        return driverId;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public void setSchoolId(int schoolId) {
        this.schoolId = schoolId;
    }

    public int getSchoolId() {
        return schoolId;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getColor() {
        return color;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getModel() {
        return model;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}