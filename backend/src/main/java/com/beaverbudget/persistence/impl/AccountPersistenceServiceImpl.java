package com.beaverbudget.persistence.impl;

import com.beaverbudget.mapper.MapperProvider;
import com.beaverbudget.model.Account;
import com.beaverbudget.persistence.AccountPersistenceService;
import com.beaverbudget.persistence.repository.AccountRepository;
import com.beaverbudget.persistence.repository.entity.AccountEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AccountPersistenceServiceImpl implements AccountPersistenceService {

    private final MapperProvider mapperProvider;
    private final AccountRepository accountRepository;

    public AccountPersistenceServiceImpl(MapperProvider mapperProvider, AccountRepository accountRepository) {
        this.mapperProvider = mapperProvider;
        this.accountRepository = accountRepository;
    }

    @Override
    public Optional<Account> findAccountById(Integer accountId) {
        AccountEntity accountEntity = accountRepository.findById(accountId).orElse(null);
        return Optional.ofNullable(mapperProvider.map(accountEntity, Account.class));
    }

    @Override
    public List<Account> findAllAccounts() {
        return accountRepository.findAll().stream()
                .map(accountEntity -> mapperProvider.map(accountEntity, Account.class))
                .toList();
    }


    @Override
    public Account saveAccount(Account account) {
        AccountEntity accountEntity = mapperProvider.map(account, AccountEntity.class);
        AccountEntity savedAccountEntity = accountRepository.save(accountEntity);
        return mapperProvider.map(savedAccountEntity, Account.class);
    }

    @Override
    public void deleteAccount(Integer accountId) {
        accountRepository.deleteById(accountId);
    }
}
