package com.adm.product.application;

public abstract class UseCase<IN, OUT> {

    public abstract OUT execute(IN anIn);
}