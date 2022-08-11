package com.veras.pocketcontrol.webresources;

import com.veras.pocketcontrol.models.Transaction;
import com.veras.pocketcontrol.services.TransactionService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/transactions")
@AllArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @GetMapping
    @ApiOperation(value = "Recuperar transações do usuário", authorizations = { @Authorization(value="jwtToken") })
    public ResponseEntity<List<Transaction>> fetchCategory(@RequestParam(required = false) String id) {
        Optional<List<Transaction>> transactions = id == null ? transactionService.getAllTransactions() : Optional.of(transactionService.getTransaction(id).stream().toList());
        if(transactions.isPresent()){
            return ResponseEntity.of(transactions);
        } else {
            return ResponseEntity.of(Optional.of(Collections.emptyList()));
        }
    }

    @GetMapping("balance")
    @ApiOperation(value = "Recuperar saldo das transações do usuário", authorizations = { @Authorization(value="jwtToken") })
    public ResponseEntity<Double> fetchBalance() {
        Double balance =  transactionService.getBalance();
        return ResponseEntity.ok(balance);
    }

    @PostMapping
    @ApiOperation(value = "Inserir transação", authorizations = { @Authorization(value="jwtToken") })
    public Transaction insertTransaction(@RequestBody Transaction transaction){
        return transactionService.insertTransaction(transaction);
    }

    @PutMapping
    @ApiOperation(value = "Atualizar Transação", authorizations = { @Authorization(value="jwtToken") })
    public Transaction updateTransaction(@RequestBody Transaction transaction){
        return transactionService.updateTransaction(transaction);
    }

    @DeleteMapping
    @ApiOperation(value = "Deletar Transação", authorizations = { @Authorization(value="jwtToken") })
    public Transaction deleteTransaction(@RequestParam String id) {
        return transactionService.deleteTransaction(id);
    }
}
