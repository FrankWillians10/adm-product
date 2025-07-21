package com.adm.product.application.product.retrieve.get;

import com.adm.product.application.Either;
import com.adm.product.application.UseCase;
import com.adm.product.domain.exceptions.DomainException;

public abstract class GetProductByIdUseCase extends UseCase<String, Either<DomainException, ProductOutPut>> {}
