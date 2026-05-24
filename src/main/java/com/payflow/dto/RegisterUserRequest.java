package com.payflow.dto;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public class RegisterUserRequest {

    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "UPI ID is required")
    private String upiId;

    @NotBlank(message = "Password is required")
    private String password;

    @NotBlank(message = "Phone number is required")
    private String phoneNumber;

    @NotNull(message = "Balance is required")
    @DecimalMin(value = "0.0",
            message = "Balance cannot be negative")
    private BigDecimal balance;

    public RegisterUserRequest() {
    }

    public String getName() {
        return name;
    }

    public String getUpiId() {
        return upiId;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUpiId(String upiId) {
        this.upiId = upiId;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}