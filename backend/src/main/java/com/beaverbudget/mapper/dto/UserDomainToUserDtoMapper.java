package com.beaverbudget.mapper.dto;

import com.beaverbudget.mapper.GenericMapper;
import com.beaverbudget.model.User;
import com.beaverbudget.model.UserDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserDomainToUserDtoMapper extends GenericMapper<User, UserDTO> {

    @Override
    default Class<User> getSourceType() {
        return User.class;
    }

    @Override
    default Class<UserDTO> getTargetType() {
        return UserDTO.class;
    }

    @Mapping(target = "password", source = "passwordHash")
    @Override
    UserDTO map(User source);
}
