package com.webshop.soa.pricing.web.rest;

import static com.webshop.soa.pricing.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import com.webshop.soa.pricing.IntegrationTest;
import com.webshop.soa.pricing.domain.Pricing;
import com.webshop.soa.pricing.repository.EntityManager;
import com.webshop.soa.pricing.repository.PricingRepository;
import java.math.BigDecimal;
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
 * Integration tests for the {@link PricingResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class PricingResourceIT {

    private static final Long DEFAULT_PRICING_ID = 1L;
    private static final Long UPDATED_PRICING_ID = 2L;

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_PRICE = new BigDecimal(0);
    private static final BigDecimal UPDATED_PRICE = new BigDecimal(1);

    private static final String ENTITY_API_URL = "/api/pricings";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PricingRepository pricingRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private Pricing pricing;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Pricing createEntity(EntityManager em) {
        Pricing pricing = new Pricing().pricingID(DEFAULT_PRICING_ID).name(DEFAULT_NAME).price(DEFAULT_PRICE);
        return pricing;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Pricing createUpdatedEntity(EntityManager em) {
        Pricing pricing = new Pricing().pricingID(UPDATED_PRICING_ID).name(UPDATED_NAME).price(UPDATED_PRICE);
        return pricing;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(Pricing.class).block();
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
        pricing = createEntity(em);
    }

    @Test
    void createPricing() throws Exception {
        int databaseSizeBeforeCreate = pricingRepository.findAll().collectList().block().size();
        // Create the Pricing
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(pricing))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the Pricing in the database
        List<Pricing> pricingList = pricingRepository.findAll().collectList().block();
        assertThat(pricingList).hasSize(databaseSizeBeforeCreate + 1);
        Pricing testPricing = pricingList.get(pricingList.size() - 1);
        assertThat(testPricing.getPricingID()).isEqualTo(DEFAULT_PRICING_ID);
        assertThat(testPricing.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testPricing.getPrice()).isEqualByComparingTo(DEFAULT_PRICE);
    }

    @Test
    void createPricingWithExistingId() throws Exception {
        // Create the Pricing with an existing ID
        pricing.setId(1L);

        int databaseSizeBeforeCreate = pricingRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(pricing))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Pricing in the database
        List<Pricing> pricingList = pricingRepository.findAll().collectList().block();
        assertThat(pricingList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkPricingIDIsRequired() throws Exception {
        int databaseSizeBeforeTest = pricingRepository.findAll().collectList().block().size();
        // set the field null
        pricing.setPricingID(null);

        // Create the Pricing, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(pricing))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Pricing> pricingList = pricingRepository.findAll().collectList().block();
        assertThat(pricingList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = pricingRepository.findAll().collectList().block().size();
        // set the field null
        pricing.setName(null);

        // Create the Pricing, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(pricing))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Pricing> pricingList = pricingRepository.findAll().collectList().block();
        assertThat(pricingList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkPriceIsRequired() throws Exception {
        int databaseSizeBeforeTest = pricingRepository.findAll().collectList().block().size();
        // set the field null
        pricing.setPrice(null);

        // Create the Pricing, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(pricing))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Pricing> pricingList = pricingRepository.findAll().collectList().block();
        assertThat(pricingList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllPricingsAsStream() {
        // Initialize the database
        pricingRepository.save(pricing).block();

        List<Pricing> pricingList = webTestClient
            .get()
            .uri(ENTITY_API_URL)
            .accept(MediaType.APPLICATION_NDJSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentTypeCompatibleWith(MediaType.APPLICATION_NDJSON)
            .returnResult(Pricing.class)
            .getResponseBody()
            .filter(pricing::equals)
            .collectList()
            .block(Duration.ofSeconds(5));

        assertThat(pricingList).isNotNull();
        assertThat(pricingList).hasSize(1);
        Pricing testPricing = pricingList.get(0);
        assertThat(testPricing.getPricingID()).isEqualTo(DEFAULT_PRICING_ID);
        assertThat(testPricing.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testPricing.getPrice()).isEqualByComparingTo(DEFAULT_PRICE);
    }

    @Test
    void getAllPricings() {
        // Initialize the database
        pricingRepository.save(pricing).block();

        // Get all the pricingList
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
            .value(hasItem(pricing.getId().intValue()))
            .jsonPath("$.[*].pricingID")
            .value(hasItem(DEFAULT_PRICING_ID.intValue()))
            .jsonPath("$.[*].name")
            .value(hasItem(DEFAULT_NAME))
            .jsonPath("$.[*].price")
            .value(hasItem(sameNumber(DEFAULT_PRICE)));
    }

    @Test
    void getPricing() {
        // Initialize the database
        pricingRepository.save(pricing).block();

        // Get the pricing
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, pricing.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(pricing.getId().intValue()))
            .jsonPath("$.pricingID")
            .value(is(DEFAULT_PRICING_ID.intValue()))
            .jsonPath("$.name")
            .value(is(DEFAULT_NAME))
            .jsonPath("$.price")
            .value(is(sameNumber(DEFAULT_PRICE)));
    }

    @Test
    void getNonExistingPricing() {
        // Get the pricing
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingPricing() throws Exception {
        // Initialize the database
        pricingRepository.save(pricing).block();

        int databaseSizeBeforeUpdate = pricingRepository.findAll().collectList().block().size();

        // Update the pricing
        Pricing updatedPricing = pricingRepository.findById(pricing.getId()).block();
        updatedPricing.pricingID(UPDATED_PRICING_ID).name(UPDATED_NAME).price(UPDATED_PRICE);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, updatedPricing.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(updatedPricing))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Pricing in the database
        List<Pricing> pricingList = pricingRepository.findAll().collectList().block();
        assertThat(pricingList).hasSize(databaseSizeBeforeUpdate);
        Pricing testPricing = pricingList.get(pricingList.size() - 1);
        assertThat(testPricing.getPricingID()).isEqualTo(UPDATED_PRICING_ID);
        assertThat(testPricing.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testPricing.getPrice()).isEqualByComparingTo(UPDATED_PRICE);
    }

    @Test
    void putNonExistingPricing() throws Exception {
        int databaseSizeBeforeUpdate = pricingRepository.findAll().collectList().block().size();
        pricing.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, pricing.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(pricing))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Pricing in the database
        List<Pricing> pricingList = pricingRepository.findAll().collectList().block();
        assertThat(pricingList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchPricing() throws Exception {
        int databaseSizeBeforeUpdate = pricingRepository.findAll().collectList().block().size();
        pricing.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(pricing))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Pricing in the database
        List<Pricing> pricingList = pricingRepository.findAll().collectList().block();
        assertThat(pricingList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamPricing() throws Exception {
        int databaseSizeBeforeUpdate = pricingRepository.findAll().collectList().block().size();
        pricing.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(pricing))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Pricing in the database
        List<Pricing> pricingList = pricingRepository.findAll().collectList().block();
        assertThat(pricingList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdatePricingWithPatch() throws Exception {
        // Initialize the database
        pricingRepository.save(pricing).block();

        int databaseSizeBeforeUpdate = pricingRepository.findAll().collectList().block().size();

        // Update the pricing using partial update
        Pricing partialUpdatedPricing = new Pricing();
        partialUpdatedPricing.setId(pricing.getId());

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedPricing.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedPricing))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Pricing in the database
        List<Pricing> pricingList = pricingRepository.findAll().collectList().block();
        assertThat(pricingList).hasSize(databaseSizeBeforeUpdate);
        Pricing testPricing = pricingList.get(pricingList.size() - 1);
        assertThat(testPricing.getPricingID()).isEqualTo(DEFAULT_PRICING_ID);
        assertThat(testPricing.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testPricing.getPrice()).isEqualByComparingTo(DEFAULT_PRICE);
    }

    @Test
    void fullUpdatePricingWithPatch() throws Exception {
        // Initialize the database
        pricingRepository.save(pricing).block();

        int databaseSizeBeforeUpdate = pricingRepository.findAll().collectList().block().size();

        // Update the pricing using partial update
        Pricing partialUpdatedPricing = new Pricing();
        partialUpdatedPricing.setId(pricing.getId());

        partialUpdatedPricing.pricingID(UPDATED_PRICING_ID).name(UPDATED_NAME).price(UPDATED_PRICE);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedPricing.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedPricing))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Pricing in the database
        List<Pricing> pricingList = pricingRepository.findAll().collectList().block();
        assertThat(pricingList).hasSize(databaseSizeBeforeUpdate);
        Pricing testPricing = pricingList.get(pricingList.size() - 1);
        assertThat(testPricing.getPricingID()).isEqualTo(UPDATED_PRICING_ID);
        assertThat(testPricing.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testPricing.getPrice()).isEqualByComparingTo(UPDATED_PRICE);
    }

    @Test
    void patchNonExistingPricing() throws Exception {
        int databaseSizeBeforeUpdate = pricingRepository.findAll().collectList().block().size();
        pricing.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, pricing.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(pricing))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Pricing in the database
        List<Pricing> pricingList = pricingRepository.findAll().collectList().block();
        assertThat(pricingList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchPricing() throws Exception {
        int databaseSizeBeforeUpdate = pricingRepository.findAll().collectList().block().size();
        pricing.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(pricing))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Pricing in the database
        List<Pricing> pricingList = pricingRepository.findAll().collectList().block();
        assertThat(pricingList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamPricing() throws Exception {
        int databaseSizeBeforeUpdate = pricingRepository.findAll().collectList().block().size();
        pricing.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(pricing))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Pricing in the database
        List<Pricing> pricingList = pricingRepository.findAll().collectList().block();
        assertThat(pricingList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deletePricing() {
        // Initialize the database
        pricingRepository.save(pricing).block();

        int databaseSizeBeforeDelete = pricingRepository.findAll().collectList().block().size();

        // Delete the pricing
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, pricing.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<Pricing> pricingList = pricingRepository.findAll().collectList().block();
        assertThat(pricingList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
