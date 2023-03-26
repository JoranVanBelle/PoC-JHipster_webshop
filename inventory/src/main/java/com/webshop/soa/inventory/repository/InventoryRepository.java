package com.webshop.soa.inventory.repository;

import com.webshop.soa.inventory.domain.Inventory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the Inventory entity.
 */
@SuppressWarnings("unused")
@Repository
public interface InventoryRepository extends ReactiveCrudRepository<Inventory, Long>, InventoryRepositoryInternal {
    @Override
    <S extends Inventory> Mono<S> save(S entity);

    @Override
    Flux<Inventory> findAll();

    @Override
    Mono<Inventory> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface InventoryRepositoryInternal {
    <S extends Inventory> Mono<S> save(S entity);

    Flux<Inventory> findAllBy(Pageable pageable);

    Flux<Inventory> findAll();

    Mono<Inventory> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<Inventory> findAllBy(Pageable pageable, Criteria criteria);

}
