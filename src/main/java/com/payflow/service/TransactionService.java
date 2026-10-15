package com.payflow.service;

import com.payflow.entity.Transaction;
import com.payflow.entity.User;
import com.payflow.exception.InsufficientBalanceException;
import com.payflow.exception.ResourceNotFoundException;
import com.payflow.repository.TransactionRepository;
import com.payflow.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
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
        if (transaction == null || transaction.getAmount() == null
                || transaction.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Transfer amount must be positive");
        }

        String senderUpiId = transaction.getSenderUpiId();
        String receiverUpiId = transaction.getReceiverUpiId();
        if (senderUpiId == null || senderUpiId.isBlank()
                || receiverUpiId == null || receiverUpiId.isBlank()) {
            throw new IllegalArgumentException("Sender and receiver UPI IDs are required");
        }
        if (senderUpiId.equals(receiverUpiId)) {
            throw new IllegalArgumentException("Sender and receiver must be different");
        }

        // Acquire pessimistic row locks in a stable order to avoid concurrent
        // transfers from both directions deadlocking on the same two accounts.
        User sender;
        User receiver;
        if (senderUpiId.compareTo(receiverUpiId) < 0) {
            sender = lockUser(senderUpiId, "Sender");
            receiver = lockUser(receiverUpiId, "Receiver");
        } else {
            receiver = lockUser(receiverUpiId, "Receiver");
            sender = lockUser(senderUpiId, "Sender");
        }

        if (sender.getBalance() == null
                || sender.getBalance().compareTo(transaction.getAmount()) < 0) {
            throw new InsufficientBalanceException("Insufficient balance");
        }

        sender.setBalance(sender.getBalance().subtract(transaction.getAmount()));
        receiver.setBalance(receiver.getBalance().add(transaction.getAmount()));

        userRepository.save(sender);
        userRepository.save(receiver);
        transaction.setTimestamp(LocalDateTime.now());

        return transactionRepository.save(transaction);
    }

    private User lockUser(String upiId, String role) {
        return userRepository.findByUpiIdForUpdate(upiId)
                .orElseThrow(() -> new ResourceNotFoundException(role + " not found"));
    }
}
