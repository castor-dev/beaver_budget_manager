package com.beaverbudget.service.impl;

import com.beaverbudget.exceptions.InvalidResourceException;
import com.beaverbudget.exceptions.ResourceNotFoundException;
import com.beaverbudget.model.Account;
import com.beaverbudget.model.Transaction;
import com.beaverbudget.persistence.AccountPersistenceService;
import com.beaverbudget.persistence.TransactionPersistenceService;
import com.beaverbudget.persistence.UserPersistenceService;
import com.beaverbudget.service.AccountService;
import lombok.AllArgsConstructor;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

@Service
@AllArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountPersistenceService accountPersistenceService;
    private final TransactionPersistenceService transactionPersistenceService;
    private final UserPersistenceService userPersistenceService;


    @Override
    public Account createAccount(Account account) {
        return accountPersistenceService.createAccount(account);
    }

    @Override
    public Account findAccountById(Integer accountId) {
        return accountPersistenceService.findAccountById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException("Account with id " + accountId + " not found"));
    }

    @Override
    public List<Account> findAllAccounts() {
        return accountPersistenceService.findAllAccounts();
    }

    @Override
    public Account updateAccount(Integer accountId, Account account) {
        Account existingAccount = accountPersistenceService.findAccountById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException("Account with id " + accountId + " not found"));

        if (nonNull(account.getName())) {
            existingAccount.setName(account.getName());
        }
        if (nonNull(account.getOwnerId())) {
            userPersistenceService.findUserById(account.getOwnerId())
                    .orElseThrow(() -> new ResourceNotFoundException("User with id " + account.getOwnerId() + " not found"));
            existingAccount.setOwnerId(account.getOwnerId());
        }
        if (nonNull(account.getBalance())) {
            existingAccount.setBalance(account.getBalance());
        }
        if (nonNull(account.getCurrency())) {
            existingAccount.setCurrency(account.getCurrency());
        }

        return accountPersistenceService.saveAccount(existingAccount);
    }

    @Override
    public void deleteAccount(Integer accountId) {
        accountPersistenceService.deleteAccount(accountId);
    }

    @Override
    public Transaction createAccountTransaction(Integer accountId, Transaction transaction) {
        return transactionPersistenceService.createAccountTransaction(accountId, transaction);
    }

    @Override
    public Transaction findAccountTransactionById(Integer accountId, Integer transactionId) {
        return transactionPersistenceService.findAccountTransactionById(accountId, transactionId)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Transaction %s not found for account %s", accountId, transactionId)));
    }

    @Override
    public List<Transaction> findAccountTransactions(Integer accountId, LocalDate from, LocalDate to) {
        return transactionPersistenceService.findAllAccountTransactions(accountId, from, to);
    }

    @Override
    public Transaction updateAccountTransaction(Integer accountId, Integer transactionId, Transaction transaction) {

        Transaction existingTransaction = transactionPersistenceService.findAccountTransactionById(accountId, transactionId)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Transaction %s not found for account %s", accountId, transactionId)));

        if (nonNull(transaction.getAmount())) {
            existingTransaction.setAmount(transaction.getAmount());
        }
        if (Strings.isNotBlank(transaction.getDescription())) {
            existingTransaction.setDescription(transaction.getDescription());
        }
        if (nonNull(transaction.getPlanned())) {
            existingTransaction.setPlanned(transaction.getPlanned());
        }
        if (nonNull(transaction.getParentTransaction())) {
            Integer parentTransactionId = transaction.getParentTransaction().getId();
            if (isNull(parentTransactionId)) {
                throw new InvalidResourceException("Invalid parent transaction");
            }
            Transaction parentTransaction = transactionPersistenceService.findAccountTransactionById(accountId, parentTransactionId)
                    .orElseThrow(() -> new ResourceNotFoundException(String.format("Transaction %s not found for account %s", accountId, parentTransactionId)));
            existingTransaction.setParentTransaction(parentTransaction);
        }

        return transactionPersistenceService.saveTransaction(existingTransaction);
    }

    @Override
    public void deleteAccountTransaction(Integer accountId, Integer transactionId) {
        transactionPersistenceService.deleteTransaction(accountId, transactionId);
    }

    @Override
    public Transaction executePlannedTransaction(Integer accountId, Integer plannedTransactionId, Transaction transaction) {
        Transaction parentTransaction = transactionPersistenceService.findAccountTransactionById(accountId, plannedTransactionId)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Transaction %s not found for account %s", accountId, plannedTransactionId)));

        if(!parentTransaction.getPlanned()){
            throw new InvalidResourceException("Not a planned transaction");
        }

        transaction.setParentTransaction(parentTransaction);
        transaction.setPlanned(false);

        return transactionPersistenceService.createAccountTransaction(accountId, transaction);
    }
}
