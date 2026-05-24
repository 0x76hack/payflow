package com.payflow.controller;

import com.payflow.dto.LoginRequest;
import com.payflow.dto.LoginResponse;
import com.payflow.dto.RegisterUserRequest;
import com.payflow.entity.User;
import com.payflow.security.JwtService;
import com.payflow.service.UserService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import com.payflow.dto.UserResponse;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    private final JwtService jwtService;

    public UserController(
            UserService userService,
            JwtService jwtService) {

        this.userService = userService;
        this.jwtService = jwtService;
    }

    private UserResponse mapToResponse(User user) {

        UserResponse response = new UserResponse();

        response.setId(user.getId());
        response.setName(user.getName());
        response.setUpiId(user.getUpiId());
        response.setPhoneNumber(user.getPhoneNumber());
        response.setBalance(user.getBalance());

        return response;
    }

    @PostMapping
    public User registerUser(
            @Valid @RequestBody RegisterUserRequest request) {

        User user = new User();

        user.setName(request.getName());
        user.setUpiId(request.getUpiId());
        user.setPhoneNumber(request.getPhoneNumber());
        user.setBalance(request.getBalance());
        user.setPassword(request.getPassword());

        return userService.registerUser(user);
    }

    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @PostMapping("/login")
    public LoginResponse login(
            @Valid @RequestBody LoginRequest request) {

        User user = userService.authenticateUser(
                request.getUpiId(),
                request.getPassword()
        );

        String token =
                jwtService.generateToken(
                        user.getUpiId());

        return new LoginResponse(token);
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    @GetMapping("/upi/{upiId}")
    public User getUserByUpiId(
            @PathVariable String upiId) {

        return userService.findByUpiId(upiId);
    }
}