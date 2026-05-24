package com.payflow.dto;

import java.math.BigDecimal;

public class UserResponse {

    private Long id;

    private String name;

    private String upiId;

    private String phoneNumber;

    private BigDecimal balance;

    public UserResponse() {
    }

    public Long getId() {
        return id;
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

    public void setId(Long id) {
        this.id = id;
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
}