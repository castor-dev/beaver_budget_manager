package com.beaverbudget.service.impl

import com.beaverbudget.exceptions.InvalidResourceException
import com.beaverbudget.exceptions.ResourceNotFoundException
import com.beaverbudget.model.Account
import com.beaverbudget.model.Transaction
import com.beaverbudget.persistence.AccountPersistenceService
import com.beaverbudget.persistence.TransactionPersistenceService
import com.beaverbudget.persistence.UserPersistenceService
import spock.lang.Specification
import spock.lang.Subject

import java.time.LocalDate

class AccountServiceImplTest extends Specification {

    def accountPersistenceService = Mock(AccountPersistenceService)
    def transactionPersistenceService = Mock(TransactionPersistenceService)
    def userPersistenceService = Mock(UserPersistenceService)

    @Subject
    AccountServiceImpl accountService = new AccountServiceImpl(
            accountPersistenceService,
            transactionPersistenceService,
            userPersistenceService
    )

    def "should create account"(){
        given:
        def account = new Account()

        when:
        accountService.createAccount(account)

        then:
        1 * accountPersistenceService.createAccount(account)
    }

    def "should find account by id"(){
        given:
        def accountId = 1

        when:
        accountService.findAccountById(accountId)

        then:
        1 * accountPersistenceService.findAccountById(accountId) >> Optional.of(new Account())
    }

    def "should throw exception when account not found"(){
        given:
        def accountId = 1

        when:
        accountService.findAccountById(accountId)

        then:
        1 * accountPersistenceService.findAccountById(_) >> Optional.empty()
        thrown ResourceNotFoundException
    }

    def "should list all accounts"(){
        given:
        def accounts = [new Account(id: 1), new Account(id:2)]

        when:
        accountService.findAllAccounts()

        then:
        1 * accountPersistenceService.findAllAccounts() >> accounts
    }


    def "should update all provided fields and save account"() {
        given:
        def existing = new Account(id: 1, name: "Old Name", ownerId: 10, balance: 100.0, currency: "USD")
        def update = new Account(name: "New Name", ownerId: 20, balance: 200.0, currency: "EUR")

        accountPersistenceService.findAccountById(1) >> Optional.of(existing)
        userPersistenceService.findUserById(20) >> Optional.of(new Object())

        when:
        def result = accountService.updateAccount(1, update)

        then:
        1 * accountPersistenceService.saveAccount({
            it.name == "New Name"
            it.ownerId == 20
            it.balance == 200.0
            it.currency == "EUR"
        }) >> { it[0] }

        result.name == "New Name"
        result.ownerId == 20
        result.balance == 200.0
        result.currency == "EUR"
    }

    def "should only update fields that are not null"() {
        given:
        def existing = new Account(id: 1, name: "Old", ownerId: 10, balance: 100, currency: "USD")
        def update = new Account(name: null, ownerId: null, balance: 250, currency: null)

        accountPersistenceService.findAccountById(1) >> Optional.of(existing)

        when:
        def result = accountService.updateAccount(1, update)

        then:
        1 * accountPersistenceService.saveAccount({
            it.name == "Old"
            it.ownerId == 10
            it.balance == 250
            it.currency == "USD"
        }) >> { it[0] }

        result.balance == 250
        result.name == "Old"
    }

    def "should throw ResourceNotFoundException if account not found"() {
        given:
        accountPersistenceService.findAccountById(99) >> Optional.empty()

        when:
        accountService.updateAccount(99, new Account())

        then:
        def e = thrown(ResourceNotFoundException)
        e.message.contains("Account with id 99 not found")
    }

    def "should throw ResourceNotFoundException if new owner does not exist"() {
        given:
        def existing = new Account(id: 1, name: "Account A", ownerId: 10)
        def update = new Account(ownerId: 20)

        accountPersistenceService.findAccountById(1) >> Optional.of(existing)
        userPersistenceService.findUserById(20) >> Optional.empty()

        when:
        accountService.updateAccount(1, update)

        then:
        def e = thrown(ResourceNotFoundException)
        e.message.contains("User with id 20 not found")
    }

    def "should call userPersistenceService only when ownerId changes"() {
        given:
        def existing = new Account(id: 1, name: "Name", ownerId: 10)
        def update = new Account(ownerId: null)

        accountPersistenceService.findAccountById(1) >> Optional.of(existing)

        when:
        accountService.updateAccount(1, update)

        then:
        0 * userPersistenceService.findUserById(_)
        1 * accountPersistenceService.saveAccount(_)
    }

