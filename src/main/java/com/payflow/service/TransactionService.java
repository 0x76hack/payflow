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
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;

    public TransactionService(TransactionRepository transactionRepository,
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
        String senderId = transaction.getSenderUpiId();
        String receiverId = transaction.getReceiverUpiId();
        if (senderId == null || receiverId == null || senderId.isBlank()
                || receiverId.isBlank()) {
            throw new IllegalArgumentException("Sender and receiver are required");
        }
        if (senderId.equals(receiverId)) {
            throw new IllegalArgumentException("Sender and receiver must be different");
        }

        List<User> locked = userRepository.findAccountsForUpdate(
                List.of(senderId, receiverId));
        Map<String, User> byUpi = locked.stream()
                .collect(Collectors.toMap(User::getUpiId, Function.identity()));
        User sender = byUpi.get(senderId);
        User receiver = byUpi.get(receiverId);
        if (sender == null) {
            throw new ResourceNotFoundException("Sender not found");
        }
        if (receiver == null) {
            throw new ResourceNotFoundException("Receiver not found");
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
}
