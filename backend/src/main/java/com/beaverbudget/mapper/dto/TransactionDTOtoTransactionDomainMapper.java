package com.beaverbudget.mapper.dto;

import com.beaverbudget.exceptions.InvalidResourceException;
import com.beaverbudget.mapper.GenericMapper;
import com.beaverbudget.model.Transaction;
import com.beaverbudget.model.TransactionDTO;
import com.beaverbudget.model.TransactionType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

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

    @Mapping(target = "transactionType", expression = "java(getTransactionType(source.getType()))")
    @Override
    Transaction map(TransactionDTO source);

    default TransactionType getTransactionType(TransactionDTO.TypeEnum transactionType){
        if(TransactionDTO.TypeEnum.EXPENSE.equals(transactionType)){
            return TransactionType.EXPENSE;
        }else if(TransactionDTO.TypeEnum.INCOME.equals(transactionType)){
            return TransactionType.INCOME;
        }else{
            throw new InvalidResourceException("Invalid transaction type");
        }
    }
}
