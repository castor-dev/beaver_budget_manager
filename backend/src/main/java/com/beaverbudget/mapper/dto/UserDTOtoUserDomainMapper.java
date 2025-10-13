package com.beaverbudget.mapper.dto;

import com.beaverbudget.mapper.GenericMapper;
import com.beaverbudget.model.User;
import com.beaverbudget.model.UserDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserDTOtoUserDomainMapper extends GenericMapper<UserDTO, User> {

    @Override
    default Class<UserDTO> getSourceType(){
        return UserDTO.class;
    }

    @Override
    default Class<User> getTargetType(){
        return User.class;
    }
}
