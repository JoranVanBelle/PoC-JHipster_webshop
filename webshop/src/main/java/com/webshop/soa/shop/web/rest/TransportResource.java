package com.webshop.soa.shop.web.rest;

import com.webshop.soa.shop.domain.Transport;
import com.webshop.soa.shop.repository.TransportRepository;
import com.webshop.soa.shop.web.rest.errors.BadRequestAlertException;
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
 * REST controller for managing {@link com.webshop.soa.shop.domain.Transport}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class TransportResource {

    private final Logger log = LoggerFactory.getLogger(TransportResource.class);

    private static final String ENTITY_NAME = "transport";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TransportRepository transportRepository;

    public TransportResource(TransportRepository transportRepository) {
        this.transportRepository = transportRepository;
    }

    /**
     * {@code POST  /transports} : Create a new transport.
     *
     * @param transport the transport to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new transport, or with status {@code 400 (Bad Request)} if the transport has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/transports")
    public Mono<ResponseEntity<Transport>> createTransport(@Valid @RequestBody Transport transport) throws URISyntaxException {
        log.debug("REST request to save Transport : {}", transport);
        if (transport.getId() != null) {
            throw new BadRequestAlertException("A new transport cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return transportRepository
            .save(transport)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/transports/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /transports/:id} : Updates an existing transport.
     *
     * @param id the id of the transport to save.
     * @param transport the transport to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated transport,
     * or with status {@code 400 (Bad Request)} if the transport is not valid,
     * or with status {@code 500 (Internal Server Error)} if the transport couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/transports/{id}")
    public Mono<ResponseEntity<Transport>> updateTransport(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Transport transport
    ) throws URISyntaxException {
        log.debug("REST request to update Transport : {}, {}", id, transport);
        if (transport.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, transport.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return transportRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return transportRepository
                    .save(transport)
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
     * {@code PATCH  /transports/:id} : Partial updates given fields of an existing transport, field will ignore if it is null
     *
     * @param id the id of the transport to save.
     * @param transport the transport to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated transport,
     * or with status {@code 400 (Bad Request)} if the transport is not valid,
     * or with status {@code 404 (Not Found)} if the transport is not found,
     * or with status {@code 500 (Internal Server Error)} if the transport couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/transports/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<Transport>> partialUpdateTransport(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Transport transport
    ) throws URISyntaxException {
        log.debug("REST request to partial update Transport partially : {}, {}", id, transport);
        if (transport.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, transport.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return transportRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<Transport> result = transportRepository
                    .findById(transport.getId())
                    .map(existingTransport -> {
                        if (transport.getTransportID() != null) {
                            existingTransport.setTransportID(transport.getTransportID());
                        }
                        if (transport.getTransportName() != null) {
                            existingTransport.setTransportName(transport.getTransportName());
                        }

                        return existingTransport;
                    })
                    .flatMap(transportRepository::save);

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
     * {@code GET  /transports} : get all the transports.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of transports in body.
     */
    @GetMapping("/transports")
    public Mono<List<Transport>> getAllTransports() {
        log.debug("REST request to get all Transports");
        return transportRepository.findAll().collectList();
    }

    /**
     * {@code GET  /transports} : get all the transports as a stream.
     * @return the {@link Flux} of transports.
     */
    @GetMapping(value = "/transports", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<Transport> getAllTransportsAsStream() {
        log.debug("REST request to get all Transports as a stream");
        return transportRepository.findAll();
    }

    /**
     * {@code GET  /transports/:id} : get the "id" transport.
     *
     * @param id the id of the transport to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the transport, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/transports/{id}")
    public Mono<ResponseEntity<Transport>> getTransport(@PathVariable Long id) {
        log.debug("REST request to get Transport : {}", id);
        Mono<Transport> transport = transportRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(transport);
    }

    /**
     * {@code DELETE  /transports/:id} : delete the "id" transport.
     *
     * @param id the id of the transport to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/transports/{id}")
    public Mono<ResponseEntity<Void>> deleteTransport(@PathVariable Long id) {
        log.debug("REST request to delete Transport : {}", id);
        return transportRepository
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
