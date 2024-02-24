package it.ibl.portal.web.rest;

import it.ibl.portal.domain.Ruolo;
import it.ibl.portal.repository.RuoloRepository;
import it.ibl.portal.service.RuoloService;
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
 * REST controller for managing {@link it.ibl.portal.domain.Ruolo}.
 */
@RestController
@RequestMapping("/api/ruolos")
public class RuoloResource {

    private final Logger log = LoggerFactory.getLogger(RuoloResource.class);

    private static final String ENTITY_NAME = "ruolo";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final RuoloService ruoloService;

    private final RuoloRepository ruoloRepository;

    public RuoloResource(RuoloService ruoloService, RuoloRepository ruoloRepository) {
        this.ruoloService = ruoloService;
        this.ruoloRepository = ruoloRepository;
    }

    /**
     * {@code POST  /ruolos} : Create a new ruolo.
     *
     * @param ruolo the ruolo to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new ruolo, or with status {@code 400 (Bad Request)} if the ruolo has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<Ruolo> createRuolo(@RequestBody Ruolo ruolo) throws URISyntaxException {
        log.debug("REST request to save Ruolo : {}", ruolo);
        if (ruolo.getId() != null) {
            throw new BadRequestAlertException("A new ruolo cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Ruolo result = ruoloService.save(ruolo);
        return ResponseEntity
            .created(new URI("/api/ruolos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /ruolos/:id} : Updates an existing ruolo.
     *
     * @param id the id of the ruolo to save.
     * @param ruolo the ruolo to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ruolo,
     * or with status {@code 400 (Bad Request)} if the ruolo is not valid,
     * or with status {@code 500 (Internal Server Error)} if the ruolo couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Ruolo> updateRuolo(@PathVariable(value = "id", required = false) final Long id, @RequestBody Ruolo ruolo)
        throws URISyntaxException {
        log.debug("REST request to update Ruolo : {}, {}", id, ruolo);
        if (ruolo.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ruolo.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!ruoloRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Ruolo result = ruoloService.update(ruolo);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, ruolo.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /ruolos/:id} : Partial updates given fields of an existing ruolo, field will ignore if it is null
     *
     * @param id the id of the ruolo to save.
     * @param ruolo the ruolo to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ruolo,
     * or with status {@code 400 (Bad Request)} if the ruolo is not valid,
     * or with status {@code 404 (Not Found)} if the ruolo is not found,
     * or with status {@code 500 (Internal Server Error)} if the ruolo couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Ruolo> partialUpdateRuolo(@PathVariable(value = "id", required = false) final Long id, @RequestBody Ruolo ruolo)
        throws URISyntaxException {
        log.debug("REST request to partial update Ruolo partially : {}, {}", id, ruolo);
        if (ruolo.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ruolo.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!ruoloRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Ruolo> result = ruoloService.partialUpdate(ruolo);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, ruolo.getId().toString())
        );
    }

    /**
     * {@code GET  /ruolos} : get all the ruolos.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of ruolos in body.
     */
    @GetMapping("")
    public List<Ruolo> getAllRuolos() {
        log.debug("REST request to get all Ruolos");
        return ruoloService.findAll();
    }

    /**
     * {@code GET  /ruolos/:id} : get the "id" ruolo.
     *
     * @param id the id of the ruolo to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the ruolo, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Ruolo> getRuolo(@PathVariable("id") Long id) {
        log.debug("REST request to get Ruolo : {}", id);
        Optional<Ruolo> ruolo = ruoloService.findOne(id);
        return ResponseUtil.wrapOrNotFound(ruolo);
    }

    /**
     * {@code DELETE  /ruolos/:id} : delete the "id" ruolo.
     *
     * @param id the id of the ruolo to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRuolo(@PathVariable("id") Long id) {
        log.debug("REST request to delete Ruolo : {}", id);
        ruoloService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /ruolos/_search?query=:query} : search for the ruolo corresponding
     * to the query.
     *
     * @param query the query of the ruolo search.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public List<Ruolo> searchRuolos(@RequestParam("query") String query) {
        log.debug("REST request to search Ruolos for query {}", query);
        try {
            return ruoloService.search(query);
        } catch (RuntimeException e) {
            throw ElasticsearchExceptionMapper.mapException(e);
        }
    }
}
