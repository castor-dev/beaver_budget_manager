package com.beaverbudget.mapper.dto;

import com.beaverbudget.mapper.GenericMapper;
import com.beaverbudget.model.Account;
import com.beaverbudget.model.AccountDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AccountDTOtoAccountDomainMapper extends GenericMapper<AccountDTO, Account> {

    @Override
    default Class<AccountDTO> getSourceType(){
        return AccountDTO.class;
    }

    @Override
    default Class<Account> getTargetType(){
        return Account.class;
    }

    @Override
    Account map(AccountDTO source);
}
