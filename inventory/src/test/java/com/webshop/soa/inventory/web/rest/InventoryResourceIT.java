package com.webshop.soa.inventory.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import com.webshop.soa.inventory.IntegrationTest;
import com.webshop.soa.inventory.domain.Inventory;
import com.webshop.soa.inventory.repository.EntityManager;
import com.webshop.soa.inventory.repository.InventoryRepository;
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
 * Integration tests for the {@link InventoryResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class InventoryResourceIT {

    private static final Long DEFAULT_INVENTORY_ID = 1L;
    private static final Long UPDATED_INVENTORY_ID = 2L;

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Long DEFAULT_QUANTITY = 1L;
    private static final Long UPDATED_QUANTITY = 2L;

    private static final String ENTITY_API_URL = "/api/inventories";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private InventoryRepository inventoryRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private Inventory inventory;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Inventory createEntity(EntityManager em) {
        Inventory inventory = new Inventory().inventoryID(DEFAULT_INVENTORY_ID).name(DEFAULT_NAME).quantity(DEFAULT_QUANTITY);
        return inventory;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Inventory createUpdatedEntity(EntityManager em) {
        Inventory inventory = new Inventory().inventoryID(UPDATED_INVENTORY_ID).name(UPDATED_NAME).quantity(UPDATED_QUANTITY);
        return inventory;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(Inventory.class).block();
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
        inventory = createEntity(em);
    }

    @Test
    void createInventory() throws Exception {
        int databaseSizeBeforeCreate = inventoryRepository.findAll().collectList().block().size();
        // Create the Inventory
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(inventory))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the Inventory in the database
        List<Inventory> inventoryList = inventoryRepository.findAll().collectList().block();
        assertThat(inventoryList).hasSize(databaseSizeBeforeCreate + 1);
        Inventory testInventory = inventoryList.get(inventoryList.size() - 1);
        assertThat(testInventory.getInventoryID()).isEqualTo(DEFAULT_INVENTORY_ID);
        assertThat(testInventory.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testInventory.getQuantity()).isEqualTo(DEFAULT_QUANTITY);
    }

    @Test
    void createInventoryWithExistingId() throws Exception {
        // Create the Inventory with an existing ID
        inventory.setId(1L);

        int databaseSizeBeforeCreate = inventoryRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(inventory))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Inventory in the database
        List<Inventory> inventoryList = inventoryRepository.findAll().collectList().block();
        assertThat(inventoryList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkInventoryIDIsRequired() throws Exception {
        int databaseSizeBeforeTest = inventoryRepository.findAll().collectList().block().size();
        // set the field null
        inventory.setInventoryID(null);

        // Create the Inventory, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(inventory))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Inventory> inventoryList = inventoryRepository.findAll().collectList().block();
        assertThat(inventoryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = inventoryRepository.findAll().collectList().block().size();
        // set the field null
        inventory.setName(null);

        // Create the Inventory, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(inventory))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Inventory> inventoryList = inventoryRepository.findAll().collectList().block();
        assertThat(inventoryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkQuantityIsRequired() throws Exception {
        int databaseSizeBeforeTest = inventoryRepository.findAll().collectList().block().size();
        // set the field null
        inventory.setQuantity(null);

        // Create the Inventory, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(inventory))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Inventory> inventoryList = inventoryRepository.findAll().collectList().block();
        assertThat(inventoryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllInventoriesAsStream() {
        // Initialize the database
        inventoryRepository.save(inventory).block();

        List<Inventory> inventoryList = webTestClient
            .get()
            .uri(ENTITY_API_URL)
            .accept(MediaType.APPLICATION_NDJSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentTypeCompatibleWith(MediaType.APPLICATION_NDJSON)
            .returnResult(Inventory.class)
            .getResponseBody()
            .filter(inventory::equals)
            .collectList()
            .block(Duration.ofSeconds(5));

        assertThat(inventoryList).isNotNull();
        assertThat(inventoryList).hasSize(1);
        Inventory testInventory = inventoryList.get(0);
        assertThat(testInventory.getInventoryID()).isEqualTo(DEFAULT_INVENTORY_ID);
        assertThat(testInventory.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testInventory.getQuantity()).isEqualTo(DEFAULT_QUANTITY);
    }

    @Test
    void getAllInventories() {
        // Initialize the database
        inventoryRepository.save(inventory).block();

        // Get all the inventoryList
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
            .value(hasItem(inventory.getId().intValue()))
            .jsonPath("$.[*].inventoryID")
            .value(hasItem(DEFAULT_INVENTORY_ID.intValue()))
            .jsonPath("$.[*].name")
            .value(hasItem(DEFAULT_NAME))
            .jsonPath("$.[*].quantity")
            .value(hasItem(DEFAULT_QUANTITY.intValue()));
    }

    @Test
    void getInventory() {
        // Initialize the database
        inventoryRepository.save(inventory).block();

        // Get the inventory
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, inventory.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(inventory.getId().intValue()))
            .jsonPath("$.inventoryID")
            .value(is(DEFAULT_INVENTORY_ID.intValue()))
            .jsonPath("$.name")
            .value(is(DEFAULT_NAME))
            .jsonPath("$.quantity")
            .value(is(DEFAULT_QUANTITY.intValue()));
    }

    @Test
    void getNonExistingInventory() {
        // Get the inventory
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingInventory() throws Exception {
        // Initialize the database
        inventoryRepository.save(inventory).block();

        int databaseSizeBeforeUpdate = inventoryRepository.findAll().collectList().block().size();

        // Update the inventory
        Inventory updatedInventory = inventoryRepository.findById(inventory.getId()).block();
        updatedInventory.inventoryID(UPDATED_INVENTORY_ID).name(UPDATED_NAME).quantity(UPDATED_QUANTITY);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, updatedInventory.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(updatedInventory))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Inventory in the database
        List<Inventory> inventoryList = inventoryRepository.findAll().collectList().block();
        assertThat(inventoryList).hasSize(databaseSizeBeforeUpdate);
        Inventory testInventory = inventoryList.get(inventoryList.size() - 1);
        assertThat(testInventory.getInventoryID()).isEqualTo(UPDATED_INVENTORY_ID);
        assertThat(testInventory.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testInventory.getQuantity()).isEqualTo(UPDATED_QUANTITY);
    }

    @Test
    void putNonExistingInventory() throws Exception {
        int databaseSizeBeforeUpdate = inventoryRepository.findAll().collectList().block().size();
        inventory.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, inventory.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(inventory))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Inventory in the database
        List<Inventory> inventoryList = inventoryRepository.findAll().collectList().block();
        assertThat(inventoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchInventory() throws Exception {
        int databaseSizeBeforeUpdate = inventoryRepository.findAll().collectList().block().size();
        inventory.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(inventory))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Inventory in the database
        List<Inventory> inventoryList = inventoryRepository.findAll().collectList().block();
        assertThat(inventoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamInventory() throws Exception {
        int databaseSizeBeforeUpdate = inventoryRepository.findAll().collectList().block().size();
        inventory.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(inventory))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Inventory in the database
        List<Inventory> inventoryList = inventoryRepository.findAll().collectList().block();
        assertThat(inventoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateInventoryWithPatch() throws Exception {
        // Initialize the database
        inventoryRepository.save(inventory).block();

        int databaseSizeBeforeUpdate = inventoryRepository.findAll().collectList().block().size();

        // Update the inventory using partial update
        Inventory partialUpdatedInventory = new Inventory();
        partialUpdatedInventory.setId(inventory.getId());

        partialUpdatedInventory.name(UPDATED_NAME).quantity(UPDATED_QUANTITY);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedInventory.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedInventory))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Inventory in the database
        List<Inventory> inventoryList = inventoryRepository.findAll().collectList().block();
        assertThat(inventoryList).hasSize(databaseSizeBeforeUpdate);
        Inventory testInventory = inventoryList.get(inventoryList.size() - 1);
        assertThat(testInventory.getInventoryID()).isEqualTo(DEFAULT_INVENTORY_ID);
        assertThat(testInventory.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testInventory.getQuantity()).isEqualTo(UPDATED_QUANTITY);
    }

    @Test
    void fullUpdateInventoryWithPatch() throws Exception {
        // Initialize the database
        inventoryRepository.save(inventory).block();

        int databaseSizeBeforeUpdate = inventoryRepository.findAll().collectList().block().size();

        // Update the inventory using partial update
        Inventory partialUpdatedInventory = new Inventory();
        partialUpdatedInventory.setId(inventory.getId());

        partialUpdatedInventory.inventoryID(UPDATED_INVENTORY_ID).name(UPDATED_NAME).quantity(UPDATED_QUANTITY);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedInventory.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedInventory))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Inventory in the database
        List<Inventory> inventoryList = inventoryRepository.findAll().collectList().block();
        assertThat(inventoryList).hasSize(databaseSizeBeforeUpdate);
        Inventory testInventory = inventoryList.get(inventoryList.size() - 1);
        assertThat(testInventory.getInventoryID()).isEqualTo(UPDATED_INVENTORY_ID);
        assertThat(testInventory.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testInventory.getQuantity()).isEqualTo(UPDATED_QUANTITY);
    }

    @Test
    void patchNonExistingInventory() throws Exception {
        int databaseSizeBeforeUpdate = inventoryRepository.findAll().collectList().block().size();
        inventory.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, inventory.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(inventory))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Inventory in the database
        List<Inventory> inventoryList = inventoryRepository.findAll().collectList().block();
        assertThat(inventoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchInventory() throws Exception {
        int databaseSizeBeforeUpdate = inventoryRepository.findAll().collectList().block().size();
        inventory.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(inventory))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Inventory in the database
        List<Inventory> inventoryList = inventoryRepository.findAll().collectList().block();
        assertThat(inventoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamInventory() throws Exception {
        int databaseSizeBeforeUpdate = inventoryRepository.findAll().collectList().block().size();
        inventory.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(inventory))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Inventory in the database
        List<Inventory> inventoryList = inventoryRepository.findAll().collectList().block();
        assertThat(inventoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteInventory() {
        // Initialize the database
        inventoryRepository.save(inventory).block();

        int databaseSizeBeforeDelete = inventoryRepository.findAll().collectList().block().size();

        // Delete the inventory
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, inventory.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<Inventory> inventoryList = inventoryRepository.findAll().collectList().block();
        assertThat(inventoryList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
