package com.beaverbudget.persistence;

import com.beaverbudget.exceptions.FeatureNotImplementedException;
import com.beaverbudget.model.Account;

import java.util.List;
import java.util.Optional;

public interface AccountPersistenceService {

    default Optional<Account> findAccountById(Integer accountId){
        throw new FeatureNotImplementedException("AccountPersistenceService.findAccountById not implemented");
    }

    default List<Account> findAllAccounts(){
        throw new FeatureNotImplementedException("AccountPersistenceService.findAllAccount not implemented");
    }

    default Account saveAccount(Account account){
        throw new FeatureNotImplementedException("AccountPersistenceService.saveAccount not implemented");
    }

    default void deleteAccount(Integer accountId){
        throw new FeatureNotImplementedException("AccountPersistenceService.deleteAccount not implemented");
    }
}
