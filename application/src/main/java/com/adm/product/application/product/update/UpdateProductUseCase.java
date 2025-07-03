package com.adm.product.application.product.update;

import com.adm.product.application.Either;
import com.adm.product.application.UseCase;
import com.adm.product.domain.validation.handler.Notification;

public abstract class UpdateProductUseCase extends UseCase<UpdateProductCommand, Either<Notification,UpdateProductOutput>> {}
