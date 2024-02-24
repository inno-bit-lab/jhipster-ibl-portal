package it.ibl.portal.web.rest;

import it.ibl.portal.domain.Azione;
import it.ibl.portal.repository.AzioneRepository;
import it.ibl.portal.service.AzioneService;
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
 * REST controller for managing {@link it.ibl.portal.domain.Azione}.
 */
@RestController
@RequestMapping("/api/aziones")
public class AzioneResource {

    private final Logger log = LoggerFactory.getLogger(AzioneResource.class);

    private static final String ENTITY_NAME = "azione";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AzioneService azioneService;

    private final AzioneRepository azioneRepository;

    public AzioneResource(AzioneService azioneService, AzioneRepository azioneRepository) {
        this.azioneService = azioneService;
        this.azioneRepository = azioneRepository;
    }

    /**
     * {@code POST  /aziones} : Create a new azione.
     *
     * @param azione the azione to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new azione, or with status {@code 400 (Bad Request)} if the azione has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<Azione> createAzione(@RequestBody Azione azione) throws URISyntaxException {
        log.debug("REST request to save Azione : {}", azione);
        if (azione.getId() != null) {
            throw new BadRequestAlertException("A new azione cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Azione result = azioneService.save(azione);
        return ResponseEntity
            .created(new URI("/api/aziones/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /aziones/:id} : Updates an existing azione.
     *
     * @param id the id of the azione to save.
     * @param azione the azione to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated azione,
     * or with status {@code 400 (Bad Request)} if the azione is not valid,
     * or with status {@code 500 (Internal Server Error)} if the azione couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Azione> updateAzione(@PathVariable(value = "id", required = false) final Long id, @RequestBody Azione azione)
        throws URISyntaxException {
        log.debug("REST request to update Azione : {}, {}", id, azione);
        if (azione.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, azione.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!azioneRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Azione result = azioneService.update(azione);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, azione.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /aziones/:id} : Partial updates given fields of an existing azione, field will ignore if it is null
     *
     * @param id the id of the azione to save.
     * @param azione the azione to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated azione,
     * or with status {@code 400 (Bad Request)} if the azione is not valid,
     * or with status {@code 404 (Not Found)} if the azione is not found,
     * or with status {@code 500 (Internal Server Error)} if the azione couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Azione> partialUpdateAzione(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Azione azione
    ) throws URISyntaxException {
        log.debug("REST request to partial update Azione partially : {}, {}", id, azione);
        if (azione.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, azione.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!azioneRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Azione> result = azioneService.partialUpdate(azione);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, azione.getId().toString())
        );
    }

    /**
     * {@code GET  /aziones} : get all the aziones.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of aziones in body.
     */
    @GetMapping("")
    public List<Azione> getAllAziones() {
        log.debug("REST request to get all Aziones");
        return azioneService.findAll();
    }

    /**
     * {@code GET  /aziones/:id} : get the "id" azione.
     *
     * @param id the id of the azione to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the azione, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Azione> getAzione(@PathVariable("id") Long id) {
        log.debug("REST request to get Azione : {}", id);
        Optional<Azione> azione = azioneService.findOne(id);
        return ResponseUtil.wrapOrNotFound(azione);
    }

    /**
     * {@code DELETE  /aziones/:id} : delete the "id" azione.
     *
     * @param id the id of the azione to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAzione(@PathVariable("id") Long id) {
        log.debug("REST request to delete Azione : {}", id);
        azioneService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /aziones/_search?query=:query} : search for the azione corresponding
     * to the query.
     *
     * @param query the query of the azione search.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public List<Azione> searchAziones(@RequestParam("query") String query) {
        log.debug("REST request to search Aziones for query {}", query);
        try {
            return azioneService.search(query);
        } catch (RuntimeException e) {
            throw ElasticsearchExceptionMapper.mapException(e);
        }
    }
}
