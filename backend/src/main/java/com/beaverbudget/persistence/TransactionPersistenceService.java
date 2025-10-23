package com.beaverbudget.persistence;

import com.beaverbudget.exceptions.FeatureNotImplementedException;
import com.beaverbudget.model.Transaction;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface TransactionPersistenceService {


    default Optional<Transaction> findTransactionById( Integer transactionId){
        throw new FeatureNotImplementedException("TransactionPersistenceService.createAccountTransaction not implemented");
    }

    default List<Transaction> findAllAccountTransactions(Integer accountId, LocalDateTime from, LocalDateTime to){
        throw new FeatureNotImplementedException("TransactionPersistenceService.findAllAccountTransactions not implemented");
    }

    default Transaction saveTransaction(Transaction transaction){
        throw new FeatureNotImplementedException("TransactionPersistenceService.saveTransaction not implemented");
    }

    default void deleteTransaction(Integer transactionId){
        throw new FeatureNotImplementedException("TransactionPersistenceService.deleteTransaction not implemented");
    }

    default List<Transaction> findAllTransactions(LocalDateTime from, LocalDateTime to){
        throw new FeatureNotImplementedException("TransactionPersistenceService.findAllTransactions not implemented");
    }
}
