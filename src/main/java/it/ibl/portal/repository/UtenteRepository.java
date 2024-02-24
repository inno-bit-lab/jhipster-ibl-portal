package it.ibl.portal.repository;

import it.ibl.portal.domain.Utente;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB repository for the Utente entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UtenteRepository extends MongoRepository<Utente, Long> {}
