package com.beaverbudget.service;

import com.beaverbudget.exceptions.FeatureNotImplementedException;
import com.beaverbudget.model.Account;

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

}
