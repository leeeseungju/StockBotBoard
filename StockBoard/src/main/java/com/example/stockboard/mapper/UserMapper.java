package com.example.stockboard.mapper;

import com.example.stockboard.domain.User;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserMapper {
    void insertUser(User user);
    User findByUserId(String userId);
    User findByIdAndPassword(@Param("userId") String userId, @Param("password") String password);
    List<User> getAllUsers();
    void updateUser(User user);
}
