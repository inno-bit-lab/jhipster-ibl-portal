package it.ibl.portal.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import it.ibl.portal.IntegrationTest;
import it.ibl.portal.domain.Ruolo;
import it.ibl.portal.repository.RuoloRepository;
import it.ibl.portal.repository.search.RuoloSearchRepository;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import org.apache.commons.collections4.IterableUtils;
import org.assertj.core.util.IterableUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Integration tests for the {@link RuoloResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class RuoloResourceIT {

    private static final LocalDate DEFAULT_CREATED = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CREATED = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_MODIFIED = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_MODIFIED = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_NOME_AZIONE = "AAAAAAAAAA";
    private static final String UPDATED_NOME_AZIONE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/ruolos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/ruolos/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private RuoloRepository ruoloRepository;

    @Autowired
    private RuoloSearchRepository ruoloSearchRepository;

    @Autowired
    private MockMvc restRuoloMockMvc;

    private Ruolo ruolo;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Ruolo createEntity() {
        Ruolo ruolo = new Ruolo().created(DEFAULT_CREATED).modified(DEFAULT_MODIFIED).nomeAzione(DEFAULT_NOME_AZIONE);
        return ruolo;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Ruolo createUpdatedEntity() {
        Ruolo ruolo = new Ruolo().created(UPDATED_CREATED).modified(UPDATED_MODIFIED).nomeAzione(UPDATED_NOME_AZIONE);
        return ruolo;
    }

    @AfterEach
    public void cleanupElasticSearchRepository() {
        ruoloSearchRepository.deleteAll();
        assertThat(ruoloSearchRepository.count()).isEqualTo(0);
    }

    @BeforeEach
    public void initTest() {
        ruoloRepository.deleteAll();
        ruolo = createEntity();
    }

    @Test
    void createRuolo() throws Exception {
        int databaseSizeBeforeCreate = ruoloRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(ruoloSearchRepository.findAll());
        // Create the Ruolo
        restRuoloMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ruolo))
            )
            .andExpect(status().isCreated());

        // Validate the Ruolo in the database
        List<Ruolo> ruoloList = ruoloRepository.findAll();
        assertThat(ruoloList).hasSize(databaseSizeBeforeCreate + 1);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(ruoloSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });
        Ruolo testRuolo = ruoloList.get(ruoloList.size() - 1);
        assertThat(testRuolo.getCreated()).isEqualTo(DEFAULT_CREATED);
        assertThat(testRuolo.getModified()).isEqualTo(DEFAULT_MODIFIED);
        assertThat(testRuolo.getNomeAzione()).isEqualTo(DEFAULT_NOME_AZIONE);
    }

    @Test
    void createRuoloWithExistingId() throws Exception {
        // Create the Ruolo with an existing ID
        ruolo.setId(1L);

        int databaseSizeBeforeCreate = ruoloRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(ruoloSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restRuoloMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ruolo))
            )
            .andExpect(status().isBadRequest());

        // Validate the Ruolo in the database
        List<Ruolo> ruoloList = ruoloRepository.findAll();
        assertThat(ruoloList).hasSize(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(ruoloSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void getAllRuolos() throws Exception {
        // Initialize the database
        ruoloRepository.save(ruolo);

        // Get all the ruoloList
        restRuoloMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ruolo.getId().intValue())))
            .andExpect(jsonPath("$.[*].created").value(hasItem(DEFAULT_CREATED.toString())))
            .andExpect(jsonPath("$.[*].modified").value(hasItem(DEFAULT_MODIFIED.toString())))
            .andExpect(jsonPath("$.[*].nomeAzione").value(hasItem(DEFAULT_NOME_AZIONE)));
    }

    @Test
    void getRuolo() throws Exception {
        // Initialize the database
        ruoloRepository.save(ruolo);

        // Get the ruolo
        restRuoloMockMvc
            .perform(get(ENTITY_API_URL_ID, ruolo.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(ruolo.getId().intValue()))
            .andExpect(jsonPath("$.created").value(DEFAULT_CREATED.toString()))
            .andExpect(jsonPath("$.modified").value(DEFAULT_MODIFIED.toString()))
            .andExpect(jsonPath("$.nomeAzione").value(DEFAULT_NOME_AZIONE));
    }

    @Test
    void getNonExistingRuolo() throws Exception {
        // Get the ruolo
        restRuoloMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    void putExistingRuolo() throws Exception {
        // Initialize the database
        ruoloRepository.save(ruolo);

        int databaseSizeBeforeUpdate = ruoloRepository.findAll().size();
        ruoloSearchRepository.save(ruolo);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(ruoloSearchRepository.findAll());

        // Update the ruolo
        Ruolo updatedRuolo = ruoloRepository.findById(ruolo.getId()).orElseThrow();
        updatedRuolo.created(UPDATED_CREATED).modified(UPDATED_MODIFIED).nomeAzione(UPDATED_NOME_AZIONE);

        restRuoloMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedRuolo.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedRuolo))
            )
            .andExpect(status().isOk());

        // Validate the Ruolo in the database
        List<Ruolo> ruoloList = ruoloRepository.findAll();
        assertThat(ruoloList).hasSize(databaseSizeBeforeUpdate);
        Ruolo testRuolo = ruoloList.get(ruoloList.size() - 1);
        assertThat(testRuolo.getCreated()).isEqualTo(UPDATED_CREATED);
        assertThat(testRuolo.getModified()).isEqualTo(UPDATED_MODIFIED);
        assertThat(testRuolo.getNomeAzione()).isEqualTo(UPDATED_NOME_AZIONE);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(ruoloSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<Ruolo> ruoloSearchList = IterableUtils.toList(ruoloSearchRepository.findAll());
                Ruolo testRuoloSearch = ruoloSearchList.get(searchDatabaseSizeAfter - 1);
                assertThat(testRuoloSearch.getCreated()).isEqualTo(UPDATED_CREATED);
                assertThat(testRuoloSearch.getModified()).isEqualTo(UPDATED_MODIFIED);
                assertThat(testRuoloSearch.getNomeAzione()).isEqualTo(UPDATED_NOME_AZIONE);
            });
    }

    @Test
    void putNonExistingRuolo() throws Exception {
        int databaseSizeBeforeUpdate = ruoloRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(ruoloSearchRepository.findAll());
        ruolo.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRuoloMockMvc
            .perform(
                put(ENTITY_API_URL_ID, ruolo.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ruolo))
            )
            .andExpect(status().isBadRequest());

        // Validate the Ruolo in the database
        List<Ruolo> ruoloList = ruoloRepository.findAll();
        assertThat(ruoloList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(ruoloSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void putWithIdMismatchRuolo() throws Exception {
        int databaseSizeBeforeUpdate = ruoloRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(ruoloSearchRepository.findAll());
        ruolo.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRuoloMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ruolo))
            )
            .andExpect(status().isBadRequest());

        // Validate the Ruolo in the database
        List<Ruolo> ruoloList = ruoloRepository.findAll();
        assertThat(ruoloList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(ruoloSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void putWithMissingIdPathParamRuolo() throws Exception {
        int databaseSizeBeforeUpdate = ruoloRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(ruoloSearchRepository.findAll());
        ruolo.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRuoloMockMvc
            .perform(
                put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ruolo))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Ruolo in the database
        List<Ruolo> ruoloList = ruoloRepository.findAll();
        assertThat(ruoloList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(ruoloSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void partialUpdateRuoloWithPatch() throws Exception {
        // Initialize the database
        ruoloRepository.save(ruolo);

        int databaseSizeBeforeUpdate = ruoloRepository.findAll().size();

        // Update the ruolo using partial update
        Ruolo partialUpdatedRuolo = new Ruolo();
        partialUpdatedRuolo.setId(ruolo.getId());

        restRuoloMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRuolo.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRuolo))
            )
            .andExpect(status().isOk());

        // Validate the Ruolo in the database
        List<Ruolo> ruoloList = ruoloRepository.findAll();
        assertThat(ruoloList).hasSize(databaseSizeBeforeUpdate);
        Ruolo testRuolo = ruoloList.get(ruoloList.size() - 1);
        assertThat(testRuolo.getCreated()).isEqualTo(DEFAULT_CREATED);
        assertThat(testRuolo.getModified()).isEqualTo(DEFAULT_MODIFIED);
        assertThat(testRuolo.getNomeAzione()).isEqualTo(DEFAULT_NOME_AZIONE);
    }

    @Test
    void fullUpdateRuoloWithPatch() throws Exception {
        // Initialize the database
        ruoloRepository.save(ruolo);

        int databaseSizeBeforeUpdate = ruoloRepository.findAll().size();

        // Update the ruolo using partial update
        Ruolo partialUpdatedRuolo = new Ruolo();
        partialUpdatedRuolo.setId(ruolo.getId());

        partialUpdatedRuolo.created(UPDATED_CREATED).modified(UPDATED_MODIFIED).nomeAzione(UPDATED_NOME_AZIONE);

        restRuoloMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRuolo.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRuolo))
            )
            .andExpect(status().isOk());

        // Validate the Ruolo in the database
        List<Ruolo> ruoloList = ruoloRepository.findAll();
        assertThat(ruoloList).hasSize(databaseSizeBeforeUpdate);
        Ruolo testRuolo = ruoloList.get(ruoloList.size() - 1);
        assertThat(testRuolo.getCreated()).isEqualTo(UPDATED_CREATED);
        assertThat(testRuolo.getModified()).isEqualTo(UPDATED_MODIFIED);
        assertThat(testRuolo.getNomeAzione()).isEqualTo(UPDATED_NOME_AZIONE);
    }

    @Test
    void patchNonExistingRuolo() throws Exception {
        int databaseSizeBeforeUpdate = ruoloRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(ruoloSearchRepository.findAll());
        ruolo.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRuoloMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, ruolo.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(ruolo))
            )
            .andExpect(status().isBadRequest());

        // Validate the Ruolo in the database
        List<Ruolo> ruoloList = ruoloRepository.findAll();
        assertThat(ruoloList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(ruoloSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void patchWithIdMismatchRuolo() throws Exception {
        int databaseSizeBeforeUpdate = ruoloRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(ruoloSearchRepository.findAll());
        ruolo.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRuoloMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(ruolo))
            )
            .andExpect(status().isBadRequest());

        // Validate the Ruolo in the database
        List<Ruolo> ruoloList = ruoloRepository.findAll();
        assertThat(ruoloList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(ruoloSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void patchWithMissingIdPathParamRuolo() throws Exception {
        int databaseSizeBeforeUpdate = ruoloRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(ruoloSearchRepository.findAll());
        ruolo.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRuoloMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(ruolo))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Ruolo in the database
        List<Ruolo> ruoloList = ruoloRepository.findAll();
        assertThat(ruoloList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(ruoloSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void deleteRuolo() throws Exception {
        // Initialize the database
        ruoloRepository.save(ruolo);
        ruoloRepository.save(ruolo);
        ruoloSearchRepository.save(ruolo);

        int databaseSizeBeforeDelete = ruoloRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(ruoloSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the ruolo
        restRuoloMockMvc
            .perform(delete(ENTITY_API_URL_ID, ruolo.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Ruolo> ruoloList = ruoloRepository.findAll();
        assertThat(ruoloList).hasSize(databaseSizeBeforeDelete - 1);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(ruoloSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    void searchRuolo() throws Exception {
        // Initialize the database
        ruolo = ruoloRepository.save(ruolo);
        ruoloSearchRepository.save(ruolo);

        // Search the ruolo
        restRuoloMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + ruolo.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ruolo.getId().intValue())))
            .andExpect(jsonPath("$.[*].created").value(hasItem(DEFAULT_CREATED.toString())))
            .andExpect(jsonPath("$.[*].modified").value(hasItem(DEFAULT_MODIFIED.toString())))
            .andExpect(jsonPath("$.[*].nomeAzione").value(hasItem(DEFAULT_NOME_AZIONE)));
    }
}
