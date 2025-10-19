package com.beaverbudget.mapper.entity;

import com.beaverbudget.mapper.GenericMapper;
import com.beaverbudget.model.Account;
import com.beaverbudget.persistence.repository.entity.AccountEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AccountDomainToAccountEntityMapper extends GenericMapper<Account, AccountEntity> {

    @Override
    default Class<Account> getSourceType() {
        return Account.class;
    }

    @Override
    default Class<AccountEntity> getTargetType(){
        return AccountEntity.class;
    }

}
