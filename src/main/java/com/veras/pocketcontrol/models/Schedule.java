package com.veras.pocketcontrol.models;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document
public class Schedule {

    @Id
    private String id;

    private String description;

    private Integer dayOfMonth;

    private String transactionId;

    private Transaction baseTransaction;

    private String userId;
}
