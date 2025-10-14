package com.beaverbudget.controller

import com.beaverbudget.mapper.MapperProvider
import com.beaverbudget.model.Transaction
import com.beaverbudget.model.TransactionDTO
import com.beaverbudget.service.TransactionService
import org.springframework.http.HttpStatus
import spock.lang.Specification

import java.time.LocalDate

class TransactionControllerTest extends Specification {

    def transactionService = Mock(TransactionService)
    def mapperProvider = Mock(MapperProvider)
    def controller = new TransactionController(transactionService, mapperProvider)

    def "should get transaction by id"() {
        given:
        def transaction = Transaction.builder().id(1).build()
        def dto = new TransactionDTO(id: 1)

        when:
        def response = controller.getTransactionById(1)

        then:
        1 * transactionService.findTransactionById(1) >> transaction
        1 * mapperProvider.map(transaction, TransactionDTO.class) >> dto
        response.statusCode == HttpStatus.OK
        response.body == dto
    }

    def "should update transaction"() {
        given:
        def transaction = Transaction.builder().id(1).build()
        def updated = Transaction.builder().id(1).build()
        def dto = new TransactionDTO(id: 1)
        def updatedDto = new TransactionDTO(id: 1)

        when:
        def response = controller.updateTransaction(1, dto)

        then:
        1 * transactionService.findTransactionById(1) >> transaction
        1 * transactionService.updateTransaction(1, transaction) >> updated
        1 * mapperProvider.map(updated, TransactionDTO.class) >> updatedDto
        response.statusCode == HttpStatus.OK
        response.body == updatedDto
    }

    def "should list transactions"() {
        given:
        def from = LocalDate.of(2025, 1, 1)
        def to = LocalDate.of(2025, 12, 31)
        def transactions = [Transaction.builder().id(1).build()]
        def dtos = [new TransactionDTO(id: 1)]

        when:
        def response = controller.listTransactions(from, to)

        then:
        1 * transactionService.findTransactions(from, to) >> transactions
        1 * mapperProvider.map(transactions[0], TransactionDTO.class) >> dtos[0]
        response.statusCode == HttpStatus.OK
        response.body == dtos
    }

    def "should delete transaction"() {
        when:
        def response = controller.deleteTransaction(1)

        then:
        1 * transactionService.deleteTransaction(1)
        response.statusCode == HttpStatus.NO_CONTENT
        response.body == null
    }
}
