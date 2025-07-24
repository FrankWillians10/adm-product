package com.adm.product.infrastructure.product;


import com.adm.product.domain.pagination.Pagination;
import com.adm.product.domain.product.Product;
import com.adm.product.domain.product.ProductGateway;
import com.adm.product.domain.product.ProductID;
import com.adm.product.domain.product.ProductSearchQuery;
import com.adm.product.infrastructure.product.persistence.ProductJpaEntity;
import com.adm.product.infrastructure.product.persistence.ProductOutBox;
import com.adm.product.infrastructure.product.persistence.ProductOutBoxRepository;
import com.adm.product.infrastructure.product.persistence.ProductRepository;
import com.adm.product.infrastructure.utils.SpecificationUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

import static com.adm.product.infrastructure.utils.SpecificationUtils.like;

@Service
public class ProductMySQLGateway implements ProductGateway {

    private final ProductRepository repository;

    private final ProductOutBoxRepository outBoxRepository;

    public ProductMySQLGateway(final ProductRepository repository, final ProductOutBoxRepository outBoxRepository) {
        this.repository = repository;
        this.outBoxRepository = outBoxRepository;
    }

    @Transactional
    @Override
    public Product create(final Product aProduct) {
        final var newProduct = this.repository.save(ProductJpaEntity.from(aProduct)).toAggregate();
        this.outBoxRepository.saveAndFlush(ProductOutBox.fromProduct(aProduct));
        return newProduct;
    }

    @Override
    public void deleteById(final ProductID anId) {
        final String anIdValue = anId.getValue();
        if (this.repository.existsById(anIdValue)) {
            this.repository.deleteById(anIdValue);
        }
    }

    @Override
    public Optional<Product> findById(final ProductID anId) {
        return this.repository.findById(anId.getValue())
                .map(ProductJpaEntity::toAggregate);
    }

    @Override
    public Product update(final Product aProduct) {
        return this.repository.saveAndFlush(ProductJpaEntity.from(aProduct)).toAggregate();
    }

    @Override
    public Pagination<Product> findAll(final ProductSearchQuery aQuery) {
        final var page = PageRequest.of(
                aQuery.page(),
                aQuery.perPage(),
                Sort.by(Sort.Direction.fromString(aQuery.direction()), aQuery.sort())
        );

        final var specifications = Optional.ofNullable(aQuery.terms())
                .filter(str -> !str.isBlank())
                .map(str -> {
                    return SpecificationUtils
                            .<ProductJpaEntity> like("name", str)
                            .or(like("brand", str));
                })
                .orElse(null);

        final var pageResult =
                this.repository.findAll(Specification.where(specifications), page);
        return new Pagination<>(
                pageResult.getNumber(),
                pageResult.getSize(),
                pageResult.getTotalElements(),
                pageResult.map(ProductJpaEntity::toAggregate).toList()
        );
    }
}
