package com.beaverbudget.service;

import com.beaverbudget.exceptions.FeatureNotImplementedException;
import com.beaverbudget.model.Transaction;

import java.time.LocalDateTime;
import java.util.List;

public interface TransactionService {

    default Transaction createTransaction(Transaction transaction){
        throw new FeatureNotImplementedException("TransactionService.createTransaction not implemented");
    }

    default Transaction findTransactionById(Integer transactionId){
        throw new FeatureNotImplementedException("TransactionService.findTransactionById not implemented");
    }

    default List<Transaction> findTransactions(LocalDateTime from, LocalDateTime to){
        throw new FeatureNotImplementedException("TransactionService.findTransactions not implemented");
    }

    default List<Transaction> findAccountTransactions(Integer accountId, LocalDateTime from, LocalDateTime to){
        throw new FeatureNotImplementedException("TransactionService.findTransactions not implemented");
    }

    default Transaction updateTransaction(Integer transactionId, Transaction transaction){
        throw new FeatureNotImplementedException("TransactionService.updateTransaction not implemented");
    }

    default void deleteTransaction(Integer transactionId){
        throw new FeatureNotImplementedException("TransactionService.deleteTransaction not implemented");
    }

    default Transaction executePlannedTransaction(Integer plannedTransactionId, Transaction transaction){
        throw new FeatureNotImplementedException("AccountService.executePlannedTransaction not implemented");
    }
}
