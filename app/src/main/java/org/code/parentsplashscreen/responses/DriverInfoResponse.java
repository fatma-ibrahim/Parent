package org.code.parentsplashscreen.responses;

import com.google.gson.annotations.SerializedName;

import org.code.parentsplashscreen.models.Driver;
import org.code.parentsplashscreen.models.Vehicle;
import org.code.parentsplashscreen.models.VehicleItem;

import java.util.List;

public class DriverInfoResponse {
    private Data data;
    private boolean success;
    private String message;

    public void setData(Data data) {
        this.data = data;
    }

    public Data getData() {
        return data;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public class Data {
        private Driver driver;
        private Vehicle vehicle;

        public void setDriver(Driver driver) {
            this.driver = driver;
        }

        public Driver getDriver() {
            return driver;
        }

        public void setVehicle(Vehicle vehicle) {
            this.vehicle = vehicle;
        }

        public Vehicle getVehicle() {
            return vehicle;
        }
    }
}