package com.beaverbudget.mapper.dto;

import com.beaverbudget.mapper.GenericMapper;
import com.beaverbudget.model.Account;
import com.beaverbudget.model.AccountDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AccountDomainToAccountDTOMapper extends GenericMapper<Account, AccountDTO> {

    @Override
    default Class<Account> getSourceType(){
        return Account.class;
    }

    @Override
    default Class<AccountDTO> getTargetType(){
        return AccountDTO.class;
    }

    @Override
    AccountDTO map(Account source);
}
