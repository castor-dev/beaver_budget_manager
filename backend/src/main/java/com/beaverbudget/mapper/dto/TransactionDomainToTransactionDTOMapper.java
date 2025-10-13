package com.beaverbudget.mapper.dto;

import com.beaverbudget.mapper.GenericMapper;
import com.beaverbudget.model.Transaction;
import com.beaverbudget.model.TransactionDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TransactionDomainToTransactionDTOMapper extends GenericMapper<Transaction, TransactionDTO> {

    @Override
    default Class<Transaction> getSourceType(){
        return Transaction.class;
    }

    @Override
    default Class<TransactionDTO> getTargetType(){
        return TransactionDTO.class;
    }

    @Override
    TransactionDTO map(Transaction source);
}
