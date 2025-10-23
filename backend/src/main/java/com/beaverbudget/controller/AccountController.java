package com.beaverbudget.controller;

import com.beaverbudget.api.AccountsApi;
import com.beaverbudget.mapper.MapperProvider;
import com.beaverbudget.model.Account;
import com.beaverbudget.model.AccountDTO;
import com.beaverbudget.model.Transaction;
import com.beaverbudget.model.TransactionDTO;

import com.beaverbudget.service.AccountService;
import com.beaverbudget.service.TransactionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Objects;

/**
 * Controller for account resource requests
 */
@RestController
public class AccountController implements AccountsApi {

    private final AccountService accountService;
    private final TransactionService transactionService;
    private final MapperProvider mapperProvider;

    /**
     * Class constructor
     * @param accountService {@link AccountService}
     * @param mapperProvider {@link MapperProvider}
     */
    public AccountController(AccountService accountService, TransactionService transactionService, MapperProvider mapperProvider) {
        this.accountService = accountService;
        this.transactionService = transactionService;
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
        Transaction accountTransaction = transactionService.createTransaction(transaction);
        return ResponseEntity.ok(mapperProvider.map(accountTransaction, TransactionDTO.class));
    }

    @Override
    public ResponseEntity<TransactionDTO> getAccountTransactionById(Integer accountId, Integer transactionId) {
        Transaction accountTransaction = transactionService.findTransactionById(transactionId);
        return ResponseEntity.ok(mapperProvider.map(accountTransaction, TransactionDTO.class));
    }

    @Override
    public ResponseEntity<TransactionDTO> updateAccountTransaction(Integer accountId,
                                                                   Integer transactionId,
                                                                   TransactionDTO transactionDTO) {
        Transaction transaction = transactionService.updateTransaction(transactionId, mapperProvider.map(transactionDTO, Transaction.class));
        return ResponseEntity.ok(mapperProvider.map(transaction, TransactionDTO.class));
    }

    @Override
    public ResponseEntity<Void> deleteAccountTransaction(Integer accountId,
                                                         Integer transactionId) {
        transactionService.deleteTransaction(transactionId);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<List<TransactionDTO>> listAccountTransactions(Integer accountId,
                                                                        OffsetDateTime from,
                                                                        OffsetDateTime to) {
        LocalDateTime fromLocalDateTime = Objects.nonNull(from) ? from.toLocalDateTime() : null;
        LocalDateTime toLocalDateTime = Objects.nonNull(to) ? from.toLocalDateTime() : null;
        return ResponseEntity.ok(transactionService.findAccountTransactions(accountId, fromLocalDateTime, toLocalDateTime).stream()
                .map(transaction ->  mapperProvider.map(transaction, TransactionDTO.class))
                .toList());
    }

    @Override
    public ResponseEntity<TransactionDTO> executePlannedAccountTransaction(Integer accountId,
                                                                           Integer transactionId,
                                                                           TransactionDTO transactionDTO) {
        Transaction transaction = mapperProvider.map(transactionDTO, Transaction.class);
        Transaction plannedTransaction = transactionService.executePlannedTransaction(transactionId, transaction);
        return ResponseEntity.ok(mapperProvider.map(plannedTransaction, TransactionDTO.class));
    }
}
