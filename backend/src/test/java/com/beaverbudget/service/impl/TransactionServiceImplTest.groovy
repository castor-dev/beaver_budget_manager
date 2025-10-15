package com.beaverbudget.service.impl

import com.beaverbudget.exceptions.InvalidResourceException
import com.beaverbudget.exceptions.ResourceNotFoundException
import com.beaverbudget.model.Transaction
import com.beaverbudget.persistence.AccountPersistenceService
import com.beaverbudget.persistence.TransactionPersistenceService
import spock.lang.Specification
import spock.lang.Subject

import java.time.LocalDate

class TransactionServiceImplTest extends Specification {
    def transactionPersistenceService = Mock(TransactionPersistenceService)
    def accountPersistenceService = Mock(AccountPersistenceService)

    @Subject

    TransactionServiceImpl service = new TransactionServiceImpl(
            transactionPersistenceService,
            accountPersistenceService
    )

    // ------------------------------------------------------------
    // findTransactionById
    // ------------------------------------------------------------
    def "should return transaction when found"() {
        given:
        Integer id = 1
        def tx = new Transaction(id: id)
        transactionPersistenceService.findTransactionById(id) >> Optional.of(tx)

        when:
        def result = service.findTransactionById(id)

        then:
        result == tx
    }

    def "should throw ResourceNotFoundException when transaction not found"() {
        given:
        Integer id = 10
        transactionPersistenceService.findTransactionById(id) >> Optional.empty()

        when:
        service.findTransactionById(id)

        then:
        def e = thrown(ResourceNotFoundException)
        e.message.contains("Transaction with id 10 not found")
    }

    // ------------------------------------------------------------
    // findTransactions
    // ------------------------------------------------------------
    def "should delegate to transactionPersistenceService.findAllTransactions"() {
        given:
        def from = LocalDate.of(2024, 1, 1)
        def to = LocalDate.of(2024, 12, 31)
        def txList = [new Transaction(id: 1), new Transaction(id: 2)]

        when:
        def result = service.findTransactions(from, to)

        then:
        1 * transactionPersistenceService.findAllTransactions(from, to) >> txList
        result == txList
    }

    // ------------------------------------------------------------
    // deleteTransaction
    // ------------------------------------------------------------
    def "should delegate to transactionPersistenceService.deleteTransaction"() {
        when:
        service.deleteTransaction(5)

        then:
        1 * transactionPersistenceService.deleteTransaction(5)
    }

    // ------------------------------------------------------------
    // updateTransaction
    // ------------------------------------------------------------
    def "should update all non-null fields and save transaction"() {
        given:
        Integer id = 1
        def existing = new Transaction(id: id, amount: 100, description: "Old", planned: true, accountId: 5)
        def update = new Transaction(amount: 200, description: "Updated", planned: false, accountId: 7)
        def parent = new Transaction(id: 10)
        update.setParentTransaction(parent)

        transactionPersistenceService.findTransactionById(id) >> Optional.of(existing)
        transactionPersistenceService.findTransactionById(10) >> Optional.of(parent)
        accountPersistenceService.findAccountById(7) >> Optional.of(new Object())

        when:
        def result = service.updateTransaction(id, update)

        then:
        1 * transactionPersistenceService.saveTransaction({
            it.amount == 200 &&
                    it.description == "Updated" &&
                    !it.planned &&
                    it.accountId == 7 &&
                    it.parentTransaction == parent
        }) >> { it[0] }

        result.amount == 200
        result.description == "Updated"
        result.parentTransaction == parent
    }

    def "should throw ResourceNotFoundException if transaction not found"() {
        given:
        Integer id = 1
        transactionPersistenceService.findTransactionById(id) >> Optional.empty()

        when:
        service.updateTransaction(id, new Transaction())

        then:
        def e = thrown(ResourceNotFoundException)
        e.message.contains("Transaction 1 not found")
    }

    def "should throw InvalidResourceException if parentTransaction id is null"() {
        given:
        Integer id = 1
        def existing = new Transaction(id: id)
        def update = new Transaction(parentTransaction: new Transaction(id: null))
        transactionPersistenceService.findTransactionById(id) >> Optional.of(existing)

        when:
        service.updateTransaction(id, update)

        then:
        thrown(InvalidResourceException)
    }

    def "should throw ResourceNotFoundException if parentTransaction not found"() {
        given:
        Integer id = 1
        def existing = new Transaction(id: id)
        def update = new Transaction(parentTransaction: new Transaction(id: 99))
        transactionPersistenceService.findTransactionById(id) >> Optional.of(existing)
        transactionPersistenceService.findTransactionById(99) >> Optional.empty()

        when:
        service.updateTransaction(id, update)

        then:
        thrown(ResourceNotFoundException)
    }

    def "should throw ResourceNotFoundException if account not found"() {
        given:
        Integer id = 1
        def existing = new Transaction(id: id)
        def update = new Transaction(accountId: 33)
        transactionPersistenceService.findTransactionById(id) >> Optional.of(existing)
        accountPersistenceService.findAccountById(33) >> Optional.empty()

        when:
        service.updateTransaction(id, update)

        then:
        def e = thrown(ResourceNotFoundException)
        e.message.contains("Account 33 not found")
    }

    def "should only update non-null fields"() {
        given:
        Integer id = 1
        def existing = new Transaction(id: id, amount: 100, description: "Desc", planned: true)
        def update = new Transaction(amount: null, description: "", planned: null)

        transactionPersistenceService.findTransactionById(id) >> Optional.of(existing)

        when:
        def result = service.updateTransaction(id, update)

        then:
        1 * transactionPersistenceService.saveTransaction({
            it.amount == 100 &&
                    it.description == "Desc" &&
                    it.planned == true
        }) >> { it[0] }

        result.amount == 100
        result.description == "Desc"
        result.planned
    }

}
