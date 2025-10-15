package com.beaverbudget.controller;

import com.beaverbudget.api.AccountsApi;
import com.beaverbudget.mapper.MapperProvider;
import com.beaverbudget.model.Account;
import com.beaverbudget.model.AccountDTO;
import com.beaverbudget.model.Transaction;
import com.beaverbudget.model.TransactionDTO;

import com.beaverbudget.service.AccountService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Controller for account resource requests
 */
@RestController
public class AccountController implements AccountsApi {

    private final AccountService accountService;
    private final MapperProvider mapperProvider;

    /**
     * Class constructor
     * @param accountService {@link AccountService}
     * @param mapperProvider {@link MapperProvider}
     */
    public AccountController(AccountService accountService, MapperProvider mapperProvider) {
        this.accountService = accountService;
        this.mapperProvider = mapperProvider;
    }


    @Override
    public ResponseEntity<AccountDTO> createAccount(AccountDTO accountDTO) {
        Account savedAccount = accountService.createAccount(mapperProvider.map(accountDTO, Account.class));
        return ResponseEntity.ok(mapperProvider.map(savedAccount, AccountDTO.class));
    }

    @Override
    public ResponseEntity<AccountDTO> getAccountById(Integer accountId) {
        Account account = accountService.findAccountById(accountId);
        return ResponseEntity.ok(mapperProvider.map(account, AccountDTO.class));
    }

    @Override
    public ResponseEntity<List<AccountDTO>> listAccounts() {
        List<AccountDTO> accountDTOList = accountService.findAllAccounts().stream()
                .map(account -> mapperProvider.map(account, AccountDTO.class))
                .toList();
        return ResponseEntity.ok(accountDTOList);
    }

    @Override
    public ResponseEntity<AccountDTO> updateAccount(Integer accountId, AccountDTO accountDTO) {
        Account updateAccount = accountService.updateAccount(accountId, mapperProvider.map(accountDTO, Account.class));
        return ResponseEntity.ok(mapperProvider.map(updateAccount, AccountDTO.class));
    }

    @Override
    public ResponseEntity<Void> deleteAccount(Integer accountId) {
        accountService.deleteAccount(accountId);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<TransactionDTO> createAccountTransaction(Integer accountId, TransactionDTO transactionDTO) {
        Transaction transaction = mapperProvider.map(transactionDTO, Transaction.class);
        Transaction accountTransaction = accountService.createAccountTransaction(accountId, transaction);
        return ResponseEntity.ok(mapperProvider.map(accountTransaction, TransactionDTO.class));
    }

    @Override
    public ResponseEntity<TransactionDTO> getAccountTransactionById(Integer accountId, Integer transactionId) {
        Transaction accountTransaction = accountService.findAccountTransactionById(accountId, transactionId);
        return ResponseEntity.ok(mapperProvider.map(accountTransaction, TransactionDTO.class));
    }

    @Override
    public ResponseEntity<TransactionDTO> updateAccountTransaction(Integer accountId, Integer transactionId, TransactionDTO transactionDTO) {
        Transaction transaction = accountService.updateAccountTransaction(accountId, transactionId, mapperProvider.map(transactionDTO, Transaction.class));
        return ResponseEntity.ok(mapperProvider.map(transaction, TransactionDTO.class));
    }

    @Override
    public ResponseEntity<Void> deleteAccountTransaction(Integer accountId, Integer transactionId) {
        accountService.deleteAccountTransaction(accountId, transactionId);
        return ResponseEntity.noContent().build();
    }
}
