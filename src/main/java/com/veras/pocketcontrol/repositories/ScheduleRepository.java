package com.veras.pocketcontrol.repositories;

import com.veras.pocketcontrol.models.Category;
import com.veras.pocketcontrol.models.Schedule;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ScheduleRepository extends MongoRepository<Schedule, String> {
    List<Schedule> findByDayOfMonthAndUserIdAndAlreadyInsertedIsFalse(Integer dayOfMonth, String userId);
    Optional<List<Schedule>> findAllByUserId(String userId);
    Optional<Schedule> findByIdAndUserId(String id,String userId);
    @Query("SELECT s from Schedule s WHERE s.dayOfMonth between ?1 AND ?2 AND s.alreadyInserted = false AND s.userId = ?3")
    Optional<List<Schedule>> findByIntervalDayOfMouthNotInserted(Integer lastLoginDay, Integer yesterday, String userId);
}
