package com.beaverbudget.controller

import com.beaverbudget.mapper.MapperProvider
import com.beaverbudget.model.User
import com.beaverbudget.model.UserDTO
import com.beaverbudget.service.UserService
import org.springframework.http.HttpStatus
import spock.lang.Specification

class UserControllerTest extends Specification {

    def userService = Mock(UserService)
    def mapperProvider = Mock(MapperProvider)
    def controller = new UserController(userService, mapperProvider)

    def "CreateUser should return CREATED when user is created successfully"() {
        given:
        def userDto = new UserDTO(id: 1, name: "John Doe")
        def user = new User(id: 1, name: "John Doe")
        userService.createUser(_ as User) >> user

        when:
        def response = controller.createUser(userDto)

        then:
        1 * mapperProvider.map(userDto, User.class) >> user
        1 * mapperProvider.map(user, UserDTO.class) >> userDto
        response.statusCode == HttpStatus.OK
        response.body == userDto

    }

    def "DeleteUser should return NO_CONTENT when user is deleted successfully"() {
        when:
        def response = controller.deleteUser(1)

        then:
        1 * userService.deleteUser(1)
        response.statusCode == HttpStatus.NO_CONTENT
        !response.hasBody()
    }

    def "GetUserById should return OK with user when found"() {
        given:
        def user = new User(id: 1, name: "John Doe")
        def userDto = new UserDTO();

        when:
        def response = controller.getUserById(1)

        then:
        1 * userService.findUserById(1) >> user
        1 * mapperProvider.map(user, UserDTO.class) >> userDto
        response.statusCode == HttpStatus.OK
        response.body == userDto
    }

    def "ListUsers should return OK with user list"() {
        given:
        def users = [new User(id: 1, name: "John"), new User(id: 2, name: "Jane")]
        userService.findAllUsers() >> users
        def userDTOs =[new UserDTO(id: 1, name: "John"), new UserDTO(id: 2, name: "Jane")]

        when:
        def response = controller.listUsers()

        then:
        1 * mapperProvider.map(users.get(0), UserDTO) >> userDTOs.get(0)
        1 * mapperProvider.map(users.get(1), UserDTO) >> userDTOs.get(1)
        response.statusCode == HttpStatus.OK
        response.body == userDTOs
    }

    def "UpdateUser should return OK when updated successfully"() {
        given:
        def updated = new UserDTO(id: 1, name: "Updated User")
        def user = new User(id: 1, name: "Updated User")

        when:
        def response = controller.updateUser(1, updated)

        then:
        1 * mapperProvider.map(updated, User.class) >> user
        1 * userService.updateUserById(1, user) >> user
        1 * mapperProvider.map(user, UserDTO.class) >> updated
        response.statusCode == HttpStatus.OK
        response.body == updated
    }
}
