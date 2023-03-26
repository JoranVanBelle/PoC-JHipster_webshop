package com.webshop.soa.pricing.repository;

import com.webshop.soa.pricing.domain.Pricing;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the Pricing entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PricingRepository extends ReactiveCrudRepository<Pricing, Long>, PricingRepositoryInternal {
    @Override
    <S extends Pricing> Mono<S> save(S entity);

    @Override
    Flux<Pricing> findAll();

    @Override
    Mono<Pricing> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface PricingRepositoryInternal {
    <S extends Pricing> Mono<S> save(S entity);

    Flux<Pricing> findAllBy(Pageable pageable);

    Flux<Pricing> findAll();

    Mono<Pricing> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<Pricing> findAllBy(Pageable pageable, Criteria criteria);

}
