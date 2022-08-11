package com.veras.pocketcontrol.repositories;

import com.veras.pocketcontrol.models.Category;
import com.veras.pocketcontrol.models.Transaction;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends MongoRepository<Category, String> {
    public Optional<List<Category>> findAllByUserId(String userId);
    public Optional<Category> findByIdAndUserId(String id,String userId);


}