    def "should delete account"(){
        given:
        def accountId = 1

        when:
        accountService.deleteAccount(accountId)

        then:
        1 * accountPersistenceService.deleteAccount(accountId)
    }
    def "createAccountTransaction should delegate to transactionPersistenceService"() {
        given:
        Integer accountId = 1
        def transaction = new Transaction()

        when:
        def result = accountService.createAccountTransaction(accountId, transaction)

        then:
        1 * transactionPersistenceService.createAccountTransaction(accountId, transaction) >> transaction
        result == transaction
    }

    def "findAccountTransactionById should return transaction when found"() {
        given:
        Integer accountId = 1
        Integer transactionId = 10
        def transaction = new Transaction(id: transactionId)

        transactionPersistenceService.findAccountTransactionById(accountId, transactionId) >> Optional.of(transaction)

        when:
        def result = accountService.findAccountTransactionById(accountId, transactionId)

        then:
        result == transaction
    }

    def "findAccountTransactionById should throw ResourceNotFoundException when not found"() {
        given:
        Integer accountId = 1
        Integer transactionId = 99

        transactionPersistenceService.findAccountTransactionById(accountId, transactionId) >> Optional.empty()

        when:
        accountService.findAccountTransactionById(accountId, transactionId)

        then:
        def e = thrown(ResourceNotFoundException)
        e.message.contains("Transaction")
        e.message.contains(accountId.toString())
        e.message.contains(transactionId.toString())
    }

    def "findAccountTransactions should delegate to transactionPersistenceService"() {
        given:
        Integer accountId = 1
        def from = LocalDate.of(2024, 1, 1)
        def to = LocalDate.of(2024, 12, 31)
        def transactions = [new Transaction(id: 1), new Transaction(id: 2)]

        when:
        def result = accountService.findAccountTransactions(accountId, from, to)

        then:
        1 * transactionPersistenceService.findAllAccountTransactions(accountId, from, to)  >> transactions
        result == transactions
    }

    // ------------------------------------------------------------------
    // updateAccountTransaction
    // ------------------------------------------------------------------
    def "should update all provided transaction fields and save"() {
        given:
        Integer accountId = 1
        Integer transactionId = 10
        def existing = new Transaction(id: transactionId, amount: 50, description: "Old", planned: true)
        def update = new Transaction(amount: 200, description: "Updated", planned: false)

        transactionPersistenceService.findAccountTransactionById(accountId, transactionId) >> Optional.of(existing)

        when:
        def result = accountService.updateAccountTransaction(accountId, transactionId, update)

        then:
        1 * transactionPersistenceService.saveTransaction({
            it.amount == 200 &&
                    it.description == "Updated" &&
                    !it.planned
        }) >> { it[0] }

        result.amount == 200
        result.description == "Updated"
        !result.planned
    }

    def "should only update non-null and non-empty fields"() {
        given:
        Integer accountId = 1
        Integer transactionId = 5
        def existing = new Transaction(id: transactionId, amount: 100, description: "Keep", planned: true)
        def update = new Transaction(amount: null, description: "", planned: null)

        transactionPersistenceService.findAccountTransactionById(accountId, transactionId) >> Optional.of(existing)

        when:
        def result = accountService.updateAccountTransaction(accountId, transactionId, update)

        then:
        1 * transactionPersistenceService.saveTransaction({
            it.amount == 100 &&
                    it.description == "Keep" &&
                    it.planned == true
        }) >> { it[0] }

        result.description == "Keep"
        result.amount == 100
    }

    def "should throw ResourceNotFoundException when transaction not found"() {
        given:
        Integer accountId = 1
        Integer transactionId = 10
        def update = new Transaction(amount: 200)

        transactionPersistenceService.findAccountTransactionById(accountId, transactionId) >> Optional.empty()

        when:
        accountService.updateAccountTransaction(accountId, transactionId, update)

        then:
        def e = thrown(ResourceNotFoundException)
        e.message.contains("Transaction")
        e.message.contains(transactionId.toString())
    }

    def "should throw InvalidResourceException if parentTransaction id is null"() {
        given:
        Integer accountId = 1
        Integer transactionId = 5
        def existing = new Transaction(id: transactionId)
        def update = new Transaction(parentTransaction: new Transaction(id: null))

        transactionPersistenceService.findAccountTransactionById(accountId, transactionId) >> Optional.of(existing)

        when:
        accountService.updateAccountTransaction(accountId, transactionId, update)

        then:
        thrown(InvalidResourceException)
    }

