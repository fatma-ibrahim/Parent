package org.code.parentsplashscreen.responses;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RegisterResponse {

    @SerializedName("data")
    private Data data = new Data();

    @SerializedName("success")
    private boolean success;

    @SerializedName("message")
    private String message;

    public Data getData() {
        return data;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public class Data {

        @SerializedName("father_name")
        private String fatherName;

        @SerializedName("password")
        private List<String> password;

        @SerializedName("code")
        private List<Object> code;

        @SerializedName("lng")
        private List<String> lng;

        @SerializedName("mobileNumber")
        private List<String> mobileNumber;

        @SerializedName("lit")
        private List<String> lit;

        @SerializedName("name")
        private List<String> name;

        @SerializedName("region")
        private List<String> region;

        @SerializedName("email")
        private List<String> email;

        public List<String> getPassword() {
            return password;
        }

        public List<Object> getCode() {
            return code;
        }

        public List<String> getLng() {
            return lng;
        }

        public List<String> getMobileNumber() {
            return mobileNumber;
        }

        public List<String> getLit() {
            return lit;
        }

        public List<String> getName() {
            return name;
        }

        public List<String> getRegion() {
            return region;
        }

        public List<String> getEmail() {
            return email;
        }

        public String getFatherName() {
            return fatherName;
        }
    }
}