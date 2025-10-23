package com.beaverbudget.service.impl;

import com.beaverbudget.exceptions.InvalidResourceException;
import com.beaverbudget.exceptions.ResourceNotFoundException;
import com.beaverbudget.model.Transaction;
import com.beaverbudget.persistence.AccountPersistenceService;
import com.beaverbudget.persistence.TransactionPersistenceService;
import com.beaverbudget.service.TransactionService;
import lombok.AllArgsConstructor;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

@Service
@AllArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final TransactionPersistenceService transactionPersistenceService;
    private final AccountPersistenceService accountPersistenceService;

    @Override
    public Transaction createTransaction(Transaction transaction) {
        return transactionPersistenceService.saveTransaction(transaction);
    }

    @Override
    public Transaction findTransactionById(Integer transactionId) {
        return transactionPersistenceService.findTransactionById(transactionId)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Transaction with id %s not found", transactionId)));
    }

    @Override
    public List<Transaction> findTransactions(LocalDateTime from, LocalDateTime to) {
        return transactionPersistenceService.findAllTransactions(from, to);
    }

    @Override
    public List<Transaction> findAccountTransactions(Integer accountId, LocalDateTime from, LocalDateTime to) {
        return transactionPersistenceService.findAllAccountTransactions(accountId, from, to);
    }

    @Override
    public void deleteTransaction(Integer transactionId) {
        transactionPersistenceService.deleteTransaction(transactionId);
    }

    @Override
    public Transaction updateTransaction(Integer transactionId, Transaction transaction) {

        Transaction existingTransaction = transactionPersistenceService.findTransactionById(transactionId)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Transaction %s not found ", transactionId)));

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
            Transaction parentTransaction = transactionPersistenceService.findTransactionById(parentTransactionId)
                    .orElseThrow(() -> new ResourceNotFoundException(String.format("Transaction %s not found", parentTransactionId)));
            existingTransaction.setParentTransaction(parentTransaction);
        }
        if (nonNull(transaction.getAccountId())) {
            Integer accountId = transaction.getAccountId();
            accountPersistenceService.findAccountById(accountId)
                    .orElseThrow(() -> new ResourceNotFoundException(String.format("Account %s not found", accountId)));
            existingTransaction.setAccountId(accountId);
        }

        return transactionPersistenceService.saveTransaction(existingTransaction);
    }

    @Override
    public Transaction executePlannedTransaction(Integer plannedTransactionId, Transaction transaction) {
        Transaction parentTransaction = transactionPersistenceService.findTransactionById(plannedTransactionId)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Transaction %s not found ", plannedTransactionId)));

        if (!parentTransaction.getPlanned()) {
            throw new InvalidResourceException("Not a planned transaction");
        }

        transaction.setParentTransaction(parentTransaction);
        transaction.setPlanned(false);

        return transactionPersistenceService.saveTransaction(transaction);
    }


}
