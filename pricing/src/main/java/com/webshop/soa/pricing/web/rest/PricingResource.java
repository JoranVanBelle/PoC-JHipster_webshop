package com.webshop.soa.pricing.web.rest;

import com.webshop.soa.pricing.domain.Pricing;
import com.webshop.soa.pricing.repository.PricingRepository;
import com.webshop.soa.pricing.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.reactive.ResponseUtil;

/**
 * REST controller for managing {@link com.webshop.soa.pricing.domain.Pricing}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class PricingResource {

    private final Logger log = LoggerFactory.getLogger(PricingResource.class);

    private static final String ENTITY_NAME = "pricingPricing";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PricingRepository pricingRepository;

    public PricingResource(PricingRepository pricingRepository) {
        this.pricingRepository = pricingRepository;
    }

    /**
     * {@code POST  /pricings} : Create a new pricing.
     *
     * @param pricing the pricing to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new pricing, or with status {@code 400 (Bad Request)} if the pricing has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/pricings")
    public Mono<ResponseEntity<Pricing>> createPricing(@Valid @RequestBody Pricing pricing) throws URISyntaxException {
        log.debug("REST request to save Pricing : {}", pricing);
        if (pricing.getId() != null) {
            throw new BadRequestAlertException("A new pricing cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return pricingRepository
            .save(pricing)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/pricings/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /pricings/:id} : Updates an existing pricing.
     *
     * @param id the id of the pricing to save.
     * @param pricing the pricing to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated pricing,
     * or with status {@code 400 (Bad Request)} if the pricing is not valid,
     * or with status {@code 500 (Internal Server Error)} if the pricing couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/pricings/{id}")
    public Mono<ResponseEntity<Pricing>> updatePricing(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Pricing pricing
    ) throws URISyntaxException {
        log.debug("REST request to update Pricing : {}, {}", id, pricing);
        if (pricing.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, pricing.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return pricingRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return pricingRepository
                    .save(pricing)
                    .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                    .map(result ->
                        ResponseEntity
                            .ok()
                            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                            .body(result)
                    );
            });
    }

    /**
     * {@code PATCH  /pricings/:id} : Partial updates given fields of an existing pricing, field will ignore if it is null
     *
     * @param id the id of the pricing to save.
     * @param pricing the pricing to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated pricing,
     * or with status {@code 400 (Bad Request)} if the pricing is not valid,
     * or with status {@code 404 (Not Found)} if the pricing is not found,
     * or with status {@code 500 (Internal Server Error)} if the pricing couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/pricings/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<Pricing>> partialUpdatePricing(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Pricing pricing
    ) throws URISyntaxException {
        log.debug("REST request to partial update Pricing partially : {}, {}", id, pricing);
        if (pricing.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, pricing.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return pricingRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<Pricing> result = pricingRepository
                    .findById(pricing.getId())
                    .map(existingPricing -> {
                        if (pricing.getPricingID() != null) {
                            existingPricing.setPricingID(pricing.getPricingID());
                        }
                        if (pricing.getName() != null) {
                            existingPricing.setName(pricing.getName());
                        }
                        if (pricing.getPrice() != null) {
                            existingPricing.setPrice(pricing.getPrice());
                        }

                        return existingPricing;
                    })
                    .flatMap(pricingRepository::save);

                return result
                    .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                    .map(res ->
                        ResponseEntity
                            .ok()
                            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, res.getId().toString()))
                            .body(res)
                    );
            });
    }

    /**
     * {@code GET  /pricings} : get all the pricings.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of pricings in body.
     */
    @GetMapping("/pricings")
    public Mono<List<Pricing>> getAllPricings() {
        log.debug("REST request to get all Pricings");
        return pricingRepository.findAll().collectList();
    }

    /**
     * {@code GET  /pricings} : get all the pricings as a stream.
     * @return the {@link Flux} of pricings.
     */
    @GetMapping(value = "/pricings", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<Pricing> getAllPricingsAsStream() {
        log.debug("REST request to get all Pricings as a stream");
        return pricingRepository.findAll();
    }

    /**
     * {@code GET  /pricings/:id} : get the "id" pricing.
     *
     * @param id the id of the pricing to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the pricing, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/pricings/{id}")
    public Mono<ResponseEntity<Pricing>> getPricing(@PathVariable Long id) {
        log.debug("REST request to get Pricing : {}", id);
        Mono<Pricing> pricing = pricingRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(pricing);
    }

    /**
     * {@code DELETE  /pricings/:id} : delete the "id" pricing.
     *
     * @param id the id of the pricing to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/pricings/{id}")
    public Mono<ResponseEntity<Void>> deletePricing(@PathVariable Long id) {
        log.debug("REST request to delete Pricing : {}", id);
        return pricingRepository
            .deleteById(id)
            .then(
                Mono.just(
                    ResponseEntity
                        .noContent()
                        .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
                        .build()
                )
            );
    }
}
