package org.code.parentsplashscreen.responses;

import java.util.List;

import com.google.gson.annotations.SerializedName;

import org.code.parentsplashscreen.models.Child;

public class ChildrensResponse {

    @SerializedName("data")
    private List<Child> data;

    @SerializedName("success")
    private boolean success;

    @SerializedName("message")
    private String message;

    public void setData(List<Child> data) {
        this.data = data;
    }

    public List<Child> getData() {
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


}