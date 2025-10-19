package com.beaverbudget.mapper.entity;

import com.beaverbudget.mapper.GenericMapper;
import com.beaverbudget.model.Transaction;
import com.beaverbudget.persistence.repository.entity.TransactionEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TransactionEntityToTransactionDomainMapper extends GenericMapper<TransactionEntity, Transaction> {

    @Override
    default Class<TransactionEntity> getSourceType() {
        return TransactionEntity.class;
    }

    @Override
    default Class<Transaction> getTargetType() {
        return Transaction.class;
    }

}
