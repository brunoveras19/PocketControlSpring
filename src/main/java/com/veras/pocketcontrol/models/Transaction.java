package com.veras.pocketcontrol.models;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Document
public class Transaction {
    @Id
    private String id;

    private Double amount;

    private LocalDateTime date;

    private String description;

    private String categoryId;

    private Category category;

    private String userId;


}
