package com.beaverbudget.mapper.entity;

import com.beaverbudget.mapper.GenericMapper;
import com.beaverbudget.model.Account;
import com.beaverbudget.persistence.repository.entity.AccountEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AccountEntityToAccountDomainMapper extends GenericMapper<AccountEntity, Account> {

    @Override
    default Class<AccountEntity> getSourceType() {
        return AccountEntity.class;
    }

    @Override
    default Class<Account> getTargetType() {
        return Account.class;
    }

}
