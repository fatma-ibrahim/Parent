package org.code.parentsplashscreen.responses;

import com.google.gson.annotations.SerializedName;

public class UpdateChildStatusResponse {

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

        @SerializedName("gender")
        private String gender;

        @SerializedName("updated_at")
        private String updatedAt;

        @SerializedName("image_path")
        private String imagePath;

        @SerializedName("name")
        private String name;

        @SerializedName("created_at")
        private String createdAt;

        @SerializedName("id")
        private int id;

        @SerializedName("father_id")
        private int fatherId;

        @SerializedName("class")
        private String jsonMemberClass;

        @SerializedName("confirmed")
        private int confirmed;

        @SerializedName("age")
        private int age;

        @SerializedName("status")
        private boolean status;

        public void setGender(String gender) {
            this.gender = gender;
        }

        public String getGender() {
            return gender;
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

        public void setName(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getId() {
            return id;
        }

        public void setFatherId(int fatherId) {
            this.fatherId = fatherId;
        }

        public int getFatherId() {
            return fatherId;
        }

        public void setJsonMemberClass(String jsonMemberClass) {
            this.jsonMemberClass = jsonMemberClass;
        }

        public String getJsonMemberClass() {
            return jsonMemberClass;
        }

        public void setConfirmed(int confirmed) {
            this.confirmed = confirmed;
        }

        public int getConfirmed() {
            return confirmed;
        }

        public void setAge(int age) {
            this.age = age;
        }

        public int getAge() {
            return age;
        }

        public void setStatus(boolean status) {
            this.status = status;
        }

        public boolean isStatus() {
            return status;
        }
    }
}