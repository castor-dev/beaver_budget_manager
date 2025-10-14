package com.beaverbudget.controller

import com.beaverbudget.mapper.MapperProvider
import com.beaverbudget.model.Account
import com.beaverbudget.model.AccountDTO
import com.beaverbudget.model.Transaction
import com.beaverbudget.model.TransactionDTO
import com.beaverbudget.service.AccountService
import org.springframework.http.HttpStatus
import spock.lang.Specification

class AccountControllerTest extends Specification {

    def accountService = Mock(AccountService)
    def mapperProvider = Mock(MapperProvider)
    def controller = new AccountController(accountService, mapperProvider)

    def "should create account"(){
        given:
        def account = Account.builder().id(1).build()
        def accountDto = new AccountDTO(id: 1)

        when:
        def responseEntity = controller.createAccount(accountDto)

        then:
        1 * mapperProvider.map(accountDto, Account.class) >> account
        1 * accountService.createAccount(account) >> account
        1 * mapperProvider.map(account, AccountDTO.class) >> accountDto
        responseEntity.statusCode == HttpStatus.OK
        responseEntity.body == accountDto
    }

    def "should get account by id"() {
        given:
        def account = Account.builder().id(1).build()
        def accountDto = new AccountDTO(id: 1)
        when:
        def responseEntity = controller.getAccountById(1)

        then:
        1 * accountService.findAccountById(1) >> account
        1 * mapperProvider.map(account, AccountDTO.class) >>accountDto
        responseEntity.statusCode == HttpStatus.OK
        responseEntity.body == accountDto
    }

    def "should list accounts"() {
        given:
        def accounts = [Account.builder().id(1).build(), Account.builder().id(2).build()]
        def dtos = [new AccountDTO(id: 1), new AccountDTO(id: 2)]

        when:
        def responseEntity = controller.listAccounts()

        then:
        1 * accountService.findAllAccounts() >> accounts
        1 * mapperProvider.map(accounts[0], AccountDTO.class) >> dtos[0]
        1 * mapperProvider.map(accounts[1], AccountDTO.class) >> dtos[1]
        responseEntity.statusCode == HttpStatus.OK
        responseEntity.body == dtos
    }

    def "should update account"() {
        given:
        def accountDto = new AccountDTO(id: 1, name: "Updated")
        def account = Account.builder().id(1).name("Updated").build()
        def updated = Account.builder().id(1).name("Updated").build()
        def updatedDto = new AccountDTO(id: 1, name: "Updated")

        when:
        def responseEntity = controller.updateAccount(1, accountDto)

        then:
        1 * mapperProvider.map(accountDto, Account.class) >> account
        1 * accountService.updateAccount(1, account) >> updated
        1 * mapperProvider.map(updated, AccountDTO.class) >> updatedDto
        responseEntity.statusCode == HttpStatus.OK
        responseEntity.body == updatedDto
    }

    def "should delete account"() {
        when:
        def responseEntity = controller.deleteAccount(1)

        then:
        1 * accountService.deleteAccount(1)
        responseEntity.statusCode == HttpStatus.NO_CONTENT
        responseEntity.body == null
    }



    def "should create account transaction"() {
        given:

        def transactionDto = new TransactionDTO(id: 1)
        def transaction = Transaction.builder().id(1).build()
        def savedTransaction = Transaction.builder().id(1).build()
        def savedDto = new TransactionDTO(id: 1)

        when:
        def responseEntity = controller.createAccountTransaction(1, transactionDto)

        then:
        1 * mapperProvider.map(transactionDto, Transaction.class) >> transaction
        1 * accountService.createAccountTransaction(1, transaction) >> savedTransaction
        1 * mapperProvider.map(savedTransaction, TransactionDTO.class) >> savedDto
        responseEntity.statusCode == HttpStatus.OK
        responseEntity.body == savedDto
    }

    def "should get account transaction by id"() {
        given:
        def transaction = Transaction.builder().id(1).build()
        def dto = new TransactionDTO(id: 1)

        when:
        def responseEntity = controller.getAccountTransactionById(1, 1)

        then:
        1 * accountService.findAccountTransactionById(1, 1) >> transaction
        1 * mapperProvider.map(transaction, TransactionDTO.class) >> dto
        responseEntity.statusCode == HttpStatus.OK
        responseEntity.body == dto
    }

    def "should update account transaction"() {
        given:
        def transactionDto = new TransactionDTO(id: 1, description: "Updated")
        def transaction = Transaction.builder().id(1).description("Updated").build()
        def updated = Transaction.builder().id(1).description("Updated").build()
        def updatedDto = new TransactionDTO(id: 1, description: "Updated")

        when:
        def responseEntity = controller.updateAccountTransaction(1, 1, transactionDto)

        then:
        1 * mapperProvider.map(transactionDto, Transaction.class) >> transaction
        1 * accountService.updateAccountTransaction(1, 1, transaction) >> updated
        1 * mapperProvider.map(updated, TransactionDTO.class) >> updatedDto
        responseEntity.statusCode == HttpStatus.OK
        responseEntity.body == updatedDto
    }

    def "should delete account transaction"() {
        when:
        def responseEntity = controller.deleteAccountTransaction(1, 1)

        then:
        1 * accountService.deleteAccountTransaction(1, 1)
        responseEntity.statusCode == HttpStatus.NO_CONTENT
        responseEntity.body == null
    }

}
