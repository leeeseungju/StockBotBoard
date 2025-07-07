package com.example.stockboard.service.impl;

import com.example.stockboard.domain.User;
import com.example.stockboard.mapper.UserMapper;
import com.example.stockboard.service.UserService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public boolean join(User user) {
        // 아이디 중복 확인
        User existingUser = userMapper.findByUserId(user.getUserId());
        if (existingUser != null) {
            System.out.println("❌ 이미 존재하는 아이디: " + user.getUserId());
            return false;
        }

        // 🔐 비밀번호 암호화 후 저장
        String hashedPw = passwordEncoder.encode(user.getPassword());
        user.setPassword(hashedPw);

        userMapper.insertUser(user);
        System.out.println("✅ 회원가입 완료: " + user.getUserId());
        return true;
    }

    @Override
    public User login(String userId, String rawPassword) {
        User user = userMapper.findByUserId(userId);

        if (user == null) {
            System.out.println("❌ 존재하지 않는 사용자: " + userId);
            return null;
        }

        if (passwordEncoder.matches(rawPassword, user.getPassword())) {
            System.out.println("✅ 로그인 성공: " + userId);
            return user;
        } else {
            System.out.println("❌ 비밀번호 불일치: " + userId);
            return null;
        }
    }
    
    @Override
    public List<User> getAllUsers() {
        return userMapper.getAllUsers();
    }
    
    @Override
    public void updateUser(User user) {
        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            String hashedPw = passwordEncoder.encode(user.getPassword());
            user.setPassword(hashedPw);
        }

        userMapper.updateUser(user);
    }
}
