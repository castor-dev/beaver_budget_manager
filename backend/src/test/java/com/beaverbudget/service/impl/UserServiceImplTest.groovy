package com.beaverbudget.service.impl


import com.beaverbudget.exceptions.ResourceNotFoundException
import com.beaverbudget.model.User
import com.beaverbudget.persistence.UserPersistenceService
import org.springframework.security.crypto.password.PasswordEncoder
import spock.lang.Specification

import java.time.LocalDateTime

class UserServiceImplTest extends Specification {

    def userPersistenceService = Mock(UserPersistenceService)
    def passwordEncoder = Mock(PasswordEncoder)
    def service = new UserServiceImpl(userPersistenceService, passwordEncoder)

    def "should create user"() {
        given:
        def user = new User(id: 1, name: "Alice")

        when:
        def result = service.createUser(user)

        then:
        1 * userPersistenceService.saveUser(user) >> user
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

    def "should update user email"() {
        given:
        def existing = new User(id: 1, email: "old@mail.com")
        def update = new User(email: "new@mail.com")
        userPersistenceService.findUserById(1) >> Optional.of(existing)
        userPersistenceService.saveUser(_ as User) >> update

        when:
        def result = service.updateUserById(1, update)

        then:
        result.email == "new@mail.com"
    }

    def "should update password and set expiry"() {
        given:
        def existing = new User(id: 1)
        def update = new User(passwordHash: "newPass", passwordExpireDate: LocalDateTime.now().plusMonths(3))
        userPersistenceService.findUserById(1) >> Optional.of(existing)
        userPersistenceService.saveUser(_ as User) >> update

        when:
        def result = service.updateUserById(1, update)

        then:
        result.passwordHash == "newPass"
        result.passwordExpireDate.isAfter(LocalDateTime.now())
    }

    def "should hash password"(){
        given:
        def hashedPassword = "hashedPassword"
        def user = new User(id: 1, passwordHash: "password")

        when:
        def createUser = service.createUser(user)

        then:
        1 * passwordEncoder.encode(_) >> hashedPassword
        1 * userPersistenceService.saveUser(_)
    }

    def "should delete user"() {
        when:
        service.deleteUser(1)

        then:
        1 * userPersistenceService.deleteUser(1)
    }
}
