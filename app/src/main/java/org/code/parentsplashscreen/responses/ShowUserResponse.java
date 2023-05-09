package org.code.parentsplashscreen.responses;

import java.util.List;

import com.google.gson.annotations.SerializedName;

import org.code.parentsplashscreen.models.Father;

public class ShowUserResponse {

    @SerializedName("data")
    private List<Father> data;

    @SerializedName("success")
    private boolean success;

    @SerializedName("message")
    private String message;

    public List<Father> getData() {
        return data;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}