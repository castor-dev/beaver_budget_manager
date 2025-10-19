package com.beaverbudget.mapper.dto;

import com.beaverbudget.exceptions.InvalidResourceException;
import com.beaverbudget.mapper.GenericMapper;
import com.beaverbudget.model.Transaction;
import com.beaverbudget.model.TransactionDTO;
import com.beaverbudget.model.TransactionType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.math.BigDecimal;

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

    @Mapping(target = "amount", expression = "java(source.getAmount().abs())")
    @Mapping(target = "type", expression = "java(getTransactionType(source.getTransactionType()))")
    @Override
    TransactionDTO map(Transaction source);

    default TransactionDTO.TypeEnum getTransactionType(TransactionType  transactionType){

        if(TransactionType.EXPENSE.equals(transactionType)){
            return TransactionDTO.TypeEnum.EXPENSE;
        }else if(TransactionType.INCOME.equals(transactionType)){
            return TransactionDTO.TypeEnum.INCOME;
        }else{
            throw new InvalidResourceException("Invalid transaction type");
        }

    }
}
