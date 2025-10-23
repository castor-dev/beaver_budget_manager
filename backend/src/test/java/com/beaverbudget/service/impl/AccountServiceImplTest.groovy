package com.beaverbudget.service.impl

import com.beaverbudget.exceptions.ResourceNotFoundException
import com.beaverbudget.model.Account
import com.beaverbudget.persistence.AccountPersistenceService
import com.beaverbudget.persistence.UserPersistenceService
import spock.lang.Specification
import spock.lang.Subject

class AccountServiceImplTest extends Specification {

    def accountPersistenceService = Mock(AccountPersistenceService)
    def userPersistenceService = Mock(UserPersistenceService)

    @Subject
    AccountServiceImpl service = new AccountServiceImpl(
            accountPersistenceService,
            userPersistenceService
    )

    def "should create account"() {
        given:
        def account = new Account(id: 1, name: "Main", balance: 100.0G, currency: "EUR")

        when:
        service.createAccount(account)

        then:
        1 * accountPersistenceService.saveAccount(account) >> account
    }

    def "should find account by id"() {
        given:
        def account = new Account(id: 1, name: "Wallet")
        accountPersistenceService.findAccountById(1) >> Optional.of(account)

        when:
        def result = service.findAccountById(1)

        then:
        result == account
    }

    def "should throw ResourceNotFoundException when account not found"() {
        given:
        accountPersistenceService.findAccountById(99) >> Optional.empty()

        when:
        service.findAccountById(99)

        then:
        def e = thrown(ResourceNotFoundException)
        e.message == "Account with id 99 not found"
    }

    def "should return all accounts"() {
        given:
        def accounts = [new Account(id: 1), new Account(id: 2)]
        accountPersistenceService.findAllAccounts() >> accounts

        when:
        def result = service.findAllAccounts()

        then:
        result == accounts
    }

    def "should update account fields and save"() {
        given:
        def existing = new Account(id: 1, name: "Old", balance: 100G, currency: "USD", ownerId: 1)
        def update = new Account(name: "New", balance: 200G, currency: "EUR", ownerId: 2)

        accountPersistenceService.findAccountById(1) >> Optional.of(existing)
        userPersistenceService.findUserById(2) >> Optional.of(new Object()) // simulate user found

        when:
        def result = service.updateAccount(1, update)

        then:
        1 * accountPersistenceService.saveAccount({
            it.name == "New"
            it.balance == 200G
            it.currency == "EUR"
            it.ownerId == 2
        }) >> existing
        result == existing
    }

    def "should throw ResourceNotFoundException when updating non-existing account"() {
        given:
        accountPersistenceService.findAccountById(1) >> Optional.empty()

        when:
        service.updateAccount(1, new Account())

        then:
        thrown(ResourceNotFoundException)
    }

    def "should throw ResourceNotFoundException when updating with non-existing user"() {
        given:
        def existing = new Account(id: 1)
        def update = new Account(ownerId: 9)
        accountPersistenceService.findAccountById(1) >> Optional.of(existing)
        userPersistenceService.findUserById(9) >> Optional.empty()

        when:
        service.updateAccount(1, update)

        then:
        thrown(ResourceNotFoundException)
    }

    def "should delete account by id"() {
        when:
        service.deleteAccount(10)

        then:
        1 * accountPersistenceService.deleteAccount(10)
    }
}
