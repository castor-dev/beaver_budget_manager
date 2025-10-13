package com.beaverbudget.mapper.dto;

import com.beaverbudget.mapper.GenericMapper;
import com.beaverbudget.model.Transaction;
import com.beaverbudget.model.TransactionDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TransactionDTOtoTransactionDomainMapper extends GenericMapper<TransactionDTO, Transaction> {

    @Override
    default Class<TransactionDTO> getSourceType(){
        return TransactionDTO.class;
    }

    @Override
    default Class<Transaction> getTargetType(){
        return Transaction.class;
    }

    @Override
    Transaction map(TransactionDTO source);
}
