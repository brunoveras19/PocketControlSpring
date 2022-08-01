package com.veras.pocketcontrol.services;

import com.veras.pocketcontrol.models.Schedule;
import com.veras.pocketcontrol.models.Transaction;
import com.veras.pocketcontrol.repositories.ScheduleRepository;
import com.veras.pocketcontrol.utils.Consts;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;


@AllArgsConstructor
@Service
public class ScheduleService {
    private final ScheduleRepository scheduleRepository;

    private final UserService userService;
    public Optional<List<Schedule>> getAllSchedules() {
        return scheduleRepository.findAllByUserId(userService.getLoggedUserId());
    }

    public Optional<Schedule> getSchedule(String id) {
        return scheduleRepository.findByIdAndUserId(id, userService.getLoggedUserId());
    }

    public Schedule insertSchedule(Schedule schedule) {
        Schedule scheduleInserted = scheduleRepository.insert(schedule);
        return scheduleInserted;
    }

    public Schedule updateSchedule(Schedule schedule) {
        Schedule scheduleToUpdate = scheduleRepository.save(schedule);
        return scheduleToUpdate;
    }

    public Schedule deleteSchedule(String id) {
        Schedule ScheduleDeleted = scheduleRepository.findById(id).get();
        scheduleRepository.deleteById(id);
        return ScheduleDeleted;
    }

    public void createScheduledTransactions(){
        List<Schedule> schedulesForToday = this.getSchedulesForToday();
        if(schedulesForToday.isEmpty()) {
            insertTransactionsScheduled(schedulesForToday);
            this.updateInsertedSchedules(schedulesForToday);
        } else {
            System.out.println(Consts.NO_TRANSACTIONS_TO_INSERT_MESSAGE);
        }
    }

    public List<Schedule> getSchedulesForToday() {
        List<Schedule> schedulesToCreateToday = scheduleRepository.findByDayOfMonthAndUserIdAndAlreadyInsertedIsFalse(Calendar.getInstance().DAY_OF_MONTH, userService.getLoggedUserId());
        int yesterday = LocalDateTime.now().getDayOfMonth() - 1;
        List<Schedule> schedulesToCreateSinceLastLogin = scheduleRepository.findByIntervalDayOfMouthNotInserted(userService.getLastLoginDay(), yesterday, userService.getLoggedUserId()).get();
        schedulesToCreateToday.addAll(schedulesToCreateSinceLastLogin);
        return schedulesToCreateToday;
    }


    private void updateInsertedSchedules(List<Schedule> schedulesForToday) {
        schedulesForToday.forEach(schedule -> {
            schedule.setAlreadyInserted(true);
            this.updateSchedule(schedule);
        });
    }

    private void insertTransactionsScheduled(List<Schedule> schedulesForToday) {
        schedulesForToday.forEach(schedule -> {
            Transaction transactionToInsert = new Transaction();
            transactionToInsert.setAmount(schedule.getBaseTransaction().getAmount());
            transactionToInsert.setDate(LocalDateTime.now());
            transactionToInsert.setId(null);
        });
    }
}