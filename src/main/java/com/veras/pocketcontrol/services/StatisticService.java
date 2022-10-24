package com.veras.pocketcontrol.services;

import com.veras.pocketcontrol.models.Category;
import com.veras.pocketcontrol.models.CategoryMonthlyAvg;
import com.veras.pocketcontrol.models.Transaction;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
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
    private static final String GET_MONTH = "month(date)";
    private static final String SUM = "sum";
    private static final String AVG = "avg";


    public List<CategoryMonthlyAvg> listMonthlyAverageByCategories() {
        MatchOperation match = Aggregation.match(Criteria.where(USER_ID_FIELD).is(userService.getLoggedUserId()));
        ProjectionOperation project1 = Aggregation.project(CATEGORY_ID_FIELD, AMOUNT_FIELD)
                .andExpression(GET_MONTH).as(MONTH_FIELD);
        GroupOperation groupSum = Aggregation.group(CATEGORY_ID_FIELD, MONTH_FIELD).sum(AMOUNT_FIELD).as(SUM);
        GroupOperation groupAvg = Aggregation.group(CATEGORY_ID_FIELD).avg(SUM).as(AVG);
        ProjectionOperation project2 = Aggregation.project(AVG).and(CATEGORY_ID_FIELD).previousOperation();
        Aggregation aggregation = Aggregation.newAggregation(match, project1, groupSum, groupAvg, project2);
        List<CategoryMonthlyAvg> results = mongoTemplate.aggregate(aggregation, mongoTemplate.getCollectionName(Transaction.class), CategoryMonthlyAvg.class).getMappedResults();

        for (CategoryMonthlyAvg avg : results){
            avg.setCategory(categoryService.getCategory(avg.getCategoryId()).get());
        }
        log.info(results.toString());
        return sortCategoryMonthlyAvg(results);
    }

    private List<CategoryMonthlyAvg> sortCategoryMonthlyAvg(List<CategoryMonthlyAvg> results) {
        List<CategoryMonthlyAvg> mutableList = new ArrayList<>(results);
        mutableList.sort((o1, o2) -> {
            if (o1 == o2) {
                return 0;
            }
            if (o1 != null) {
                return (o2 != null) ? o1.getCategory().getDescription().compareTo(o2.getCategory().getDescription()) : 1;
            }
            return -1;
        });
        return mutableList;
    }
}

