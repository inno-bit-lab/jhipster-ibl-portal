package it.ibl.portal.service;

import it.ibl.portal.domain.Ruolo;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link it.ibl.portal.domain.Ruolo}.
 */
public interface RuoloService {
    /**
     * Save a ruolo.
     *
     * @param ruolo the entity to save.
     * @return the persisted entity.
     */
    Ruolo save(Ruolo ruolo);

    /**
     * Updates a ruolo.
     *
     * @param ruolo the entity to update.
     * @return the persisted entity.
     */
    Ruolo update(Ruolo ruolo);

    /**
     * Partially updates a ruolo.
     *
     * @param ruolo the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Ruolo> partialUpdate(Ruolo ruolo);

    /**
     * Get all the ruolos.
     *
     * @return the list of entities.
     */
    List<Ruolo> findAll();

    /**
     * Get the "id" ruolo.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Ruolo> findOne(Long id);

    /**
     * Delete the "id" ruolo.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the ruolo corresponding to the query.
     *
     * @param query the query of the search.
     * @return the list of entities.
     */
    List<Ruolo> search(String query);
}
