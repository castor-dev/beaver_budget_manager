package com.beaverbudget.mapper;

public interface GenericMapper <S,T>{
    Class<S> getSourceType();
    Class<T> getTargetType();
    T map(S source);
}
