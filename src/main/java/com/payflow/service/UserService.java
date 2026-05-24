package com.payflow.service;

import com.payflow.entity.User;
import com.payflow.exception.ResourceNotFoundException;
import com.payflow.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder) {

        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User registerUser(User user) {

        String encodedPassword =
                passwordEncoder.encode(user.getPassword());

        user.setPassword(encodedPassword);

        return userRepository.save(user);
    }

    public User authenticateUser(
            String upiId,
            String password) {

        User user = userRepository.findByUpiId(upiId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Invalid credentials"));

        boolean passwordMatches =
                passwordEncoder.matches(
                        password,
                        user.getPassword());

        if (!passwordMatches) {
            throw new RuntimeException("Invalid credentials");
        }

        return user;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));
    }

    public User findByUpiId(String upiId) {
        return userRepository.findByUpiId(upiId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("UPI ID not found"));
    }
}