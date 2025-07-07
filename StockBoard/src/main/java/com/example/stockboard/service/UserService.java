package com.example.stockboard.service;

import java.util.List;

import com.example.stockboard.domain.User;

public interface UserService {
    boolean join(User user);
    User login(String userId, String password);
    List<User> getAllUsers();
    void updateUser(User user);
}