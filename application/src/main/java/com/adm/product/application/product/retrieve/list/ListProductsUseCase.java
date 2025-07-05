package com.adm.product.application.product.retrieve.list;

import com.adm.product.application.UseCase;
import com.adm.product.domain.pagination.Pagination;
import com.adm.product.domain.product.ProductSearchQuery;

public abstract class ListProductsUseCase extends UseCase<ProductSearchQuery, Pagination<ProductListOutPut>> {}
