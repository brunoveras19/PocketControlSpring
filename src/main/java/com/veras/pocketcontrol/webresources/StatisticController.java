package com.veras.pocketcontrol.webresources;

import com.veras.pocketcontrol.models.CategoryMonthlyAvg;
import com.veras.pocketcontrol.services.StatisticService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/statistics")
@AllArgsConstructor
public class StatisticController {

    private final StatisticService statisticService;

    @GetMapping("/categories-monthly-avg")
    @ApiOperation(value = "Recuperar a média de gastos mensal por categoria", authorizations = { @Authorization(value="jwtToken") })
    public ResponseEntity<List<CategoryMonthlyAvg>> listMonthlyAvgCategories(@RequestParam(required = false) String id) {
        List<CategoryMonthlyAvg> avgs = statisticService.listMonthlyAverageByCategories();
        return ResponseEntity.ok(avgs);

    }
}
