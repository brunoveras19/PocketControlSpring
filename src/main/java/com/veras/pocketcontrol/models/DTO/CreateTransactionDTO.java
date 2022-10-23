package com.veras.pocketcontrol.models.DTO;

import com.veras.pocketcontrol.models.Transaction;
import lombok.Data;

@Data
public class CreateTransactionDTO {

    private Transaction transaction;

    private Boolean isSchedule;

    private Boolean isFixedValue;
}
