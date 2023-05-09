package org.code.parentsplashscreen.responses;

import com.google.gson.annotations.SerializedName;

import org.code.parentsplashscreen.models.Child;

public class ChildResponse {

    @SerializedName("data")
    private Child child;

    @SerializedName("success")
    private boolean success;

    @SerializedName("message")
    private String message;

    public void setData(Child child) {
        this.child = child;
    }

    public Child getData() {
        return child;
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
}