package com.payflow.dto;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public class TransferRequest {

    @NotBlank(message = "Sender UPI ID required")
    private String senderUpiId;

    @NotBlank(message = "Receiver UPI ID required")
    private String receiverUpiId;

    @NotNull(message = "Amount required")
    @DecimalMin(value = "1.0",
            message = "Transfer amount must be positive")
    private BigDecimal amount;

    public TransferRequest() {
    }

    public String getSenderUpiId() {
        return senderUpiId;
    }

    public String getReceiverUpiId() {
        return receiverUpiId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setSenderUpiId(String senderUpiId) {
        this.senderUpiId = senderUpiId;
    }

    public void setReceiverUpiId(String receiverUpiId) {
        this.receiverUpiId = receiverUpiId;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}