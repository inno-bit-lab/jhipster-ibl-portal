package it.ibl.portal.service.impl;

import it.ibl.portal.domain.Ruolo;
import it.ibl.portal.repository.RuoloRepository;
import it.ibl.portal.repository.search.RuoloSearchRepository;
import it.ibl.portal.service.RuoloService;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Service Implementation for managing {@link it.ibl.portal.domain.Ruolo}.
 */
@Service
public class RuoloServiceImpl implements RuoloService {

    private final Logger log = LoggerFactory.getLogger(RuoloServiceImpl.class);

    private final RuoloRepository ruoloRepository;

    private final RuoloSearchRepository ruoloSearchRepository;

    public RuoloServiceImpl(RuoloRepository ruoloRepository, RuoloSearchRepository ruoloSearchRepository) {
        this.ruoloRepository = ruoloRepository;
        this.ruoloSearchRepository = ruoloSearchRepository;
    }

    @Override
    public Ruolo save(Ruolo ruolo) {
        log.debug("Request to save Ruolo : {}", ruolo);
        Ruolo result = ruoloRepository.save(ruolo);
        ruoloSearchRepository.index(result);
        return result;
    }

    @Override
    public Ruolo update(Ruolo ruolo) {
        log.debug("Request to update Ruolo : {}", ruolo);
        Ruolo result = ruoloRepository.save(ruolo);
        ruoloSearchRepository.index(result);
        return result;
    }

    @Override
    public Optional<Ruolo> partialUpdate(Ruolo ruolo) {
        log.debug("Request to partially update Ruolo : {}", ruolo);

        return ruoloRepository
            .findById(ruolo.getId())
            .map(existingRuolo -> {
                if (ruolo.getCreated() != null) {
                    existingRuolo.setCreated(ruolo.getCreated());
                }
                if (ruolo.getModified() != null) {
                    existingRuolo.setModified(ruolo.getModified());
                }
                if (ruolo.getNomeAzione() != null) {
                    existingRuolo.setNomeAzione(ruolo.getNomeAzione());
                }

                return existingRuolo;
            })
            .map(ruoloRepository::save)
            .map(savedRuolo -> {
                ruoloSearchRepository.index(savedRuolo);
                return savedRuolo;
            });
    }

    @Override
    public List<Ruolo> findAll() {
        log.debug("Request to get all Ruolos");
        return ruoloRepository.findAll();
    }

    @Override
    public Optional<Ruolo> findOne(Long id) {
        log.debug("Request to get Ruolo : {}", id);
        return ruoloRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Ruolo : {}", id);
        ruoloRepository.deleteById(id);
        ruoloSearchRepository.deleteFromIndexById(id);
    }

    @Override
    public List<Ruolo> search(String query) {
        log.debug("Request to search Ruolos for query {}", query);
        try {
            return StreamSupport.stream(ruoloSearchRepository.search(query).spliterator(), false).toList();
        } catch (RuntimeException e) {
            throw e;
        }
    }
}
