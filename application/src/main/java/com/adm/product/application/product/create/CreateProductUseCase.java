package com.adm.product.application.product.create;

import com.adm.product.application.UseCase;
import com.adm.product.domain.validation.handler.Notification;

public abstract class CreateProductUseCase extends UseCase<CreateProductCommand, Either<Notification, CreateProductOutput>> {
}
