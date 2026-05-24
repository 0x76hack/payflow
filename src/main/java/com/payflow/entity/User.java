package com.payflow.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(unique = true)
    private String upiId;

    private String password;

    private String phoneNumber;

    private BigDecimal balance;

    public User() {
    }

    public User(Long id, String name, String upiId,
                String phoneNumber, BigDecimal balance) {
        this.id = id;
        this.name = name;
        this.upiId = upiId;
        this.phoneNumber = phoneNumber;
        this.balance = balance;
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
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}