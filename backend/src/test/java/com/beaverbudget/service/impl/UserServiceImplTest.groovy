package com.beaverbudget.service.impl

import com.beaverbudget.exceptions.InvalidResourceException
import com.beaverbudget.exceptions.ResourceNotFoundException
import com.beaverbudget.model.User
import com.beaverbudget.persistence.UserPersistenceService
import spock.lang.Specification

import java.time.LocalDateTime

class UserServiceImplTest extends Specification {

    def userPersistenceService = Mock(UserPersistenceService)
    def service = new UserServiceImpl(userPersistenceService)

    def "should create user"() {
        given:
        def user = new User(id: 1, name: "Alice")

        when:
        def result = service.createUser(user)

        then:
        1 * userPersistenceService.createUser(user) >> user
        result == user
    }

    def "should find user by id"() {
        given:
        def user = new User(id: 1, name: "Bob")

        when:
        def result = service.findUserById(1)

        then:
        1 * userPersistenceService.findUserById(1) >> Optional.of(user)
        result == user
    }

    def "should throw ResourceNotFoundException when user not found"() {
        given:
        userPersistenceService.findUserById(99) >> Optional.empty()

        when:
        service.findUserById(99)

        then:
        thrown(ResourceNotFoundException)
    }

    def "should find all users"() {
        given:
        def users = [new User(id: 1), new User(id: 2)]
        userPersistenceService.findAllUsers() >> users

        when:
        def result = service.findAllUsers()

        then:
        result == users
    }

    def "should update user name only"() {
        given:
        def existing = new User(id: 1, name: "OldName")
        def update = new User(name: "NewName")
        userPersistenceService.findUserById(1) >> Optional.of(existing)
        userPersistenceService.saveUser(_ as User) >> update

        when:
        def result = service.updateUserById(1, update)

        then:
        result.name == "NewName"
    }

    def "should update user email if not duplicated"() {
        given:
        def existing = new User(id: 1, email: "old@mail.com")
        def update = new User(email: "new@mail.com")
        userPersistenceService.findUserById(1) >> Optional.of(existing)
        userPersistenceService.findUserByEmail("new@mail.com") >> Optional.empty()
        userPersistenceService.saveUser(_ as User) >> update

        when:
        def result = service.updateUserById(1, update)

        then:
        result.email == "new@mail.com"
    }

    def "should throw InvalidResourceException if email already exists"() {
        given:
        def existing = new User(id: 1, email: "old@mail.com")
        def update = new User(email: "duplicate@mail.com")
        userPersistenceService.findUserById(1) >> Optional.of(existing)
        userPersistenceService.findUserByEmail("duplicate@mail.com") >> Optional.of(new User(id: 2, email: "duplicate@mail.com"))

        when:
        service.updateUserById(1, update)

        then:
        thrown(InvalidResourceException)
    }

    def "should update password and set expiry"() {
        given:
        def existing = new User(id: 1)
        def update = new User(password: "newPass", passwordExpireDate: LocalDateTime.now().plusMonths(3))
        userPersistenceService.findUserById(1) >> Optional.of(existing)
        userPersistenceService.saveUser(_ as User) >> update

        when:
        def result = service.updateUserById(1, update)

        then:
        result.password == "newPass"
        result.passwordExpireDate.isAfter(LocalDateTime.now())
    }

    def "should delete user"() {
        when:
        service.deleteUser(1)

        then:
        1 * userPersistenceService.deleteUser(1)
    }
}
