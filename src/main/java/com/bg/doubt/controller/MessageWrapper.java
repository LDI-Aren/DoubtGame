package com.bg.doubt.controller;

@FunctionalInterface
public interface MessageWrapper<T, U, R> {

    R apply(T t, U u) throws Exception;
}
