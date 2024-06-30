package com.example.healthtrack.model;

public class User {
    private String userId;
    private String username;
    private String name;
    private Integer height;
    private Integer weight;
    private String gender;

    public User() {
    }
    public User(String userId, String username) {
        this.userId = userId;
        this.username = username;
    }

    public void updatePersonalInformation(String name, Integer height, Integer weight,
                                          String gender) {
        this.name = name;
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

    // Returns user's name
    public String getName() {
        return name;
    }

    // Returns user's height
    public Integer getHeight() {
        return height;
    }

    // Returns user's weight
    public Integer getWeight() {
        return weight;
    }

    // Returns user's gender
    public String getGender() {
        return gender;
    }
}
