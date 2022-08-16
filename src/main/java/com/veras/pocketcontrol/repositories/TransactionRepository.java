package com.veras.pocketcontrol.repositories;

import com.veras.pocketcontrol.models.Transaction;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface TransactionRepository extends MongoRepository<Transaction, String> {
    public Optional<List<Transaction>> findAllByUserIdOrderByDateDesc(String userId);
    public Optional<Transaction> findByIdAndUserId(String id,String userId);
}
