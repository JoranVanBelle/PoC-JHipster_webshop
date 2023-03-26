package com.webshop.soa.transport.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import com.webshop.soa.transport.IntegrationTest;
import com.webshop.soa.transport.domain.Transport;
import com.webshop.soa.transport.repository.EntityManager;
import com.webshop.soa.transport.repository.TransportRepository;
import java.time.Duration;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;

/**
 * Integration tests for the {@link TransportResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class TransportResourceIT {

    private static final Long DEFAULT_TRANSPORT_ID = 1L;
    private static final Long UPDATED_TRANSPORT_ID = 2L;

    private static final String DEFAULT_TRANSPORT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_TRANSPORT_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/transports";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TransportRepository transportRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private Transport transport;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Transport createEntity(EntityManager em) {
        Transport transport = new Transport().transportID(DEFAULT_TRANSPORT_ID).transportName(DEFAULT_TRANSPORT_NAME);
        return transport;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Transport createUpdatedEntity(EntityManager em) {
        Transport transport = new Transport().transportID(UPDATED_TRANSPORT_ID).transportName(UPDATED_TRANSPORT_NAME);
        return transport;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(Transport.class).block();
        } catch (Exception e) {
            // It can fail, if other entities are still referring this - it will be removed later.
        }
    }

    @AfterEach
    public void cleanup() {
        deleteEntities(em);
    }

    @BeforeEach
    public void initTest() {
        deleteEntities(em);
        transport = createEntity(em);
    }

    @Test
    void createTransport() throws Exception {
        int databaseSizeBeforeCreate = transportRepository.findAll().collectList().block().size();
        // Create the Transport
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(transport))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the Transport in the database
        List<Transport> transportList = transportRepository.findAll().collectList().block();
        assertThat(transportList).hasSize(databaseSizeBeforeCreate + 1);
        Transport testTransport = transportList.get(transportList.size() - 1);
        assertThat(testTransport.getTransportID()).isEqualTo(DEFAULT_TRANSPORT_ID);
        assertThat(testTransport.getTransportName()).isEqualTo(DEFAULT_TRANSPORT_NAME);
    }

    @Test
    void createTransportWithExistingId() throws Exception {
        // Create the Transport with an existing ID
        transport.setId(1L);

        int databaseSizeBeforeCreate = transportRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(transport))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Transport in the database
        List<Transport> transportList = transportRepository.findAll().collectList().block();
        assertThat(transportList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkTransportIDIsRequired() throws Exception {
        int databaseSizeBeforeTest = transportRepository.findAll().collectList().block().size();
        // set the field null
        transport.setTransportID(null);

        // Create the Transport, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(transport))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Transport> transportList = transportRepository.findAll().collectList().block();
        assertThat(transportList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkTransportNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = transportRepository.findAll().collectList().block().size();
        // set the field null
        transport.setTransportName(null);

        // Create the Transport, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(transport))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Transport> transportList = transportRepository.findAll().collectList().block();
        assertThat(transportList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllTransportsAsStream() {
        // Initialize the database
        transportRepository.save(transport).block();

        List<Transport> transportList = webTestClient
            .get()
            .uri(ENTITY_API_URL)
            .accept(MediaType.APPLICATION_NDJSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentTypeCompatibleWith(MediaType.APPLICATION_NDJSON)
            .returnResult(Transport.class)
            .getResponseBody()
            .filter(transport::equals)
            .collectList()
            .block(Duration.ofSeconds(5));

        assertThat(transportList).isNotNull();
        assertThat(transportList).hasSize(1);
        Transport testTransport = transportList.get(0);
        assertThat(testTransport.getTransportID()).isEqualTo(DEFAULT_TRANSPORT_ID);
        assertThat(testTransport.getTransportName()).isEqualTo(DEFAULT_TRANSPORT_NAME);
    }

    @Test
    void getAllTransports() {
        // Initialize the database
        transportRepository.save(transport).block();

        // Get all the transportList
        webTestClient
            .get()
            .uri(ENTITY_API_URL + "?sort=id,desc")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.[*].id")
            .value(hasItem(transport.getId().intValue()))
            .jsonPath("$.[*].transportID")
            .value(hasItem(DEFAULT_TRANSPORT_ID.intValue()))
            .jsonPath("$.[*].transportName")
            .value(hasItem(DEFAULT_TRANSPORT_NAME));
    }

    @Test
    void getTransport() {
        // Initialize the database
        transportRepository.save(transport).block();

        // Get the transport
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, transport.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(transport.getId().intValue()))
            .jsonPath("$.transportID")
            .value(is(DEFAULT_TRANSPORT_ID.intValue()))
            .jsonPath("$.transportName")
            .value(is(DEFAULT_TRANSPORT_NAME));
    }

    @Test
    void getNonExistingTransport() {
        // Get the transport
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingTransport() throws Exception {
        // Initialize the database
        transportRepository.save(transport).block();

        int databaseSizeBeforeUpdate = transportRepository.findAll().collectList().block().size();

        // Update the transport
        Transport updatedTransport = transportRepository.findById(transport.getId()).block();
        updatedTransport.transportID(UPDATED_TRANSPORT_ID).transportName(UPDATED_TRANSPORT_NAME);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, updatedTransport.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(updatedTransport))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Transport in the database
        List<Transport> transportList = transportRepository.findAll().collectList().block();
        assertThat(transportList).hasSize(databaseSizeBeforeUpdate);
        Transport testTransport = transportList.get(transportList.size() - 1);
        assertThat(testTransport.getTransportID()).isEqualTo(UPDATED_TRANSPORT_ID);
        assertThat(testTransport.getTransportName()).isEqualTo(UPDATED_TRANSPORT_NAME);
    }

    @Test
    void putNonExistingTransport() throws Exception {
        int databaseSizeBeforeUpdate = transportRepository.findAll().collectList().block().size();
        transport.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, transport.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(transport))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Transport in the database
        List<Transport> transportList = transportRepository.findAll().collectList().block();
        assertThat(transportList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchTransport() throws Exception {
        int databaseSizeBeforeUpdate = transportRepository.findAll().collectList().block().size();
        transport.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(transport))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Transport in the database
        List<Transport> transportList = transportRepository.findAll().collectList().block();
        assertThat(transportList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamTransport() throws Exception {
        int databaseSizeBeforeUpdate = transportRepository.findAll().collectList().block().size();
        transport.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(transport))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Transport in the database
        List<Transport> transportList = transportRepository.findAll().collectList().block();
        assertThat(transportList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateTransportWithPatch() throws Exception {
        // Initialize the database
        transportRepository.save(transport).block();

        int databaseSizeBeforeUpdate = transportRepository.findAll().collectList().block().size();

        // Update the transport using partial update
        Transport partialUpdatedTransport = new Transport();
        partialUpdatedTransport.setId(transport.getId());

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedTransport.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedTransport))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Transport in the database
        List<Transport> transportList = transportRepository.findAll().collectList().block();
        assertThat(transportList).hasSize(databaseSizeBeforeUpdate);
        Transport testTransport = transportList.get(transportList.size() - 1);
        assertThat(testTransport.getTransportID()).isEqualTo(DEFAULT_TRANSPORT_ID);
        assertThat(testTransport.getTransportName()).isEqualTo(DEFAULT_TRANSPORT_NAME);
    }

    @Test
    void fullUpdateTransportWithPatch() throws Exception {
        // Initialize the database
        transportRepository.save(transport).block();

        int databaseSizeBeforeUpdate = transportRepository.findAll().collectList().block().size();

        // Update the transport using partial update
        Transport partialUpdatedTransport = new Transport();
        partialUpdatedTransport.setId(transport.getId());

        partialUpdatedTransport.transportID(UPDATED_TRANSPORT_ID).transportName(UPDATED_TRANSPORT_NAME);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedTransport.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedTransport))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Transport in the database
        List<Transport> transportList = transportRepository.findAll().collectList().block();
        assertThat(transportList).hasSize(databaseSizeBeforeUpdate);
        Transport testTransport = transportList.get(transportList.size() - 1);
        assertThat(testTransport.getTransportID()).isEqualTo(UPDATED_TRANSPORT_ID);
        assertThat(testTransport.getTransportName()).isEqualTo(UPDATED_TRANSPORT_NAME);
    }

    @Test
    void patchNonExistingTransport() throws Exception {
        int databaseSizeBeforeUpdate = transportRepository.findAll().collectList().block().size();
        transport.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, transport.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(transport))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Transport in the database
        List<Transport> transportList = transportRepository.findAll().collectList().block();
        assertThat(transportList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchTransport() throws Exception {
        int databaseSizeBeforeUpdate = transportRepository.findAll().collectList().block().size();
        transport.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(transport))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Transport in the database
        List<Transport> transportList = transportRepository.findAll().collectList().block();
        assertThat(transportList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamTransport() throws Exception {
        int databaseSizeBeforeUpdate = transportRepository.findAll().collectList().block().size();
        transport.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(transport))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Transport in the database
        List<Transport> transportList = transportRepository.findAll().collectList().block();
        assertThat(transportList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteTransport() {
        // Initialize the database
        transportRepository.save(transport).block();

        int databaseSizeBeforeDelete = transportRepository.findAll().collectList().block().size();

        // Delete the transport
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, transport.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<Transport> transportList = transportRepository.findAll().collectList().block();
        assertThat(transportList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
