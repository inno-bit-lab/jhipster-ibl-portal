package it.ibl.portal.service.impl;

import it.ibl.portal.domain.Azione;
import it.ibl.portal.repository.AzioneRepository;
import it.ibl.portal.repository.search.AzioneSearchRepository;
import it.ibl.portal.service.AzioneService;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Service Implementation for managing {@link it.ibl.portal.domain.Azione}.
 */
@Service
public class AzioneServiceImpl implements AzioneService {

    private final Logger log = LoggerFactory.getLogger(AzioneServiceImpl.class);

    private final AzioneRepository azioneRepository;

    private final AzioneSearchRepository azioneSearchRepository;

    public AzioneServiceImpl(AzioneRepository azioneRepository, AzioneSearchRepository azioneSearchRepository) {
        this.azioneRepository = azioneRepository;
        this.azioneSearchRepository = azioneSearchRepository;
    }

    @Override
    public Azione save(Azione azione) {
        log.debug("Request to save Azione : {}", azione);
        Azione result = azioneRepository.save(azione);
        azioneSearchRepository.index(result);
        return result;
    }

    @Override
    public Azione update(Azione azione) {
        log.debug("Request to update Azione : {}", azione);
        Azione result = azioneRepository.save(azione);
        azioneSearchRepository.index(result);
        return result;
    }

    @Override
    public Optional<Azione> partialUpdate(Azione azione) {
        log.debug("Request to partially update Azione : {}", azione);

        return azioneRepository
            .findById(azione.getId())
            .map(existingAzione -> {
                if (azione.getCreated() != null) {
                    existingAzione.setCreated(azione.getCreated());
                }
                if (azione.getModified() != null) {
                    existingAzione.setModified(azione.getModified());
                }
                if (azione.getNomeAzione() != null) {
                    existingAzione.setNomeAzione(azione.getNomeAzione());
                }
                if (azione.getDescrizione() != null) {
                    existingAzione.setDescrizione(azione.getDescrizione());
                }

                return existingAzione;
            })
            .map(azioneRepository::save)
            .map(savedAzione -> {
                azioneSearchRepository.index(savedAzione);
                return savedAzione;
            });
    }

    @Override
    public List<Azione> findAll() {
        log.debug("Request to get all Aziones");
        return azioneRepository.findAll();
    }

    @Override
    public Optional<Azione> findOne(Long id) {
        log.debug("Request to get Azione : {}", id);
        return azioneRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Azione : {}", id);
        azioneRepository.deleteById(id);
        azioneSearchRepository.deleteFromIndexById(id);
    }

    @Override
    public List<Azione> search(String query) {
        log.debug("Request to search Aziones for query {}", query);
        try {
            return StreamSupport.stream(azioneSearchRepository.search(query).spliterator(), false).toList();
        } catch (RuntimeException e) {
            throw e;
        }
    }
}
