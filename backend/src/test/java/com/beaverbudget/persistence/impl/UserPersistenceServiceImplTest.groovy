package com.beaverbudget.persistence.impl

import com.beaverbudget.exceptions.InvalidResourceException
import com.beaverbudget.mapper.MapperProvider
import com.beaverbudget.model.User
import com.beaverbudget.persistence.repository.UserRepository
import com.beaverbudget.persistence.repository.entity.UserEntity
import spock.lang.Specification

class UserPersistenceServiceImplTest extends Specification {

    def mapperProvider = Mock(MapperProvider)
    def userRepository = Mock(UserRepository)
    def service = new UserPersistenceServiceImpl(mapperProvider, userRepository)

    def "should save user when email is unique"() {
        given:
        def user = new User(email: "test@mail.com")
        def userEntity = new UserEntity()
        def savedEntity = new UserEntity()
        def mappedUser = new User(id: 1, email: "test@mail.com")

        when:
        def result = service.saveUser(user)

        then:
        1 * userRepository.findByEmail("test@mail.com") >> []
        1 * mapperProvider.map(user, UserEntity) >> userEntity
        1 * userRepository.save(userEntity) >> savedEntity
        1 * mapperProvider.map(savedEntity, User) >> mappedUser

        result == mappedUser
    }

    def "should throw InvalidResourceException when email already exists"() {
        given:
        def user = new User(email: "duplicate@mail.com")

        when:
        service.saveUser(user)

        then:
        1 * userRepository.findByEmail("duplicate@mail.com") >> [new UserEntity()]
        0 * userRepository.save(_)
        def ex = thrown(InvalidResourceException)
        ex.message == "user email already used"
    }

    def "should find user by id"() {
        given:
        def entity = new UserEntity(id: 1, email: "a@mail.com")
        def mapped = new User(id: 1, email: "a@mail.com")

        when:
        def result = service.findUserById(1)

        then:
        1 * userRepository.findById(1) >> Optional.of(entity)
        1 * mapperProvider.map(entity, User) >> mapped
        result.isPresent()
        result.get() == mapped
    }

    def "should return empty optional when user not found"() {
        when:
        def result = service.findUserById(99)

        then:
        1 * userRepository.findById(99) >> Optional.empty()
        1 * mapperProvider.map(null, User) >> null
        !result.isPresent()
    }

    def "should find all users"() {
        given:
        def entity1 = new UserEntity()
        def entity2 = new UserEntity()
        def mapped1 = new User()
        def mapped2 = new User()

        when:
        def result = service.findAllUsers()

        then:
        1 * userRepository.findAll() >> [entity1, entity2]
        1 * mapperProvider.map(entity1, User) >> mapped1
        1 * mapperProvider.map(entity2, User) >> mapped2

        result.size() == 2
        result.containsAll([mapped1, mapped2])
    }

    def "should delete user by id"() {
        when:
        service.deleteUser(42)

        then:
        1 * userRepository.deleteById(42)
    }


}
