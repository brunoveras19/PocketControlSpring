package com.veras.pocketcontrol.services;

import com.veras.pocketcontrol.models.Schedule;
import com.veras.pocketcontrol.models.Transaction;
import com.veras.pocketcontrol.repositories.ScheduleRepository;
import com.veras.pocketcontrol.utils.Consts;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@AllArgsConstructor
@Service
@Slf4j
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
        Transaction transaction = transactionService.getTransaction(schedule.getId()).get();
        schedule.setBaseTransaction(transaction);
        schedule.setUserId(transaction.getUserId());
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
        List<Schedule> schedulesForToday = this.getSchedulesToCreateToday();
        if(!schedulesForToday.isEmpty()) {
            insertTransactionsScheduled(schedulesForToday);
        } else {
            log.info(Consts.NO_TRANSACTIONS_TO_INSERT_MESSAGE);
        }
    }

    public List<Schedule> getSchedulesToCreateToday() {
        int today = LocalDateTime.now().getDayOfMonth();
        List<Schedule> schedulesToCreateToday = scheduleRepository
                .findByDayOfMonthAndIsFixedValueIsTrueAndUserId(today, userService.getLoggedUserId()).get();
        return schedulesToCreateToday;
    }

    public List<Schedule> getSchedulesToNotifyToday() {
        int today = LocalDateTime.now().getDayOfMonth();
        List<Schedule> schedulesToCreateToday = scheduleRepository
                .findByDayOfMonthAndIsFixedValueIsFalseAndUserId(today, userService.getLoggedUserId()).get();
        return schedulesToCreateToday;
    }

    private void insertTransactionsScheduled(List<Schedule> schedulesForToday) {
        schedulesForToday.forEach(schedule -> {
            Transaction transactionToInsert = Transaction.builder()
                    .category(schedule.getBaseTransaction().getCategory())
                    .categoryId(schedule.getBaseTransaction().getCategoryId())
                    .description(Consts.SCHEDULED_TRANSACTION_TAG + schedule.getBaseTransaction().getDescription())
                    .amount(schedule.getBaseTransaction().getAmount())
                    .date(LocalDateTime.now())
                    .userId(schedule.getBaseTransaction().getUserId())
                    .id(null)
                    .build();

            transactionService.insertTransaction(transactionToInsert);
        });
    }

}