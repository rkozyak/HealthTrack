package com.example.healthtrack.model;

public class User {
    private String userId;
    private String username;
    private Integer height;
    private Integer weight;
    private String gender;

    public User() {
    }
    public User(String userId, String username) {
        this.userId = userId;
        this.username = username;
    }

    public void updatePersonalInformation(Integer height, Integer weight, String gender) {
        this.height = height;
        this.weight = weight;
        this.gender = gender;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserId() {
        return userId;
    }
}
