package it.ibl.portal.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import it.ibl.portal.IntegrationTest;
import it.ibl.portal.domain.Utente;
import it.ibl.portal.repository.UtenteRepository;
import it.ibl.portal.repository.search.UtenteSearchRepository;
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
 * Integration tests for the {@link UtenteResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class UtenteResourceIT {

    private static final LocalDate DEFAULT_CREATED = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CREATED = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_MODIFIED = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_MODIFIED = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_USERNAME = "AAAAAAAAAA";
    private static final String UPDATED_USERNAME = "BBBBBBBBBB";

    private static final String DEFAULT_PASSWORD = "AAAAAAAAAA";
    private static final String UPDATED_PASSWORD = "BBBBBBBBBB";

    private static final String DEFAULT_MAIL = "AAAAAAAAAA";
    private static final String UPDATED_MAIL = "BBBBBBBBBB";

    private static final String DEFAULT_MOBILE = "AAAAAAAAAA";
    private static final String UPDATED_MOBILE = "BBBBBBBBBB";

    private static final String DEFAULT_FACEBOOK = "AAAAAAAAAA";
    private static final String UPDATED_FACEBOOK = "BBBBBBBBBB";

    private static final String DEFAULT_GOOGLE = "AAAAAAAAAA";
    private static final String UPDATED_GOOGLE = "BBBBBBBBBB";

    private static final String DEFAULT_INSTANGRAM = "AAAAAAAAAA";
    private static final String UPDATED_INSTANGRAM = "BBBBBBBBBB";

    private static final String DEFAULT_PROVIDER = "AAAAAAAAAA";
    private static final String UPDATED_PROVIDER = "BBBBBBBBBB";

    private static final Boolean DEFAULT_ATTIVO = false;
    private static final Boolean UPDATED_ATTIVO = true;

    private static final String DEFAULT_MOTIVO_BOLOCCO = "AAAAAAAAAA";
    private static final String UPDATED_MOTIVO_BOLOCCO = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DATA_BOLOCCO = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATA_BOLOCCO = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_REGISTRATION_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_REGISTRATION_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_LAST_ACCESS = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_LAST_ACCESS = LocalDate.now(ZoneId.systemDefault());

    private static final String ENTITY_API_URL = "/api/utentes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/utentes/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private UtenteRepository utenteRepository;

    @Autowired
    private UtenteSearchRepository utenteSearchRepository;

    @Autowired
    private MockMvc restUtenteMockMvc;

    private Utente utente;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Utente createEntity() {
        Utente utente = new Utente()
            .created(DEFAULT_CREATED)
            .modified(DEFAULT_MODIFIED)
            .username(DEFAULT_USERNAME)
            .password(DEFAULT_PASSWORD)
            .mail(DEFAULT_MAIL)
            .mobile(DEFAULT_MOBILE)
            .facebook(DEFAULT_FACEBOOK)
            .google(DEFAULT_GOOGLE)
            .instangram(DEFAULT_INSTANGRAM)
            .provider(DEFAULT_PROVIDER)
            .attivo(DEFAULT_ATTIVO)
            .motivoBolocco(DEFAULT_MOTIVO_BOLOCCO)
            .dataBolocco(DEFAULT_DATA_BOLOCCO)
            .registrationDate(DEFAULT_REGISTRATION_DATE)
            .lastAccess(DEFAULT_LAST_ACCESS);
        return utente;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Utente createUpdatedEntity() {
        Utente utente = new Utente()
            .created(UPDATED_CREATED)
            .modified(UPDATED_MODIFIED)
            .username(UPDATED_USERNAME)
            .password(UPDATED_PASSWORD)
            .mail(UPDATED_MAIL)
            .mobile(UPDATED_MOBILE)
            .facebook(UPDATED_FACEBOOK)
            .google(UPDATED_GOOGLE)
            .instangram(UPDATED_INSTANGRAM)
            .provider(UPDATED_PROVIDER)
            .attivo(UPDATED_ATTIVO)
            .motivoBolocco(UPDATED_MOTIVO_BOLOCCO)
            .dataBolocco(UPDATED_DATA_BOLOCCO)
            .registrationDate(UPDATED_REGISTRATION_DATE)
            .lastAccess(UPDATED_LAST_ACCESS);
        return utente;
    }

    @AfterEach
    public void cleanupElasticSearchRepository() {
        utenteSearchRepository.deleteAll();
        assertThat(utenteSearchRepository.count()).isEqualTo(0);
    }

    @BeforeEach
    public void initTest() {
        utenteRepository.deleteAll();
        utente = createEntity();
    }

    @Test
    void createUtente() throws Exception {
        int databaseSizeBeforeCreate = utenteRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(utenteSearchRepository.findAll());
        // Create the Utente
        restUtenteMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(utente))
            )
            .andExpect(status().isCreated());

        // Validate the Utente in the database
        List<Utente> utenteList = utenteRepository.findAll();
        assertThat(utenteList).hasSize(databaseSizeBeforeCreate + 1);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(utenteSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });
        Utente testUtente = utenteList.get(utenteList.size() - 1);
        assertThat(testUtente.getCreated()).isEqualTo(DEFAULT_CREATED);
        assertThat(testUtente.getModified()).isEqualTo(DEFAULT_MODIFIED);
        assertThat(testUtente.getUsername()).isEqualTo(DEFAULT_USERNAME);
        assertThat(testUtente.getPassword()).isEqualTo(DEFAULT_PASSWORD);
        assertThat(testUtente.getMail()).isEqualTo(DEFAULT_MAIL);
        assertThat(testUtente.getMobile()).isEqualTo(DEFAULT_MOBILE);
        assertThat(testUtente.getFacebook()).isEqualTo(DEFAULT_FACEBOOK);
        assertThat(testUtente.getGoogle()).isEqualTo(DEFAULT_GOOGLE);
        assertThat(testUtente.getInstangram()).isEqualTo(DEFAULT_INSTANGRAM);
        assertThat(testUtente.getProvider()).isEqualTo(DEFAULT_PROVIDER);
        assertThat(testUtente.getAttivo()).isEqualTo(DEFAULT_ATTIVO);
        assertThat(testUtente.getMotivoBolocco()).isEqualTo(DEFAULT_MOTIVO_BOLOCCO);
        assertThat(testUtente.getDataBolocco()).isEqualTo(DEFAULT_DATA_BOLOCCO);
        assertThat(testUtente.getRegistrationDate()).isEqualTo(DEFAULT_REGISTRATION_DATE);
        assertThat(testUtente.getLastAccess()).isEqualTo(DEFAULT_LAST_ACCESS);
    }

    @Test
    void createUtenteWithExistingId() throws Exception {
        // Create the Utente with an existing ID
        utente.setId(1L);

        int databaseSizeBeforeCreate = utenteRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(utenteSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restUtenteMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(utente))
            )
            .andExpect(status().isBadRequest());

        // Validate the Utente in the database
        List<Utente> utenteList = utenteRepository.findAll();
        assertThat(utenteList).hasSize(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(utenteSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void getAllUtentes() throws Exception {
        // Initialize the database
        utenteRepository.save(utente);

        // Get all the utenteList
        restUtenteMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(utente.getId().intValue())))
            .andExpect(jsonPath("$.[*].created").value(hasItem(DEFAULT_CREATED.toString())))
            .andExpect(jsonPath("$.[*].modified").value(hasItem(DEFAULT_MODIFIED.toString())))
            .andExpect(jsonPath("$.[*].username").value(hasItem(DEFAULT_USERNAME)))
            .andExpect(jsonPath("$.[*].password").value(hasItem(DEFAULT_PASSWORD)))
            .andExpect(jsonPath("$.[*].mail").value(hasItem(DEFAULT_MAIL)))
            .andExpect(jsonPath("$.[*].mobile").value(hasItem(DEFAULT_MOBILE)))
            .andExpect(jsonPath("$.[*].facebook").value(hasItem(DEFAULT_FACEBOOK)))
            .andExpect(jsonPath("$.[*].google").value(hasItem(DEFAULT_GOOGLE)))
            .andExpect(jsonPath("$.[*].instangram").value(hasItem(DEFAULT_INSTANGRAM)))
            .andExpect(jsonPath("$.[*].provider").value(hasItem(DEFAULT_PROVIDER)))
            .andExpect(jsonPath("$.[*].attivo").value(hasItem(DEFAULT_ATTIVO.booleanValue())))
            .andExpect(jsonPath("$.[*].motivoBolocco").value(hasItem(DEFAULT_MOTIVO_BOLOCCO)))
            .andExpect(jsonPath("$.[*].dataBolocco").value(hasItem(DEFAULT_DATA_BOLOCCO.toString())))
            .andExpect(jsonPath("$.[*].registrationDate").value(hasItem(DEFAULT_REGISTRATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastAccess").value(hasItem(DEFAULT_LAST_ACCESS.toString())));
    }

    @Test
    void getUtente() throws Exception {
        // Initialize the database
        utenteRepository.save(utente);

        // Get the utente
        restUtenteMockMvc
            .perform(get(ENTITY_API_URL_ID, utente.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(utente.getId().intValue()))
            .andExpect(jsonPath("$.created").value(DEFAULT_CREATED.toString()))
            .andExpect(jsonPath("$.modified").value(DEFAULT_MODIFIED.toString()))
            .andExpect(jsonPath("$.username").value(DEFAULT_USERNAME))
            .andExpect(jsonPath("$.password").value(DEFAULT_PASSWORD))
            .andExpect(jsonPath("$.mail").value(DEFAULT_MAIL))
            .andExpect(jsonPath("$.mobile").value(DEFAULT_MOBILE))
            .andExpect(jsonPath("$.facebook").value(DEFAULT_FACEBOOK))
            .andExpect(jsonPath("$.google").value(DEFAULT_GOOGLE))
            .andExpect(jsonPath("$.instangram").value(DEFAULT_INSTANGRAM))
            .andExpect(jsonPath("$.provider").value(DEFAULT_PROVIDER))
            .andExpect(jsonPath("$.attivo").value(DEFAULT_ATTIVO.booleanValue()))
            .andExpect(jsonPath("$.motivoBolocco").value(DEFAULT_MOTIVO_BOLOCCO))
            .andExpect(jsonPath("$.dataBolocco").value(DEFAULT_DATA_BOLOCCO.toString()))
            .andExpect(jsonPath("$.registrationDate").value(DEFAULT_REGISTRATION_DATE.toString()))
            .andExpect(jsonPath("$.lastAccess").value(DEFAULT_LAST_ACCESS.toString()));
    }

    @Test
    void getNonExistingUtente() throws Exception {
        // Get the utente
        restUtenteMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    void putExistingUtente() throws Exception {
        // Initialize the database
        utenteRepository.save(utente);

        int databaseSizeBeforeUpdate = utenteRepository.findAll().size();
        utenteSearchRepository.save(utente);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(utenteSearchRepository.findAll());

        // Update the utente
        Utente updatedUtente = utenteRepository.findById(utente.getId()).orElseThrow();
        updatedUtente
            .created(UPDATED_CREATED)
            .modified(UPDATED_MODIFIED)
            .username(UPDATED_USERNAME)
            .password(UPDATED_PASSWORD)
            .mail(UPDATED_MAIL)
            .mobile(UPDATED_MOBILE)
            .facebook(UPDATED_FACEBOOK)
            .google(UPDATED_GOOGLE)
            .instangram(UPDATED_INSTANGRAM)
            .provider(UPDATED_PROVIDER)
            .attivo(UPDATED_ATTIVO)
            .motivoBolocco(UPDATED_MOTIVO_BOLOCCO)
            .dataBolocco(UPDATED_DATA_BOLOCCO)
            .registrationDate(UPDATED_REGISTRATION_DATE)
            .lastAccess(UPDATED_LAST_ACCESS);

        restUtenteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedUtente.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedUtente))
            )
            .andExpect(status().isOk());

        // Validate the Utente in the database
        List<Utente> utenteList = utenteRepository.findAll();
        assertThat(utenteList).hasSize(databaseSizeBeforeUpdate);
        Utente testUtente = utenteList.get(utenteList.size() - 1);
        assertThat(testUtente.getCreated()).isEqualTo(UPDATED_CREATED);
        assertThat(testUtente.getModified()).isEqualTo(UPDATED_MODIFIED);
        assertThat(testUtente.getUsername()).isEqualTo(UPDATED_USERNAME);
        assertThat(testUtente.getPassword()).isEqualTo(UPDATED_PASSWORD);
        assertThat(testUtente.getMail()).isEqualTo(UPDATED_MAIL);
        assertThat(testUtente.getMobile()).isEqualTo(UPDATED_MOBILE);
        assertThat(testUtente.getFacebook()).isEqualTo(UPDATED_FACEBOOK);
        assertThat(testUtente.getGoogle()).isEqualTo(UPDATED_GOOGLE);
        assertThat(testUtente.getInstangram()).isEqualTo(UPDATED_INSTANGRAM);
        assertThat(testUtente.getProvider()).isEqualTo(UPDATED_PROVIDER);
        assertThat(testUtente.getAttivo()).isEqualTo(UPDATED_ATTIVO);
        assertThat(testUtente.getMotivoBolocco()).isEqualTo(UPDATED_MOTIVO_BOLOCCO);
        assertThat(testUtente.getDataBolocco()).isEqualTo(UPDATED_DATA_BOLOCCO);
        assertThat(testUtente.getRegistrationDate()).isEqualTo(UPDATED_REGISTRATION_DATE);
        assertThat(testUtente.getLastAccess()).isEqualTo(UPDATED_LAST_ACCESS);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(utenteSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<Utente> utenteSearchList = IterableUtils.toList(utenteSearchRepository.findAll());
                Utente testUtenteSearch = utenteSearchList.get(searchDatabaseSizeAfter - 1);
                assertThat(testUtenteSearch.getCreated()).isEqualTo(UPDATED_CREATED);
                assertThat(testUtenteSearch.getModified()).isEqualTo(UPDATED_MODIFIED);
                assertThat(testUtenteSearch.getUsername()).isEqualTo(UPDATED_USERNAME);
                assertThat(testUtenteSearch.getPassword()).isEqualTo(UPDATED_PASSWORD);
                assertThat(testUtenteSearch.getMail()).isEqualTo(UPDATED_MAIL);
                assertThat(testUtenteSearch.getMobile()).isEqualTo(UPDATED_MOBILE);
                assertThat(testUtenteSearch.getFacebook()).isEqualTo(UPDATED_FACEBOOK);
                assertThat(testUtenteSearch.getGoogle()).isEqualTo(UPDATED_GOOGLE);
                assertThat(testUtenteSearch.getInstangram()).isEqualTo(UPDATED_INSTANGRAM);
                assertThat(testUtenteSearch.getProvider()).isEqualTo(UPDATED_PROVIDER);
                assertThat(testUtenteSearch.getAttivo()).isEqualTo(UPDATED_ATTIVO);
                assertThat(testUtenteSearch.getMotivoBolocco()).isEqualTo(UPDATED_MOTIVO_BOLOCCO);
                assertThat(testUtenteSearch.getDataBolocco()).isEqualTo(UPDATED_DATA_BOLOCCO);
                assertThat(testUtenteSearch.getRegistrationDate()).isEqualTo(UPDATED_REGISTRATION_DATE);
                assertThat(testUtenteSearch.getLastAccess()).isEqualTo(UPDATED_LAST_ACCESS);
            });
    }

    @Test
    void putNonExistingUtente() throws Exception {
        int databaseSizeBeforeUpdate = utenteRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(utenteSearchRepository.findAll());
        utente.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUtenteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, utente.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(utente))
            )
            .andExpect(status().isBadRequest());

        // Validate the Utente in the database
        List<Utente> utenteList = utenteRepository.findAll();
        assertThat(utenteList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(utenteSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void putWithIdMismatchUtente() throws Exception {
        int databaseSizeBeforeUpdate = utenteRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(utenteSearchRepository.findAll());
        utente.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUtenteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(utente))
            )
            .andExpect(status().isBadRequest());

        // Validate the Utente in the database
        List<Utente> utenteList = utenteRepository.findAll();
        assertThat(utenteList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(utenteSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void putWithMissingIdPathParamUtente() throws Exception {
        int databaseSizeBeforeUpdate = utenteRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(utenteSearchRepository.findAll());
        utente.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUtenteMockMvc
            .perform(
                put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(utente))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Utente in the database
        List<Utente> utenteList = utenteRepository.findAll();
        assertThat(utenteList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(utenteSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void partialUpdateUtenteWithPatch() throws Exception {
        // Initialize the database
        utenteRepository.save(utente);

        int databaseSizeBeforeUpdate = utenteRepository.findAll().size();

        // Update the utente using partial update
        Utente partialUpdatedUtente = new Utente();
        partialUpdatedUtente.setId(utente.getId());

        partialUpdatedUtente
            .created(UPDATED_CREATED)
            .password(UPDATED_PASSWORD)
            .mobile(UPDATED_MOBILE)
            .instangram(UPDATED_INSTANGRAM)
            .motivoBolocco(UPDATED_MOTIVO_BOLOCCO);

        restUtenteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUtente.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedUtente))
            )
            .andExpect(status().isOk());

        // Validate the Utente in the database
        List<Utente> utenteList = utenteRepository.findAll();
        assertThat(utenteList).hasSize(databaseSizeBeforeUpdate);
        Utente testUtente = utenteList.get(utenteList.size() - 1);
        assertThat(testUtente.getCreated()).isEqualTo(UPDATED_CREATED);
        assertThat(testUtente.getModified()).isEqualTo(DEFAULT_MODIFIED);
        assertThat(testUtente.getUsername()).isEqualTo(DEFAULT_USERNAME);
        assertThat(testUtente.getPassword()).isEqualTo(UPDATED_PASSWORD);
        assertThat(testUtente.getMail()).isEqualTo(DEFAULT_MAIL);
        assertThat(testUtente.getMobile()).isEqualTo(UPDATED_MOBILE);
        assertThat(testUtente.getFacebook()).isEqualTo(DEFAULT_FACEBOOK);
        assertThat(testUtente.getGoogle()).isEqualTo(DEFAULT_GOOGLE);
        assertThat(testUtente.getInstangram()).isEqualTo(UPDATED_INSTANGRAM);
        assertThat(testUtente.getProvider()).isEqualTo(DEFAULT_PROVIDER);
        assertThat(testUtente.getAttivo()).isEqualTo(DEFAULT_ATTIVO);
        assertThat(testUtente.getMotivoBolocco()).isEqualTo(UPDATED_MOTIVO_BOLOCCO);
        assertThat(testUtente.getDataBolocco()).isEqualTo(DEFAULT_DATA_BOLOCCO);
        assertThat(testUtente.getRegistrationDate()).isEqualTo(DEFAULT_REGISTRATION_DATE);
        assertThat(testUtente.getLastAccess()).isEqualTo(DEFAULT_LAST_ACCESS);
    }

    @Test
    void fullUpdateUtenteWithPatch() throws Exception {
        // Initialize the database
        utenteRepository.save(utente);

        int databaseSizeBeforeUpdate = utenteRepository.findAll().size();

        // Update the utente using partial update
        Utente partialUpdatedUtente = new Utente();
        partialUpdatedUtente.setId(utente.getId());

        partialUpdatedUtente
            .created(UPDATED_CREATED)
            .modified(UPDATED_MODIFIED)
            .username(UPDATED_USERNAME)
            .password(UPDATED_PASSWORD)
            .mail(UPDATED_MAIL)
            .mobile(UPDATED_MOBILE)
            .facebook(UPDATED_FACEBOOK)
            .google(UPDATED_GOOGLE)
            .instangram(UPDATED_INSTANGRAM)
            .provider(UPDATED_PROVIDER)
            .attivo(UPDATED_ATTIVO)
            .motivoBolocco(UPDATED_MOTIVO_BOLOCCO)
            .dataBolocco(UPDATED_DATA_BOLOCCO)
            .registrationDate(UPDATED_REGISTRATION_DATE)
            .lastAccess(UPDATED_LAST_ACCESS);

        restUtenteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUtente.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedUtente))
            )
            .andExpect(status().isOk());

        // Validate the Utente in the database
        List<Utente> utenteList = utenteRepository.findAll();
        assertThat(utenteList).hasSize(databaseSizeBeforeUpdate);
        Utente testUtente = utenteList.get(utenteList.size() - 1);
        assertThat(testUtente.getCreated()).isEqualTo(UPDATED_CREATED);
        assertThat(testUtente.getModified()).isEqualTo(UPDATED_MODIFIED);
        assertThat(testUtente.getUsername()).isEqualTo(UPDATED_USERNAME);
        assertThat(testUtente.getPassword()).isEqualTo(UPDATED_PASSWORD);
        assertThat(testUtente.getMail()).isEqualTo(UPDATED_MAIL);
        assertThat(testUtente.getMobile()).isEqualTo(UPDATED_MOBILE);
        assertThat(testUtente.getFacebook()).isEqualTo(UPDATED_FACEBOOK);
        assertThat(testUtente.getGoogle()).isEqualTo(UPDATED_GOOGLE);
        assertThat(testUtente.getInstangram()).isEqualTo(UPDATED_INSTANGRAM);
        assertThat(testUtente.getProvider()).isEqualTo(UPDATED_PROVIDER);
        assertThat(testUtente.getAttivo()).isEqualTo(UPDATED_ATTIVO);
        assertThat(testUtente.getMotivoBolocco()).isEqualTo(UPDATED_MOTIVO_BOLOCCO);
        assertThat(testUtente.getDataBolocco()).isEqualTo(UPDATED_DATA_BOLOCCO);
        assertThat(testUtente.getRegistrationDate()).isEqualTo(UPDATED_REGISTRATION_DATE);
        assertThat(testUtente.getLastAccess()).isEqualTo(UPDATED_LAST_ACCESS);
    }

    @Test
    void patchNonExistingUtente() throws Exception {
        int databaseSizeBeforeUpdate = utenteRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(utenteSearchRepository.findAll());
        utente.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUtenteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, utente.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(utente))
            )
            .andExpect(status().isBadRequest());

        // Validate the Utente in the database
        List<Utente> utenteList = utenteRepository.findAll();
        assertThat(utenteList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(utenteSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void patchWithIdMismatchUtente() throws Exception {
        int databaseSizeBeforeUpdate = utenteRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(utenteSearchRepository.findAll());
        utente.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUtenteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(utente))
            )
            .andExpect(status().isBadRequest());

        // Validate the Utente in the database
        List<Utente> utenteList = utenteRepository.findAll();
        assertThat(utenteList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(utenteSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void patchWithMissingIdPathParamUtente() throws Exception {
        int databaseSizeBeforeUpdate = utenteRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(utenteSearchRepository.findAll());
        utente.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUtenteMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(utente))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Utente in the database
        List<Utente> utenteList = utenteRepository.findAll();
        assertThat(utenteList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(utenteSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void deleteUtente() throws Exception {
        // Initialize the database
        utenteRepository.save(utente);
        utenteRepository.save(utente);
        utenteSearchRepository.save(utente);

        int databaseSizeBeforeDelete = utenteRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(utenteSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the utente
        restUtenteMockMvc
            .perform(delete(ENTITY_API_URL_ID, utente.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Utente> utenteList = utenteRepository.findAll();
        assertThat(utenteList).hasSize(databaseSizeBeforeDelete - 1);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(utenteSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    void searchUtente() throws Exception {
        // Initialize the database
        utente = utenteRepository.save(utente);
        utenteSearchRepository.save(utente);

        // Search the utente
        restUtenteMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + utente.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(utente.getId().intValue())))
            .andExpect(jsonPath("$.[*].created").value(hasItem(DEFAULT_CREATED.toString())))
            .andExpect(jsonPath("$.[*].modified").value(hasItem(DEFAULT_MODIFIED.toString())))
            .andExpect(jsonPath("$.[*].username").value(hasItem(DEFAULT_USERNAME)))
            .andExpect(jsonPath("$.[*].password").value(hasItem(DEFAULT_PASSWORD)))
            .andExpect(jsonPath("$.[*].mail").value(hasItem(DEFAULT_MAIL)))
            .andExpect(jsonPath("$.[*].mobile").value(hasItem(DEFAULT_MOBILE)))
            .andExpect(jsonPath("$.[*].facebook").value(hasItem(DEFAULT_FACEBOOK)))
            .andExpect(jsonPath("$.[*].google").value(hasItem(DEFAULT_GOOGLE)))
            .andExpect(jsonPath("$.[*].instangram").value(hasItem(DEFAULT_INSTANGRAM)))
            .andExpect(jsonPath("$.[*].provider").value(hasItem(DEFAULT_PROVIDER)))
            .andExpect(jsonPath("$.[*].attivo").value(hasItem(DEFAULT_ATTIVO.booleanValue())))
            .andExpect(jsonPath("$.[*].motivoBolocco").value(hasItem(DEFAULT_MOTIVO_BOLOCCO)))
            .andExpect(jsonPath("$.[*].dataBolocco").value(hasItem(DEFAULT_DATA_BOLOCCO.toString())))
            .andExpect(jsonPath("$.[*].registrationDate").value(hasItem(DEFAULT_REGISTRATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastAccess").value(hasItem(DEFAULT_LAST_ACCESS.toString())));
    }
}
