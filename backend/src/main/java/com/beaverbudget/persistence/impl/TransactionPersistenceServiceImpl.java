package com.beaverbudget.persistence.impl;

import com.beaverbudget.mapper.MapperProvider;
import com.beaverbudget.model.Transaction;
import com.beaverbudget.persistence.TransactionPersistenceService;
import com.beaverbudget.persistence.repository.TransactionRepository;
import com.beaverbudget.persistence.repository.entity.TransactionEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class TransactionPersistenceServiceImpl implements TransactionPersistenceService {

    private final TransactionRepository repository;
    private final MapperProvider mapperProvider;

    public TransactionPersistenceServiceImpl(TransactionRepository repository, MapperProvider mapperProvider) {
        this.repository = repository;
        this.mapperProvider = mapperProvider;
    }

    @Override
    public Transaction saveTransaction(Transaction transaction) {
        TransactionEntity transactionEntity = mapperProvider.map(transaction, TransactionEntity.class);
        TransactionEntity savedTransaction = repository.save(transactionEntity);
        return mapperProvider.map(savedTransaction, Transaction.class);
    }

    @Override
    public Optional<Transaction> findTransactionById(Integer transactionId) {
        TransactionEntity transactionEntity = repository.findById(transactionId).orElse(null);
        return Optional.ofNullable(mapperProvider.map(transactionEntity, Transaction.class));
    }

    @Override
    public List<Transaction> findAllAccountTransactions(Integer accountId, LocalDateTime from, LocalDateTime to) {
        return repository.findAccountTransactions(accountId, from, to).stream()
                .map(transactionEntity -> mapperProvider.map(transactionEntity, Transaction.class))
                .toList();
    }

    @Override
    public List<Transaction> findAllTransactions(LocalDateTime from, LocalDateTime to) {
        return repository.findTransactions(from, to).stream()
                .map(transactionEntity -> mapperProvider.map(transactionEntity, Transaction.class))
                .toList();
    }

    @Override
    public void deleteTransaction(Integer transactionId) {
        repository.deleteById(transactionId);
    }
}
