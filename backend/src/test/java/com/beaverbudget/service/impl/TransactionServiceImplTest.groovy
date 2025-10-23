package com.beaverbudget.service.impl

import com.beaverbudget.exceptions.InvalidResourceException
import com.beaverbudget.exceptions.ResourceNotFoundException
import com.beaverbudget.model.Transaction
import com.beaverbudget.persistence.AccountPersistenceService
import com.beaverbudget.persistence.TransactionPersistenceService
import spock.lang.Specification
import spock.lang.Subject

import java.time.LocalDateTime

class TransactionServiceImplTest extends Specification {
    def transactionPersistenceService = Mock(TransactionPersistenceService)
    def accountPersistenceService = Mock(AccountPersistenceService)

    @Subject

    TransactionServiceImpl service = new TransactionServiceImpl(
            transactionPersistenceService,
            accountPersistenceService
    )

    def "should create transaction"(){
        given:
        def transaction = new Transaction()

        when:
        service.createTransaction(transaction)

        then:
        1 * transactionPersistenceService.saveTransaction(transaction)
    }

    def "should find transaction by id"() {
        given:
        def transaction = new Transaction(id: 1)
        transactionPersistenceService.findTransactionById(1) >> Optional.of(transaction)

        when:
        def result = service.findTransactionById(1)

        then:
        result == transaction
    }

    def "should throw ResourceNotFoundException when transaction not found"() {
        given:
        transactionPersistenceService.findTransactionById(99) >> Optional.empty()

        when:
        service.findTransactionById(99)

        then:
        def e = thrown(ResourceNotFoundException)
        e.message == "Transaction with id 99 not found"
    }

    def "should find transactions in period"() {
        given:
        def from = LocalDateTime.now().minusDays(10)
        def to = LocalDateTime.now()
        def transactions = [new Transaction(id: 1), new Transaction(id: 2)]
        transactionPersistenceService.findAllTransactions(from, to) >> transactions

        when:
        def result = service.findTransactions(from, to)

        then:
        result == transactions
    }

    def "should find account transactions in period"() {
        given:
        def from = LocalDateTime.now().minusDays(5)
        def to = LocalDateTime.now()
        def transactions = [new Transaction(id: 1)]
        transactionPersistenceService.findAllAccountTransactions(10, from, to) >> transactions

        when:
        def result = service.findAccountTransactions(10, from, to)

        then:
        result == transactions
    }

    def "should delete transaction by id"() {
        when:
        service.deleteTransaction(5)

        then:
        1 * transactionPersistenceService.deleteTransaction(5)
    }

    def "should update transaction successfully"() {
        given:
        def existing = new Transaction(id: 1, amount: 100G, description: "Old", planned: true, accountId: 2)
        def update = new Transaction(amount: 200G, description: "New", planned: false)
        transactionPersistenceService.findTransactionById(1) >> Optional.of(existing)
        transactionPersistenceService.saveTransaction(_) >> { Transaction t -> t }

        when:
        def result = service.updateTransaction(1, update)

        then:
        result.amount == 200G
        result.description == "New"
        result.planned == false
    }

    def "should throw ResourceNotFoundException when updating non-existing transaction"() {
        given:
        transactionPersistenceService.findTransactionById(1) >> Optional.empty()

        when:
        service.updateTransaction(1, new Transaction())

        then:
        thrown(ResourceNotFoundException)
    }

    def "should set parent transaction when valid parent id provided"() {
        given:
        def existing = new Transaction(id: 1)
        def parent = new Transaction(id: 2)
        def update = new Transaction(parentTransaction: new Transaction(id: 2))

        transactionPersistenceService.findTransactionById(1) >> Optional.of(existing)
        transactionPersistenceService.findTransactionById(2) >> Optional.of(parent)
        transactionPersistenceService.saveTransaction(_) >> { Transaction t -> t }

        when:
        def result = service.updateTransaction(1, update)

        then:
        result.parentTransaction == parent
    }

    def "should throw InvalidResourceException when parent transaction id is null"() {
        given:
        def existing = new Transaction(id: 1)
        def update = new Transaction(parentTransaction: new Transaction())

        transactionPersistenceService.findTransactionById(1) >> Optional.of(existing)

        when:
        service.updateTransaction(1, update)

        then:
        thrown(InvalidResourceException)
    }

    def "should throw ResourceNotFoundException when parent transaction not found"() {
        given:
        def existing = new Transaction(id: 1)
        def update = new Transaction(parentTransaction: new Transaction(id: 2))

        transactionPersistenceService.findTransactionById(1) >> Optional.of(existing)
        transactionPersistenceService.findTransactionById(2) >> Optional.empty()

        when:
        service.updateTransaction(1, update)

        then:
        thrown(ResourceNotFoundException)
    }

    def "should set account id when valid account exists"() {
        given:
        def existing = new Transaction(id: 1)
        def update = new Transaction(accountId: 5)
        transactionPersistenceService.findTransactionById(1) >> Optional.of(existing)
        accountPersistenceService.findAccountById(5) >> Optional.of(new Object())
        transactionPersistenceService.saveTransaction(_) >> { Transaction t -> t }

        when:
        def result = service.updateTransaction(1, update)

        then:
        result.accountId == 5
    }

    def "should throw ResourceNotFoundException when account not found during update"() {
        given:
        def existing = new Transaction(id: 1)
        def update = new Transaction(accountId: 10)
        transactionPersistenceService.findTransactionById(1) >> Optional.of(existing)
        accountPersistenceService.findAccountById(10) >> Optional.empty()

        when:
        service.updateTransaction(1, update)

        then:
        thrown(ResourceNotFoundException)
    }

    def "should execute planned transaction successfully"() {
        given:
        def parent = new Transaction(id: 1, planned: true)
        def newTx = new Transaction(amount: 100G)
        transactionPersistenceService.findTransactionById(1) >> Optional.of(parent)
        transactionPersistenceService.saveTransaction(_) >> { Transaction t -> t }

        when:
        def result = service.executePlannedTransaction(1, newTx)

        then:
        result.parentTransaction == parent
        !result.planned
    }

    def "should throw ResourceNotFoundException when planned transaction not found"() {
        given:
        transactionPersistenceService.findTransactionById(1) >> Optional.empty()

        when:
        service.executePlannedTransaction(1, new Transaction())

        then:
        thrown(ResourceNotFoundException)
    }

    def "should throw InvalidResourceException when executing non-planned transaction"() {
        given:
        def parent = new Transaction(id: 1, planned: false)
        transactionPersistenceService.findTransactionById(1) >> Optional.of(parent)

        when:
        service.executePlannedTransaction(1, new Transaction())

        then:
        thrown(InvalidResourceException)
    }
}
