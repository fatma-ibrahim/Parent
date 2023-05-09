package org.code.parentsplashscreen.models;

import com.google.gson.annotations.SerializedName;

public class Child {
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

    public Child(String gender, String imagePath, String name, String jsonMemberClass, int age) {
        this.gender = gender;
        this.imagePath = imagePath;
        this.name = name;
        this.jsonMemberClass = jsonMemberClass;
        this.age = age;
    }

    public Child(int id, String gender, String imagePath, String name, String jsonMemberClass, int age) {
        this.gender = gender;
        this.imagePath = imagePath;
        this.name = name;
        this.jsonMemberClass = jsonMemberClass;
        this.age = age;
        this.id = id;
    }

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

    public int isConfirmed() {
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
