package com.beaverbudget.persistence;

import com.beaverbudget.exceptions.FeatureNotImplementedException;
import com.beaverbudget.model.Transaction;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface TransactionPersistenceService {

    default Transaction createAccountTransaction(Integer accountId, Transaction transaction){
        throw new FeatureNotImplementedException("TransactionPersistenceService.createAccountTransaction not implemented");
    }

    default Optional<Transaction> findAccountTransactionById(Integer accountId, Integer transactionId){
        throw new FeatureNotImplementedException("TransactionPersistenceService.createAccountTransaction not implemented");
    }

    default List<Transaction> findAllAccountTransactions(Integer accountId, LocalDate from, LocalDate to){
        throw new FeatureNotImplementedException("TransactionPersistenceService.findAllAccountTransactions not implemented");
    }

    default Transaction saveTransaction(Transaction transaction){
        throw new FeatureNotImplementedException("TransactionPersistenceService.saveTransaction not implemented");
    }

    default void deleteTransaction(Integer accountId, Integer transactionId){
        throw new FeatureNotImplementedException("TransactionPersistenceService.deleteTransaction not implemented");
    }

    default Optional<Transaction> findTransactionById(Integer transactionId){
        throw new FeatureNotImplementedException("TransactionPersistenceService.findTransactionById not implemented");
    }

    default List<Transaction> findAllTransactions(LocalDate from, LocalDate to){
        throw new FeatureNotImplementedException("TransactionPersistenceService.findAllTransactions not implemented");
    }

    default Transaction updateTransaction(Integer transactionId, Transaction transaction){
        throw new FeatureNotImplementedException("TransactionPersistenceService.updateTransaction not implemented");
    }

    default void deleteTransaction(Integer transactionId){
        throw new FeatureNotImplementedException("TransactionPersistenceService.deleteTransaction not implemented");
    }

}
