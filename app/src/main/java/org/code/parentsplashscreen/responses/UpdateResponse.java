package org.code.parentsplashscreen.responses;

import com.google.gson.annotations.SerializedName;

public class UpdateResponse {

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

        @SerializedName("trip_id")
        private Object tripId;

        @SerializedName("lng")
        private String lng;

        @SerializedName("mobileNumber")
        private String mobileNumber;

        @SerializedName("created_at")
        private String createdAt;

        @SerializedName("confirmed")
        private boolean confirmed;

        @SerializedName("school_id")
        private int schoolId;

        @SerializedName("updated_at")
        private String updatedAt;

        @SerializedName("image_path")
        private String imagePath;

        @SerializedName("lit")
        private String lit;

        @SerializedName("name")
        private String name;

        @SerializedName("id")
        private int id;

        @SerializedName("region")
        private String region;

        @SerializedName("email")
        private String email;

        @SerializedName("status")
        private int status;

        public void setTripId(Object tripId) {
            this.tripId = tripId;
        }

        public Object getTripId() {
            return tripId;
        }

        public void setLng(String lng) {
            this.lng = lng;
        }

        public String getLng() {
            return lng;
        }

        public void setMobileNumber(String mobileNumber) {
            this.mobileNumber = mobileNumber;
        }

        public String getMobileNumber() {
            return mobileNumber;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public void setConfirmed(boolean confirmed) {
            this.confirmed = confirmed;
        }

        public boolean isConfirmed() {
            return confirmed;
        }

        public void setSchoolId(int schoolId) {
            this.schoolId = schoolId;
        }

        public int getSchoolId() {
            return schoolId;
        }

        public void setUpdatedAt(String updatedAt) {
            this.updatedAt = updatedAt;
        }

        public String getUpdatedAt() {
            return updatedAt;
        }

        public void setImagePath(String imagePath) {
            this.imagePath = imagePath;
        }

        public String getImagePath() {
            return imagePath;
        }

        public void setLit(String lit) {
            this.lit = lit;
        }

        public String getLit() {
            return lit;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getId() {
            return id;
        }

        public void setRegion(String region) {
            this.region = region;
        }

        public String getRegion() {
            return region;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getEmail() {
            return email;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public int getStatus() {
            return status;
        }
    }
}