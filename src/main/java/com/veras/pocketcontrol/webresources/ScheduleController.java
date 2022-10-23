package com.veras.pocketcontrol.webresources;

import com.veras.pocketcontrol.models.Schedule;
import com.veras.pocketcontrol.services.ScheduleService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/schedules")
@AllArgsConstructor
public class ScheduleController {

    private final ScheduleService scheduleService;

    @GetMapping
    @ApiOperation(value = "", authorizations = { @Authorization(value="jwtToken") })
    public ResponseEntity<List<Schedule>> fetchSchedules(@RequestParam(required = false) String id) {
        Optional<List<Schedule>> schedule = id == null ? scheduleService.getAllSchedules() : Optional.of(scheduleService.getSchedule(id).stream().toList());
        if(schedule.isPresent()){
            return ResponseEntity.of(schedule);
        } else {
            return ResponseEntity.of(Optional.of(Collections.emptyList()));
        }
    }

    @PostMapping
    @ApiOperation(value = "", authorizations = { @Authorization(value="jwtToken") })
    public Schedule insertSchedule(@RequestBody Schedule schedule){
        return scheduleService.insertSchedule(schedule);
    }

    @PutMapping
    @ApiOperation(value = "", authorizations = { @Authorization(value="jwtToken") })
    public Schedule updateSchedule(@RequestBody Schedule schedule){
        return scheduleService.updateSchedule(schedule);
    }

    @DeleteMapping
    @ApiOperation(value = "", authorizations = { @Authorization(value="jwtToken") })
    public Schedule deleteSchedule(@RequestParam String id) {
        return scheduleService.deleteSchedule(id);
    }

    @GetMapping("/to-notify")
    @ApiOperation(value = "", authorizations = { @Authorization(value="jwtToken") })
    public ResponseEntity<List<Schedule>> fetchAllSchedulesWithUnfixedAmountsForToday() {
        List<Schedule> schedules = scheduleService.getSchedulesToNotifyToday();
            return ResponseEntity.ok(schedules);
    }
}
