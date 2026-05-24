package com.payflow.controller;

import com.payflow.dto.TransferRequest;
import com.payflow.entity.Transaction;
import com.payflow.service.TransactionService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(
            TransactionService transactionService) {

        this.transactionService = transactionService;
    }

    @PostMapping
    public Transaction sendMoney(
            @Valid @RequestBody TransferRequest request) {

        Transaction transaction = new Transaction();

        transaction.setSenderUpiId(
                request.getSenderUpiId());

        transaction.setReceiverUpiId(
                request.getReceiverUpiId());

        transaction.setAmount(
                request.getAmount());

        return transactionService.sendMoney(transaction);
    }
}