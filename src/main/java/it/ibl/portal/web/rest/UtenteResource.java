package it.ibl.portal.web.rest;

import it.ibl.portal.domain.Utente;
import it.ibl.portal.repository.UtenteRepository;
import it.ibl.portal.service.UtenteService;
import it.ibl.portal.web.rest.errors.BadRequestAlertException;
import it.ibl.portal.web.rest.errors.ElasticsearchExceptionMapper;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link it.ibl.portal.domain.Utente}.
 */
@RestController
@RequestMapping("/api/utentes")
public class UtenteResource {

    private final Logger log = LoggerFactory.getLogger(UtenteResource.class);

    private static final String ENTITY_NAME = "utente";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final UtenteService utenteService;

    private final UtenteRepository utenteRepository;

    public UtenteResource(UtenteService utenteService, UtenteRepository utenteRepository) {
        this.utenteService = utenteService;
        this.utenteRepository = utenteRepository;
    }

    /**
     * {@code POST  /utentes} : Create a new utente.
     *
     * @param utente the utente to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new utente, or with status {@code 400 (Bad Request)} if the utente has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<Utente> createUtente(@RequestBody Utente utente) throws URISyntaxException {
        log.debug("REST request to save Utente : {}", utente);
        if (utente.getId() != null) {
            throw new BadRequestAlertException("A new utente cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Utente result = utenteService.save(utente);
        return ResponseEntity
            .created(new URI("/api/utentes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /utentes/:id} : Updates an existing utente.
     *
     * @param id the id of the utente to save.
     * @param utente the utente to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated utente,
     * or with status {@code 400 (Bad Request)} if the utente is not valid,
     * or with status {@code 500 (Internal Server Error)} if the utente couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Utente> updateUtente(@PathVariable(value = "id", required = false) final Long id, @RequestBody Utente utente)
        throws URISyntaxException {
        log.debug("REST request to update Utente : {}, {}", id, utente);
        if (utente.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, utente.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!utenteRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Utente result = utenteService.update(utente);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, utente.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /utentes/:id} : Partial updates given fields of an existing utente, field will ignore if it is null
     *
     * @param id the id of the utente to save.
     * @param utente the utente to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated utente,
     * or with status {@code 400 (Bad Request)} if the utente is not valid,
     * or with status {@code 404 (Not Found)} if the utente is not found,
     * or with status {@code 500 (Internal Server Error)} if the utente couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Utente> partialUpdateUtente(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Utente utente
    ) throws URISyntaxException {
        log.debug("REST request to partial update Utente partially : {}, {}", id, utente);
        if (utente.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, utente.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!utenteRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Utente> result = utenteService.partialUpdate(utente);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, utente.getId().toString())
        );
    }

    /**
     * {@code GET  /utentes} : get all the utentes.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of utentes in body.
     */
    @GetMapping("")
    public List<Utente> getAllUtentes() {
        log.debug("REST request to get all Utentes");
        return utenteService.findAll();
    }

    /**
     * {@code GET  /utentes/:id} : get the "id" utente.
     *
     * @param id the id of the utente to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the utente, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Utente> getUtente(@PathVariable("id") Long id) {
        log.debug("REST request to get Utente : {}", id);
        Optional<Utente> utente = utenteService.findOne(id);
        return ResponseUtil.wrapOrNotFound(utente);
    }

    /**
     * {@code DELETE  /utentes/:id} : delete the "id" utente.
     *
     * @param id the id of the utente to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUtente(@PathVariable("id") Long id) {
        log.debug("REST request to delete Utente : {}", id);
        utenteService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /utentes/_search?query=:query} : search for the utente corresponding
     * to the query.
     *
     * @param query the query of the utente search.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public List<Utente> searchUtentes(@RequestParam("query") String query) {
        log.debug("REST request to search Utentes for query {}", query);
        try {
            return utenteService.search(query);
        } catch (RuntimeException e) {
            throw ElasticsearchExceptionMapper.mapException(e);
        }
    }
}
