package com.veras.pocketcontrol.services;

import com.veras.pocketcontrol.models.Transaction;
import com.veras.pocketcontrol.repositories.TransactionRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;

    private final CategoryService categoryService;

    private  final UserService userService;
    public Optional<List<Transaction>> getAllTransactions() {
        return transactionRepository.findAllByUserId(userService.getLoggedUserId());
    }

    public Optional<Transaction> getTransaction(String id) {
        return transactionRepository.findByIdAndUserId(id, userService.getLoggedUserId());
    }

    public Transaction insertTransaction(Transaction transaction) {
        transaction.setCategory(categoryService.getCategory(transaction.getCategoryId()).get());
        transaction.setUserId(userService.getLoggedUserId());
        Transaction transactionInserted = transactionRepository.insert(transaction);
        return transactionInserted;
    }

    public Transaction updateTransaction(Transaction transaction) {
        Transaction transactionInserted = transactionRepository.save(transaction);
        return transactionInserted;
    }

    public Transaction deleteTransaction(String id) {
        Transaction transactionDeleted = transactionRepository.findById(id).get();
        transactionRepository.deleteById(id);
        transactionDeleted.setId(null);
        return transactionDeleted;
    }
}
