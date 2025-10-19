package com.beaverbudget.mapper.entity;

import com.beaverbudget.mapper.GenericMapper;
import com.beaverbudget.model.User;
import com.beaverbudget.persistence.repository.entity.UserEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserEntityToUserDomainMapper extends GenericMapper<UserEntity, User> {

    @Override
    default Class<UserEntity> getSourceType() {
        return UserEntity.class;
    }

    @Override
    default Class<User> getTargetType() {
        return User.class;
    }

}
