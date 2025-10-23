package com.beaverbudget.persistence.impl

import com.beaverbudget.mapper.MapperProvider
import com.beaverbudget.model.Account
import com.beaverbudget.persistence.repository.AccountRepository
import com.beaverbudget.persistence.repository.entity.AccountEntity
import spock.lang.Specification

class AccountPersistenceServiceImplTest extends Specification {

    def mapperProvider = Mock(MapperProvider)
    def accountRepository = Mock(AccountRepository)
    def service = new AccountPersistenceServiceImpl(mapperProvider, accountRepository)

    def "should find account by id"() {
        given:
        def accountId = 1
        def entity = new AccountEntity()
        def domain = new Account()

        accountRepository.findById(accountId) >> Optional.of(entity)
        mapperProvider.map(entity, Account.class) >> domain

        when:
        def result = service.findAccountById(accountId)

        then:
        result.isPresent()
        result.get() == domain
    }

    def "should return empty optional when account not found"() {
        given:
        def accountId = 99

        when:
        def result = service.findAccountById(accountId)

        then:
        1 * accountRepository.findById(accountId) >> Optional.empty()
        !result.isPresent()
        1 * mapperProvider.map(null, _)
    }

    def "should find all accounts"() {
        given:
        def entity1 = new AccountEntity()
        def entity2 = new AccountEntity()
        def domain1 = new Account()
        def domain2 = new Account()


        when:
        def result = service.findAllAccounts()

        then:
        1 * accountRepository.findAll() >> [entity1, entity2]
        1 * mapperProvider.map(entity1, Account.class) >> domain1
        1 * mapperProvider.map(entity2, Account.class) >> domain2
        result.size() == 2
        result.containsAll([domain1, domain2])
    }

    def "should save account"() {
        given:
        def domain = new Account()
        def entity = new AccountEntity()
        def savedEntity = new AccountEntity()
        def savedDomain = new Account()

        mapperProvider.map(domain, AccountEntity.class) >> entity
        accountRepository.save(entity) >> savedEntity
        mapperProvider.map(savedEntity, Account.class) >> savedDomain

        when:
        def result = service.saveAccount(domain)

        then:
        result == savedDomain
    }

    def "should delete account"() {
        given:
        def accountId = 1

        when:
        service.deleteAccount(accountId)

        then:
        1 * accountRepository.deleteById(accountId)
    }
}