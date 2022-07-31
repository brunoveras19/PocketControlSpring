package com.veras.pocketcontrol.services;

import com.veras.pocketcontrol.models.Schedule;
import com.veras.pocketcontrol.models.Transaction;
import com.veras.pocketcontrol.repositories.ScheduleRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;


@AllArgsConstructor
@Service
public class ScheduleService {
    private final ScheduleRepository ScheduleRepository;

    private final UserService userService;
    public Optional<List<Schedule>> getAllSchedules() {
        return ScheduleRepository.findAllByUserId(userService.getLoggedUserId());
    }

    public Optional<Schedule> getSchedule(String id) {
        return ScheduleRepository.findByIdAndUserId(id, userService.getLoggedUserId());
    }

    public Schedule insertSchedule(Schedule schedule) {
        Schedule scheduleInserted = ScheduleRepository.insert(schedule);
        return scheduleInserted;
    }

    public Schedule updateSchedule(Schedule schedule) {
        Schedule scheduleToUpdate = ScheduleRepository.save(schedule);
        return scheduleToUpdate;
    }

    public Schedule deleteSchedule(String id) {
        Schedule ScheduleDeleted = ScheduleRepository.findById(id).get();
        ScheduleRepository.deleteById(id);
        return ScheduleDeleted;
    }

    public void CreateScheduledTransactions(){
        List<Schedule> schedulesForToday = this.getSchedulesForToday();
        if(schedulesForToday.isEmpty()) {
            insertTransactionsScheduled(schedulesForToday);
            this.updateInsertedSchedules(schedulesForToday);
        } else {
            System.out.println("Nenhuma transação a ser cadastrada");
        }
    }

    private void updateInsertedSchedules(List<Schedule> schedulesForToday) {
        schedulesForToday.forEach(schedule -> {
            schedule.setAlreadyInsertedToday(true);
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

    public List<Schedule> getSchedulesForToday() {
        return ScheduleRepository.findByDayOfMonthAndAlreadyInsertedTodayIsFalse(Calendar.getInstance().DAY_OF_MONTH);
    }
}