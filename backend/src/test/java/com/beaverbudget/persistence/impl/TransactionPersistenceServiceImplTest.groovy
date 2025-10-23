package com.beaverbudget.persistence.impl

import com.beaverbudget.mapper.MapperProvider
import com.beaverbudget.model.Transaction
import com.beaverbudget.persistence.repository.TransactionRepository
import com.beaverbudget.persistence.repository.entity.TransactionEntity
import spock.lang.Specification

import java.time.LocalDateTime

class TransactionPersistenceServiceImplTest extends Specification {

    def repository = Mock(TransactionRepository)
    def mapperProvider = Mock(MapperProvider)
    def service = new TransactionPersistenceServiceImpl(repository, mapperProvider)

    def "should save transaction successfully"() {
        given:
        def transaction = new Transaction(id: 1, description: "test transaction")
        def entity = new TransactionEntity(id: 1, description: "test transaction")
        def savedEntity = new TransactionEntity(id: 1, description: "test transaction")
        def mappedBack = new Transaction(id: 1, description: "test transaction")

        when:
        def result = service.saveTransaction(transaction)

        then:
        1 * mapperProvider.map(transaction, TransactionEntity.class) >> entity
        1 * repository.save(entity) >> savedEntity
        1 * mapperProvider.map(savedEntity, Transaction.class) >> mappedBack

        and:
        result == mappedBack
    }

    def "should find transaction by id when exists"() {
        given:
        def entity = new TransactionEntity(id: 1)
        def transaction = new Transaction(id: 1)
        repository.findById(1) >> Optional.of(entity)
        mapperProvider.map(entity, Transaction.class) >> transaction

        when:
        def result = service.findTransactionById(1)

        then:
        result.isPresent()
        result.get() == transaction
    }

    def "should return empty optional when transaction not found"() {
        given:
        repository.findById(10) >> Optional.empty()
        mapperProvider.map(null, Transaction.class) >> null

        when:
        def result = service.findTransactionById(10)

        then:
        !result.isPresent()
    }

    def "should find all account transactions with date filters"() {
        given:
        def from = LocalDateTime.now().minusDays(10)
        def to = LocalDateTime.now()
        def entities = [new TransactionEntity(id: 1), new TransactionEntity(id: 2)]
        def transactions = [new Transaction(id: 1), new Transaction(id: 2)]

        repository.findAccountTransactions(5, from, to) >> entities
        mapperProvider.map(entities[0], Transaction.class) >> transactions[0]
        mapperProvider.map(entities[1], Transaction.class) >> transactions[1]

        when:
        def result = service.findAllAccountTransactions(5, from, to)

        then:
        result == transactions
    }

    def "should find all transactions with date filters"() {
        given:
        def from = LocalDateTime.now().minusDays(5)
        def to = LocalDateTime.now()
        def entities = [new TransactionEntity(id: 3)]
        def transactions = [new Transaction(id: 3)]

        repository.findTransactions(from, to) >> entities
        mapperProvider.map(entities[0], Transaction.class) >> transactions[0]

        when:
        def result = service.findAllTransactions(from, to)

        then:
        result == transactions
    }

    def "should delete transaction by id"() {
        when:
        service.deleteTransaction(10)

        then:
        1 * repository.deleteById(10)
    }
}