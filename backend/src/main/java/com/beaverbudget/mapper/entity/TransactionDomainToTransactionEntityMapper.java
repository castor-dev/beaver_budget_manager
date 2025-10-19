package com.beaverbudget.mapper.entity;

import com.beaverbudget.mapper.GenericMapper;
import com.beaverbudget.model.Transaction;
import com.beaverbudget.persistence.repository.entity.TransactionEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TransactionDomainToTransactionEntityMapper extends GenericMapper<Transaction, TransactionEntity> {

    @Override
    default Class<Transaction> getSourceType() {
        return Transaction.class;
    }

    @Override
    default Class<TransactionEntity> getTargetType(){
        return TransactionEntity.class;
    }

}
