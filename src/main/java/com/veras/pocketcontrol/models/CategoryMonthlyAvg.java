package com.veras.pocketcontrol.models;

import lombok.Data;

@Data
public class CategoryMonthlyAvg {
    private String categoryId;
    private Double avg;
    private Category category;
}
