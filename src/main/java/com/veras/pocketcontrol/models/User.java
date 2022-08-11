package com.veras.pocketcontrol.models;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Document
public class User {
    @Id
    private String id;
    private String userName;
    private String password;
    private String email;
    private LocalDateTime lastLogin;
}
