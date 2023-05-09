package org.code.parentsplashscreen.responses;

import com.google.gson.annotations.SerializedName;

import org.code.parentsplashscreen.models.School;
import org.code.parentsplashscreen.models.Trip;

public class ShowTripResponse {

    @SerializedName("data")
    private String data;

    @SerializedName("success")
    private boolean success;

    @SerializedName("message")
    private Message message;

    public void setData(String data) {
        this.data = data;
    }

    public String getData() {
        return data;
    }

    public boolean isSuccess() {
        return success;
    }

    public Message getMessage() {
        return message;
    }


    public class Message {

        @SerializedName("trip")
        private Trip trip;

        @SerializedName("school")
        private School school;

        public Trip getTrip() {
            return trip;
        }

        public School getSchool() {
            return school;
        }
    }

}