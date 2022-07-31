package com.veras.pocketcontrol.repositories;

import com.veras.pocketcontrol.models.Category;
import com.veras.pocketcontrol.models.Schedule;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface ScheduleRepository extends MongoRepository<Schedule, String> {
    public List<Schedule> findByDayOfMonthAndAlreadyInsertedTodayIsFalse(Integer dayOfMonth);

    public Optional<List<Schedule>> findAllByUserId(String userId);
    public Optional<Schedule> findByIdAndUserId(String id,String userId);
}
