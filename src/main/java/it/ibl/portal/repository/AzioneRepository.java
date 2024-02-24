package it.ibl.portal.repository;

import it.ibl.portal.domain.Azione;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB repository for the Azione entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AzioneRepository extends MongoRepository<Azione, Long> {}
