package com.beaverbudget.controller;

import com.beaverbudget.api.TransactionsApi;
import com.beaverbudget.mapper.MapperProvider;
import com.beaverbudget.model.Transaction;
import com.beaverbudget.model.TransactionDTO;
import com.beaverbudget.service.TransactionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
public class TransactionController implements TransactionsApi {

    private final TransactionService transactionService;
    private final MapperProvider mapperProvider;

    public TransactionController(TransactionService transactionService, MapperProvider mapperProvider) {
        this.transactionService = transactionService;
        this.mapperProvider = mapperProvider;
    }

    @Override
    public ResponseEntity<TransactionDTO> getTransactionById(Integer transactionId) {
        Transaction transaction = transactionService.findTransactionById(transactionId);
        return ResponseEntity.ok(mapperProvider.map(transaction, TransactionDTO.class));
    }

    @Override
    public ResponseEntity<TransactionDTO> updateTransaction(Integer transactionId, TransactionDTO transactionDTO) {
        Transaction updatedTransaction = transactionService.updateTransaction(transactionId, transactionService.findTransactionById(transactionId));
        return ResponseEntity.ok(mapperProvider.map(updatedTransaction, TransactionDTO.class));
    }

    @Override
    public ResponseEntity<List<TransactionDTO>> listTransactions(LocalDate from, LocalDate to) {

        List<TransactionDTO> transactionDTOList = transactionService.findTransactions(from, to).stream()
                .map(transaction -> mapperProvider.map(transaction, TransactionDTO.class))
                .toList();
        return ResponseEntity.ok(transactionDTOList);
    }


    @Override
    public ResponseEntity<Void> deleteTransaction(Integer transactionId) {
        transactionService.deleteTransaction(transactionId);
        return ResponseEntity.noContent().build();
    }
}