    def "should throw ResourceNotFoundException if parentTransaction not found"() {
        given:
        Integer accountId = 1
        Integer transactionId = 10
        def existing = new Transaction(id: transactionId)
        def update = new Transaction(parentTransaction: new Transaction(id: 99))

        transactionPersistenceService.findAccountTransactionById(accountId, transactionId) >> Optional.of(existing)
        transactionPersistenceService.findAccountTransactionById(accountId, 99) >> Optional.empty()

        when:
        accountService.updateAccountTransaction(accountId, transactionId, update)

        then:
        def e = thrown(ResourceNotFoundException)
        e.message.contains("Transaction 1 not found") || e.message.contains("99")
    }

    def "should correctly set parentTransaction when found"() {
        given:
        Integer accountId = 1
        Integer transactionId = 10
        def existing = new Transaction(id: transactionId)
        def parent = new Transaction(id: 99)
        def update = new Transaction(parentTransaction: new Transaction(id: 99))

        transactionPersistenceService.findAccountTransactionById(accountId, transactionId) >> Optional.of(existing)
        transactionPersistenceService.findAccountTransactionById(accountId, 99) >> Optional.of(parent)

        when:
        def result = accountService.updateAccountTransaction(accountId, transactionId, update)

        then:
        1 * transactionPersistenceService.saveTransaction({
            it.parentTransaction == parent
        }) >> { it[0] }

        result.parentTransaction == parent
    }

    // ------------------------------------------------------------------
    // deleteAccountTransaction
    // ------------------------------------------------------------------
    def "should delegate deleteAccountTransaction to transactionPersistenceService"() {
        given:
        Integer accountId = 1
        Integer transactionId = 5

        when:
        accountService.deleteAccountTransaction(accountId, transactionId)

        then:
        1 * transactionPersistenceService.deleteTransaction(accountId, transactionId)
    }


    // ------------------------------------------------------------------
    // executePlannedTransaction
    // ------------------------------------------------------------------
    def "should execute planned transaction successfully"() {
        given:
        Integer accountId = 1
        Integer plannedTransactionId = 10
        def plannedTransaction = new Transaction(id: plannedTransactionId, planned: true)
        def newTransaction = new Transaction(amount: 100)

        transactionPersistenceService.findAccountTransactionById(accountId, plannedTransactionId) >> Optional.of(plannedTransaction)

        when:
        def result = accountService.executePlannedTransaction(accountId, plannedTransactionId, newTransaction)

        then:
        result.parentTransaction == plannedTransaction
        result.planned == false

        1 * transactionPersistenceService.createAccountTransaction(accountId, {
            it.parentTransaction == plannedTransaction &&
                    !it.planned
        }) >> { args -> args[1] }
    }

    def "should throw ResourceNotFoundException when planned transaction not found"() {
        given:
        Integer accountId = 1
        Integer plannedTransactionId = 99
        def newTransaction = new Transaction()

        transactionPersistenceService.findAccountTransactionById(accountId, plannedTransactionId) >> Optional.empty()

        when:
        accountService.executePlannedTransaction(accountId, plannedTransactionId, newTransaction)

        then:
        def e = thrown(ResourceNotFoundException)
        e.message.contains("Transaction")
        e.message.contains(plannedTransactionId.toString())
    }

    def "should throw InvalidResourceException when parent transaction is not planned"() {
        given:
        Integer accountId = 1
        Integer plannedTransactionId = 10
        def parentTransaction = new Transaction(id: plannedTransactionId, planned: false)
        def newTransaction = new Transaction(amount: 50)

        transactionPersistenceService.findAccountTransactionById(accountId, plannedTransactionId) >> Optional.of(parentTransaction)

        when:
        accountService.executePlannedTransaction(accountId, plannedTransactionId, newTransaction)

        then:
        def e = thrown(InvalidResourceException)
        e.message == "Not a planned transaction"
    }

    def "should ensure new transaction inherits parent and sets planned to false"() {
        given:
        Integer accountId = 1
        Integer plannedTransactionId = 10
        def plannedTransaction = new Transaction(id: plannedTransactionId, planned: true)
        def newTransaction = new Transaction(planned: true, amount: 200)

        transactionPersistenceService.findAccountTransactionById(accountId, plannedTransactionId) >> Optional.of(plannedTransaction)
        transactionPersistenceService.createAccountTransaction(accountId, _) >> { args -> args[1] }

        when:
        def result = accountService.executePlannedTransaction(accountId, plannedTransactionId, newTransaction)

        then:
        result.parentTransaction == plannedTransaction
        !result.planned
    }

}