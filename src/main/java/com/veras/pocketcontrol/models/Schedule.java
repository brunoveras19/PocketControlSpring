package com.veras.pocketcontrol.models;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document
@Builder
public class Schedule {

    @Id
    private String id;

    private String description;

    private Integer dayOfMonth;

    private Transaction baseTransaction;

    private String userId;

    private Boolean isFixedValue;
}

