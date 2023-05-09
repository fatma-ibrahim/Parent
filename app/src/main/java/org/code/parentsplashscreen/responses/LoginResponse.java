package org.code.parentsplashscreen.responses;

import com.google.gson.annotations.SerializedName;

import org.code.parentsplashscreen.models.Father;

public class LoginResponse {

    @SerializedName("data")
    private Data data;

    @SerializedName("success")
    private boolean success;

    @SerializedName("message")
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

        @SerializedName("father")
        private Father father;

        @SerializedName("schoolName")
        private String schoolName;

        @SerializedName("token")
        private String token;

        public void setFather(Father father) {
            this.father = father;
        }

        public Father getFather() {
            return father;
        }

        public void setSchoolName(String schoolName) {
            this.schoolName = schoolName;
        }

        public String getSchoolName() {
            return schoolName;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public String getToken() {
            return token;
        }
    }
}