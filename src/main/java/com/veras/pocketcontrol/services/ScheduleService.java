package com.veras.pocketcontrol.services;

import com.veras.pocketcontrol.models.Schedule;
import com.veras.pocketcontrol.models.Transaction;
import com.veras.pocketcontrol.repositories.ScheduleRepository;
import com.veras.pocketcontrol.utils.Consts;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;


@AllArgsConstructor
@Service
public class ScheduleService {
    private final ScheduleRepository scheduleRepository;
    private final TransactionService transactionService;

    private final UserService userService;
    public Optional<List<Schedule>> getAllSchedules() {
        return scheduleRepository.findAllByUserId(userService.getLoggedUserId());
    }

    public Optional<Schedule> getSchedule(String id) {
        return scheduleRepository.findByIdAndUserId(id, userService.getLoggedUserId());
    }

    public Schedule insertSchedule(Schedule schedule) {
        Transaction transaction = transactionService.getTransaction(schedule.getTransactionId()).get();
        schedule.setBaseTransaction(transaction);
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
    @Scheduled(cron  = "0 0 01 * * ?")
    public void createScheduledTransactions(){
        List<Schedule> schedulesForToday = this.getSchedulesForToday();
        if(!schedulesForToday.isEmpty()) {
            insertTransactionsScheduled(schedulesForToday);
        } else {
            System.out.println(Consts.NO_TRANSACTIONS_TO_INSERT_MESSAGE);
        }
    }

    public List<Schedule> getSchedulesForToday() {
        int today = LocalDateTime.now().getDayOfMonth();
        List<Schedule> schedulesToCreateToday = scheduleRepository.findByDayOfMonth(today).get();
        return schedulesToCreateToday;
    }

    private void insertTransactionsScheduled(List<Schedule> schedulesForToday) {
        schedulesForToday.forEach(schedule -> {
            Transaction transactionToInsert = new Transaction();
            transactionToInsert.setCategory(schedule.getBaseTransaction().getCategory());
            transactionToInsert.setCategoryId(schedule.getBaseTransaction().getCategoryId());
            transactionToInsert.setDescription(Consts.SCHEDULED_TRANSACTION_TAG + schedule.getBaseTransaction().getDescription());
            transactionToInsert.setAmount(schedule.getBaseTransaction().getAmount());
            transactionToInsert.setDate(LocalDateTime.now());
            transactionToInsert.setUserId(schedule.getBaseTransaction().getUserId());
            transactionToInsert.setId(null);

            transactionService.insertTransaction(transactionToInsert);
        });
    }

}