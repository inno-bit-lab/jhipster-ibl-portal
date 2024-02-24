package it.ibl.portal.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import it.ibl.portal.IntegrationTest;
import it.ibl.portal.domain.Azione;
import it.ibl.portal.repository.AzioneRepository;
import it.ibl.portal.repository.search.AzioneSearchRepository;
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
 * Integration tests for the {@link AzioneResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AzioneResourceIT {

    private static final LocalDate DEFAULT_CREATED = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CREATED = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_MODIFIED = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_MODIFIED = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_NOME_AZIONE = "AAAAAAAAAA";
    private static final String UPDATED_NOME_AZIONE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIZIONE = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIZIONE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/aziones";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/aziones/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private AzioneRepository azioneRepository;

    @Autowired
    private AzioneSearchRepository azioneSearchRepository;

    @Autowired
    private MockMvc restAzioneMockMvc;

    private Azione azione;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Azione createEntity() {
        Azione azione = new Azione()
            .created(DEFAULT_CREATED)
            .modified(DEFAULT_MODIFIED)
            .nomeAzione(DEFAULT_NOME_AZIONE)
            .descrizione(DEFAULT_DESCRIZIONE);
        return azione;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Azione createUpdatedEntity() {
        Azione azione = new Azione()
            .created(UPDATED_CREATED)
            .modified(UPDATED_MODIFIED)
            .nomeAzione(UPDATED_NOME_AZIONE)
            .descrizione(UPDATED_DESCRIZIONE);
        return azione;
    }

    @AfterEach
    public void cleanupElasticSearchRepository() {
        azioneSearchRepository.deleteAll();
        assertThat(azioneSearchRepository.count()).isEqualTo(0);
    }

    @BeforeEach
    public void initTest() {
        azioneRepository.deleteAll();
        azione = createEntity();
    }

    @Test
    void createAzione() throws Exception {
        int databaseSizeBeforeCreate = azioneRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(azioneSearchRepository.findAll());
        // Create the Azione
        restAzioneMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(azione))
            )
            .andExpect(status().isCreated());

        // Validate the Azione in the database
        List<Azione> azioneList = azioneRepository.findAll();
        assertThat(azioneList).hasSize(databaseSizeBeforeCreate + 1);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(azioneSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });
        Azione testAzione = azioneList.get(azioneList.size() - 1);
        assertThat(testAzione.getCreated()).isEqualTo(DEFAULT_CREATED);
        assertThat(testAzione.getModified()).isEqualTo(DEFAULT_MODIFIED);
        assertThat(testAzione.getNomeAzione()).isEqualTo(DEFAULT_NOME_AZIONE);
        assertThat(testAzione.getDescrizione()).isEqualTo(DEFAULT_DESCRIZIONE);
    }

    @Test
    void createAzioneWithExistingId() throws Exception {
        // Create the Azione with an existing ID
        azione.setId(1L);

        int databaseSizeBeforeCreate = azioneRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(azioneSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restAzioneMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(azione))
            )
            .andExpect(status().isBadRequest());

        // Validate the Azione in the database
        List<Azione> azioneList = azioneRepository.findAll();
        assertThat(azioneList).hasSize(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(azioneSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void getAllAziones() throws Exception {
        // Initialize the database
        azioneRepository.save(azione);

        // Get all the azioneList
        restAzioneMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(azione.getId().intValue())))
            .andExpect(jsonPath("$.[*].created").value(hasItem(DEFAULT_CREATED.toString())))
            .andExpect(jsonPath("$.[*].modified").value(hasItem(DEFAULT_MODIFIED.toString())))
            .andExpect(jsonPath("$.[*].nomeAzione").value(hasItem(DEFAULT_NOME_AZIONE)))
            .andExpect(jsonPath("$.[*].descrizione").value(hasItem(DEFAULT_DESCRIZIONE)));
    }

    @Test
    void getAzione() throws Exception {
        // Initialize the database
        azioneRepository.save(azione);

        // Get the azione
        restAzioneMockMvc
            .perform(get(ENTITY_API_URL_ID, azione.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(azione.getId().intValue()))
            .andExpect(jsonPath("$.created").value(DEFAULT_CREATED.toString()))
            .andExpect(jsonPath("$.modified").value(DEFAULT_MODIFIED.toString()))
            .andExpect(jsonPath("$.nomeAzione").value(DEFAULT_NOME_AZIONE))
            .andExpect(jsonPath("$.descrizione").value(DEFAULT_DESCRIZIONE));
    }

    @Test
    void getNonExistingAzione() throws Exception {
        // Get the azione
        restAzioneMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    void putExistingAzione() throws Exception {
        // Initialize the database
        azioneRepository.save(azione);

        int databaseSizeBeforeUpdate = azioneRepository.findAll().size();
        azioneSearchRepository.save(azione);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(azioneSearchRepository.findAll());

        // Update the azione
        Azione updatedAzione = azioneRepository.findById(azione.getId()).orElseThrow();
        updatedAzione.created(UPDATED_CREATED).modified(UPDATED_MODIFIED).nomeAzione(UPDATED_NOME_AZIONE).descrizione(UPDATED_DESCRIZIONE);

        restAzioneMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedAzione.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedAzione))
            )
            .andExpect(status().isOk());

        // Validate the Azione in the database
        List<Azione> azioneList = azioneRepository.findAll();
        assertThat(azioneList).hasSize(databaseSizeBeforeUpdate);
        Azione testAzione = azioneList.get(azioneList.size() - 1);
        assertThat(testAzione.getCreated()).isEqualTo(UPDATED_CREATED);
        assertThat(testAzione.getModified()).isEqualTo(UPDATED_MODIFIED);
        assertThat(testAzione.getNomeAzione()).isEqualTo(UPDATED_NOME_AZIONE);
        assertThat(testAzione.getDescrizione()).isEqualTo(UPDATED_DESCRIZIONE);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(azioneSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<Azione> azioneSearchList = IterableUtils.toList(azioneSearchRepository.findAll());
                Azione testAzioneSearch = azioneSearchList.get(searchDatabaseSizeAfter - 1);
                assertThat(testAzioneSearch.getCreated()).isEqualTo(UPDATED_CREATED);
                assertThat(testAzioneSearch.getModified()).isEqualTo(UPDATED_MODIFIED);
                assertThat(testAzioneSearch.getNomeAzione()).isEqualTo(UPDATED_NOME_AZIONE);
                assertThat(testAzioneSearch.getDescrizione()).isEqualTo(UPDATED_DESCRIZIONE);
            });
    }

    @Test
    void putNonExistingAzione() throws Exception {
        int databaseSizeBeforeUpdate = azioneRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(azioneSearchRepository.findAll());
        azione.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAzioneMockMvc
            .perform(
                put(ENTITY_API_URL_ID, azione.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(azione))
            )
            .andExpect(status().isBadRequest());

        // Validate the Azione in the database
        List<Azione> azioneList = azioneRepository.findAll();
        assertThat(azioneList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(azioneSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void putWithIdMismatchAzione() throws Exception {
        int databaseSizeBeforeUpdate = azioneRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(azioneSearchRepository.findAll());
        azione.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAzioneMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(azione))
            )
            .andExpect(status().isBadRequest());

        // Validate the Azione in the database
        List<Azione> azioneList = azioneRepository.findAll();
        assertThat(azioneList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(azioneSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void putWithMissingIdPathParamAzione() throws Exception {
        int databaseSizeBeforeUpdate = azioneRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(azioneSearchRepository.findAll());
        azione.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAzioneMockMvc
            .perform(
                put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(azione))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Azione in the database
        List<Azione> azioneList = azioneRepository.findAll();
        assertThat(azioneList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(azioneSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void partialUpdateAzioneWithPatch() throws Exception {
        // Initialize the database
        azioneRepository.save(azione);

        int databaseSizeBeforeUpdate = azioneRepository.findAll().size();

        // Update the azione using partial update
        Azione partialUpdatedAzione = new Azione();
        partialUpdatedAzione.setId(azione.getId());

        partialUpdatedAzione.nomeAzione(UPDATED_NOME_AZIONE).descrizione(UPDATED_DESCRIZIONE);

        restAzioneMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAzione.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAzione))
            )
            .andExpect(status().isOk());

        // Validate the Azione in the database
        List<Azione> azioneList = azioneRepository.findAll();
        assertThat(azioneList).hasSize(databaseSizeBeforeUpdate);
        Azione testAzione = azioneList.get(azioneList.size() - 1);
        assertThat(testAzione.getCreated()).isEqualTo(DEFAULT_CREATED);
        assertThat(testAzione.getModified()).isEqualTo(DEFAULT_MODIFIED);
        assertThat(testAzione.getNomeAzione()).isEqualTo(UPDATED_NOME_AZIONE);
        assertThat(testAzione.getDescrizione()).isEqualTo(UPDATED_DESCRIZIONE);
    }

    @Test
    void fullUpdateAzioneWithPatch() throws Exception {
        // Initialize the database
        azioneRepository.save(azione);

        int databaseSizeBeforeUpdate = azioneRepository.findAll().size();

        // Update the azione using partial update
        Azione partialUpdatedAzione = new Azione();
        partialUpdatedAzione.setId(azione.getId());

        partialUpdatedAzione
            .created(UPDATED_CREATED)
            .modified(UPDATED_MODIFIED)
            .nomeAzione(UPDATED_NOME_AZIONE)
            .descrizione(UPDATED_DESCRIZIONE);

        restAzioneMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAzione.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAzione))
            )
            .andExpect(status().isOk());

        // Validate the Azione in the database
        List<Azione> azioneList = azioneRepository.findAll();
        assertThat(azioneList).hasSize(databaseSizeBeforeUpdate);
        Azione testAzione = azioneList.get(azioneList.size() - 1);
        assertThat(testAzione.getCreated()).isEqualTo(UPDATED_CREATED);
        assertThat(testAzione.getModified()).isEqualTo(UPDATED_MODIFIED);
        assertThat(testAzione.getNomeAzione()).isEqualTo(UPDATED_NOME_AZIONE);
        assertThat(testAzione.getDescrizione()).isEqualTo(UPDATED_DESCRIZIONE);
    }

    @Test
    void patchNonExistingAzione() throws Exception {
        int databaseSizeBeforeUpdate = azioneRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(azioneSearchRepository.findAll());
        azione.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAzioneMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, azione.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(azione))
            )
            .andExpect(status().isBadRequest());

        // Validate the Azione in the database
        List<Azione> azioneList = azioneRepository.findAll();
        assertThat(azioneList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(azioneSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void patchWithIdMismatchAzione() throws Exception {
        int databaseSizeBeforeUpdate = azioneRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(azioneSearchRepository.findAll());
        azione.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAzioneMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(azione))
            )
            .andExpect(status().isBadRequest());

        // Validate the Azione in the database
        List<Azione> azioneList = azioneRepository.findAll();
        assertThat(azioneList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(azioneSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void patchWithMissingIdPathParamAzione() throws Exception {
        int databaseSizeBeforeUpdate = azioneRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(azioneSearchRepository.findAll());
        azione.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAzioneMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(azione))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Azione in the database
        List<Azione> azioneList = azioneRepository.findAll();
        assertThat(azioneList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(azioneSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void deleteAzione() throws Exception {
        // Initialize the database
        azioneRepository.save(azione);
        azioneRepository.save(azione);
        azioneSearchRepository.save(azione);

        int databaseSizeBeforeDelete = azioneRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(azioneSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the azione
        restAzioneMockMvc
            .perform(delete(ENTITY_API_URL_ID, azione.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Azione> azioneList = azioneRepository.findAll();
        assertThat(azioneList).hasSize(databaseSizeBeforeDelete - 1);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(azioneSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    void searchAzione() throws Exception {
        // Initialize the database
        azione = azioneRepository.save(azione);
        azioneSearchRepository.save(azione);

        // Search the azione
        restAzioneMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + azione.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(azione.getId().intValue())))
            .andExpect(jsonPath("$.[*].created").value(hasItem(DEFAULT_CREATED.toString())))
            .andExpect(jsonPath("$.[*].modified").value(hasItem(DEFAULT_MODIFIED.toString())))
            .andExpect(jsonPath("$.[*].nomeAzione").value(hasItem(DEFAULT_NOME_AZIONE)))
            .andExpect(jsonPath("$.[*].descrizione").value(hasItem(DEFAULT_DESCRIZIONE)));
    }
}
