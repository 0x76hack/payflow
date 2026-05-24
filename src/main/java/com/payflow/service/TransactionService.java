package com.payflow.service;

import com.payflow.entity.Transaction;
import com.payflow.entity.User;
import com.payflow.exception.InsufficientBalanceException;
import com.payflow.exception.ResourceNotFoundException;
import com.payflow.repository.TransactionRepository;
import com.payflow.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;

    public TransactionService(
            TransactionRepository transactionRepository,
            UserRepository userRepository) {

        this.transactionRepository = transactionRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public Transaction sendMoney(Transaction transaction) {

        User sender = userRepository
                .findByUpiId(transaction.getSenderUpiId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Sender not found"));

        User receiver = userRepository
                .findByUpiId(transaction.getReceiverUpiId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Receiver not found"));

        if (sender.getBalance()
                .compareTo(transaction.getAmount()) < 0) {

            throw new InsufficientBalanceException("Insufficient balance");
        }

        sender.setBalance(
                sender.getBalance()
                        .subtract(transaction.getAmount())
        );

        receiver.setBalance(
                receiver.getBalance()
                        .add(transaction.getAmount())
        );

        userRepository.save(sender);
        userRepository.save(receiver);

        transaction.setTimestamp(LocalDateTime.now());

        return transactionRepository.save(transaction);
    }
}