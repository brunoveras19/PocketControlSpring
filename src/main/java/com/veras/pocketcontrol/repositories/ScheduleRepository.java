package com.veras.pocketcontrol.repositories;

import com.veras.pocketcontrol.models.Category;
import com.veras.pocketcontrol.models.Schedule;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ScheduleRepository extends MongoRepository<Schedule, String> {
    Optional<List<Schedule>> findAllByUserId(String userId);
    Optional<Schedule> findByIdAndUserId(String id,String userId);
    Optional<List<Schedule>> findByDayOfMonthAndHasDefinedAmountIsTrueAndUserId(Integer dayOfMonth, String userId);

    Optional<List<Schedule>> findByDayOfMonthAndHasDefinedAmountIsFalseAndUserId(Integer dayOfMonth, String userId);
}
