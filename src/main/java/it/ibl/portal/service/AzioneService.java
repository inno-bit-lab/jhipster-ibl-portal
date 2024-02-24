package it.ibl.portal.service;

import it.ibl.portal.domain.Azione;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link it.ibl.portal.domain.Azione}.
 */
public interface AzioneService {
    /**
     * Save a azione.
     *
     * @param azione the entity to save.
     * @return the persisted entity.
     */
    Azione save(Azione azione);

    /**
     * Updates a azione.
     *
     * @param azione the entity to update.
     * @return the persisted entity.
     */
    Azione update(Azione azione);

    /**
     * Partially updates a azione.
     *
     * @param azione the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Azione> partialUpdate(Azione azione);

    /**
     * Get all the aziones.
     *
     * @return the list of entities.
     */
    List<Azione> findAll();

    /**
     * Get the "id" azione.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Azione> findOne(Long id);

    /**
     * Delete the "id" azione.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the azione corresponding to the query.
     *
     * @param query the query of the search.
     * @return the list of entities.
     */
    List<Azione> search(String query);
}
