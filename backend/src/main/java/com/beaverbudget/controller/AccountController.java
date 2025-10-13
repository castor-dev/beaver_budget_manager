package com.beaverbudget.controller;

import com.beaverbudget.api.AccountsApi;
import com.beaverbudget.mapper.MapperProvider;
import com.beaverbudget.model.AccountDTO;
import com.beaverbudget.model.TransactionDTO;
import com.beaverbudget.service.AccountService;
import com.beaverbudget.service.TransactionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class AccountController implements AccountsApi {

    private final AccountService accountService;
    private final TransactionService transactionService;
    private final MapperProvider mapperProvider;

    public AccountController(AccountService accountService, TransactionService transactionService, MapperProvider mapperProvider) {
        this.accountService = accountService;
        this.transactionService = transactionService;
        this.mapperProvider = mapperProvider;
    }


    @Override
    public ResponseEntity<AccountDTO> createAccount(AccountDTO accountDTO) {
        return AccountsApi.super.createAccount(accountDTO);
    }

    @Override
    public ResponseEntity<AccountDTO> getAccountById(Integer accountId) {
        return AccountsApi.super.getAccountById(accountId);
    }

    @Override
    public ResponseEntity<List<AccountDTO>> listAccounts() {
        return AccountsApi.super.listAccounts();
    }

    @Override
    public ResponseEntity<AccountDTO> updateAccount(Integer accountId, AccountDTO accountDTO) {
        return AccountsApi.super.updateAccount(accountId, accountDTO);
    }

    @Override
    public ResponseEntity<Void> deleteAccount(Integer accountId) {
        return AccountsApi.super.deleteAccount(accountId);
    }

    @Override
    public ResponseEntity<TransactionDTO> createAccountTransaction(Integer accountId, TransactionDTO transactionDTO) {
        return AccountsApi.super.createAccountTransaction(accountId, transactionDTO);
    }

    @Override
    public ResponseEntity<TransactionDTO> getAccountTransactionById(Integer accountId, Integer transactionId) {
        return AccountsApi.super.getAccountTransactionById(accountId, transactionId);
    }

    @Override
    public ResponseEntity<TransactionDTO> updateAccountTransaction(Integer accountId, Integer transactionId, TransactionDTO transactionDTO) {
        return AccountsApi.super.updateAccountTransaction(accountId, transactionId, transactionDTO);
    }

    @Override
    public ResponseEntity<Void> deleteAccountTransaction(Integer accountId, Integer transactionId) {
        return AccountsApi.super.deleteAccountTransaction(accountId, transactionId);
    }
}
