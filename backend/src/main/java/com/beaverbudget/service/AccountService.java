package com.beaverbudget.service;

import com.beaverbudget.exceptions.FeatureNotImplementedException;
import com.beaverbudget.model.Account;
import com.beaverbudget.model.Transaction;

import java.time.LocalDate;
import java.util.List;

public interface AccountService {

    default Account createAccount(Account account){
        throw new FeatureNotImplementedException("AccountService.createAccount not implemented");
    }

    default Account findAccountById(Integer accountId){
        throw new FeatureNotImplementedException("AccountService.findAccountById not implemented");
    }

    default List<Account> findAllAccounts(){
        throw new FeatureNotImplementedException("AccountService.findAllAccounts not implemented");
    }

    default Account updateAccount(Integer accountId, Account account){
        throw new FeatureNotImplementedException("AccountService.updateAccount not implemented");
    }

    default void deleteAccount(Integer accountId){
        throw new FeatureNotImplementedException("AccountService.deleteAccount not implemented");
    }

    default Transaction createAccountTransaction(Integer accountId, Transaction transaction){
        throw new FeatureNotImplementedException("AccountService.createAccountTransaction not implemented");
    }

    default Transaction findAccountTransactionById(Integer accountId, Integer transactionId){
        throw new FeatureNotImplementedException("AccountService.findAccountTransactionById not implemented");
    }

    default List<Transaction> findAccountTransactions(Integer accountId, LocalDate from, LocalDate to){
        throw new FeatureNotImplementedException("AccountService.findAccountTransactions not implemented");
    }

    default Transaction updateAccountTransaction(Integer accountId, Integer transactionId, Transaction transaction){
        throw new FeatureNotImplementedException("AccountService.updateAccountTransaction not implemented");
    }

    default void deleteAccountTransaction(Integer accountId, Integer transactionId){
        throw new FeatureNotImplementedException("AccountService.deleteAccountTransaction not implemented");
    }

    default Transaction executePlannedTransaction(Integer accountId, Integer plannedTransactionId, Transaction plannedTransaction){
        throw new FeatureNotImplementedException("AccountService.executePlannedTransaction not implemented");
    }

}
