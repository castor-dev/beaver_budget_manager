package com.beaverbudget.service;

import com.beaverbudget.exceptions.FeatureNotImplementedException;
import com.beaverbudget.model.Transaction;

import java.time.LocalDate;
import java.util.List;

public interface TransactionService {

    default Transaction findTransactionById(Integer transactionId){
        throw new FeatureNotImplementedException("AccountService.findTransactionById not implemented");
    }

    default List<Transaction> findTransactions(LocalDate from, LocalDate to){
        throw new FeatureNotImplementedException("findTransactions.findTransactions not implemented");
    }

    default Transaction updateTransaction(Integer transactionId, Transaction transaction){
        throw new FeatureNotImplementedException("AccountService.updateTransaction not implemented");
    }

    default void deleteTransaction(Integer transactionId){
        throw new FeatureNotImplementedException("AccountService.deleteAccountTransaction not implemented");
    }
}
