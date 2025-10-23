package com.beaverbudget.controller;

import com.beaverbudget.api.TransactionsApi;
import com.beaverbudget.mapper.MapperProvider;
import com.beaverbudget.model.Transaction;
import com.beaverbudget.model.TransactionDTO;
import com.beaverbudget.service.TransactionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Objects;

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
    public ResponseEntity<List<TransactionDTO>> listTransactions(OffsetDateTime from, OffsetDateTime to) {
        LocalDateTime fromLocalDateTime = Objects.nonNull(from) ? from.toLocalDateTime() : null;
        LocalDateTime toLocalDateTime = Objects.nonNull(to) ? from.toLocalDateTime() : null;
        List<TransactionDTO> transactionDTOList = transactionService.findTransactions(fromLocalDateTime, toLocalDateTime).stream()
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
