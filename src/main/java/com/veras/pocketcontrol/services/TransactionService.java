package com.veras.pocketcontrol.services;

import com.veras.pocketcontrol.models.Transaction;
import com.veras.pocketcontrol.repositories.TransactionRepository;
import lombok.AllArgsConstructor;

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
        return transactionRepository.findAllByUserIdOrderByDateDesc(userService.getLoggedUserId());
    }

    public Optional<Transaction> getTransaction(String id) {
        return transactionRepository.findByIdAndUserId(id, userService.getLoggedUserId());
    }

    public Transaction insertTransaction(Transaction transaction) {
        if(transaction.getCategory() == null)
            transaction.setCategory(categoryService.getCategory(transaction.getCategoryId()).get());
        if(transaction.getUserId() == null)
            transaction.setUserId(userService.getLoggedUserId());
        Transaction transactionInserted = transactionRepository.insert(transaction);
        return transactionInserted;
    }

    public Transaction updateTransaction(Transaction transaction) {
        Transaction transactionToUpdate = transactionRepository.findById(transaction.getId()).get();
        verifyAndUpdateFieldsWithValue(transaction, transactionToUpdate);
        Transaction transactionInserted = transactionRepository.save(transactionToUpdate);
        return transactionInserted;
    }

    public Transaction deleteTransaction(String id) {
        Transaction transactionDeleted = transactionRepository.findById(id).get();
        transactionRepository.deleteById(id);
        transactionDeleted.setId(null);
        return transactionDeleted;
    }

    public Double getBalance() {
        Double balance = 0.0;
        Optional<List<Transaction>> transactions = this.getAllTransactions();
        for(Transaction transaction :  transactions.get()){
            balance += transaction.getAmount();
        }
        return Math.round(balance * 100) / 100d;
    }

    private void verifyAndUpdateFieldsWithValue(Transaction transaction, Transaction transactionToUpdate) {
        transactionToUpdate.setAmount(transaction.getAmount() != null ? transaction.getAmount() : transactionToUpdate.getAmount());
        transactionToUpdate.setDescription(transaction.getDescription() != null ? transaction.getDescription() : transactionToUpdate.getDescription());
        transactionToUpdate.setCategoryId(transaction.getCategoryId() != null ? transaction.getCategoryId() : transactionToUpdate.getCategoryId());
        transactionToUpdate.setCategory(transaction.getCategoryId() != null ? categoryService.getCategory(transaction.getCategoryId()).get() : categoryService.getCategory(transactionToUpdate.getCategoryId()).get());
        transactionToUpdate.setUserId(userService.getLoggedUserId());
    }


}
