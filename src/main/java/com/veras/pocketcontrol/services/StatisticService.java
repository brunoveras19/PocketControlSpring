package com.veras.pocketcontrol.services;

import com.veras.pocketcontrol.models.CategoryMonthlyAvg;
import com.veras.pocketcontrol.models.Transaction;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.GroupOperation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.aggregation.ProjectionOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
@Slf4j
public class StatisticService {

    private MongoTemplate mongoTemplate;

    private final UserService userService;

    private final CategoryService categoryService;

    private static final String CATEGORY_ID_FIELD = "categoryId";
    private static final String USER_ID_FIELD = "userId";
    private static final String AMOUNT_FIELD = "amount";
    private static final String MONTH_FIELD = "month";


    public List<CategoryMonthlyAvg> listMonthlyAverageByCategories() {
        MatchOperation match = Aggregation.match(Criteria.where(USER_ID_FIELD).is(userService.getLoggedUserId()));
        ProjectionOperation project1 = Aggregation.project(CATEGORY_ID_FIELD, AMOUNT_FIELD)
                .andExpression("month(date)").as(MONTH_FIELD);
        GroupOperation groupSum = Aggregation.group(CATEGORY_ID_FIELD, MONTH_FIELD).sum(AMOUNT_FIELD).as("sum");
        GroupOperation groupAvg = Aggregation.group(CATEGORY_ID_FIELD).avg("sum").as("avg");
        ProjectionOperation project2 = Aggregation.project("avg").and(CATEGORY_ID_FIELD).previousOperation();
        Aggregation aggregation = Aggregation.newAggregation(match, project1, groupSum, groupAvg, project2);
        List<CategoryMonthlyAvg> results = mongoTemplate.aggregate(aggregation, mongoTemplate.getCollectionName(Transaction.class), CategoryMonthlyAvg.class).getMappedResults();
        log.info(results.toString());
        for (CategoryMonthlyAvg avg : results){
            avg.setCategory(categoryService.getCategory(avg.getCategoryId()).get());
        }
        return results;
    }
}
