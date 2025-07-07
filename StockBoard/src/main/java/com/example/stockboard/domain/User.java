package com.example.stockboard.domain;

public class User {
    private int idx;
    private String userType;
    private String userId;
    private String password;
    private String userName;
    private String createdAt;

    public User() {
    }

    public User(int idx, String userType, String userId, String password, String userName, String createdAt) {
        this.idx = idx;
        this.userType = userType;
        this.userId = userId;
        this.password = password;
        this.userName = userName;
        this.createdAt = createdAt;
    }
    
    public int getIdx() {
        return idx;
    }

    public void setIdx(int idx) {
        this.idx = idx;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
