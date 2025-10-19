package com.beaverbudget.mapper.entity;

import com.beaverbudget.mapper.GenericMapper;
import com.beaverbudget.model.User;
import com.beaverbudget.persistence.repository.entity.UserEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserDomainToUserEntityMapper extends GenericMapper<User, UserEntity> {

    @Override
    default Class<User> getSourceType() {
        return User.class;
    }

    @Override
    default Class<UserEntity> getTargetType(){
        return UserEntity.class;
    }

}
