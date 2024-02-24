package it.ibl.portal.repository;

import it.ibl.portal.domain.Ruolo;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB repository for the Ruolo entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RuoloRepository extends MongoRepository<Ruolo, Long> {}
