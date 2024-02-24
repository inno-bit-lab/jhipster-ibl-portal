package it.ibl.portal.service.impl;

import it.ibl.portal.domain.Utente;
import it.ibl.portal.repository.UtenteRepository;
import it.ibl.portal.repository.search.UtenteSearchRepository;
import it.ibl.portal.service.UtenteService;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Service Implementation for managing {@link it.ibl.portal.domain.Utente}.
 */
@Service
public class UtenteServiceImpl implements UtenteService {

    private final Logger log = LoggerFactory.getLogger(UtenteServiceImpl.class);

    private final UtenteRepository utenteRepository;

    private final UtenteSearchRepository utenteSearchRepository;

    public UtenteServiceImpl(UtenteRepository utenteRepository, UtenteSearchRepository utenteSearchRepository) {
        this.utenteRepository = utenteRepository;
        this.utenteSearchRepository = utenteSearchRepository;
    }

    @Override
    public Utente save(Utente utente) {
        log.debug("Request to save Utente : {}", utente);
        Utente result = utenteRepository.save(utente);
        utenteSearchRepository.index(result);
        return result;
    }

    @Override
    public Utente update(Utente utente) {
        log.debug("Request to update Utente : {}", utente);
        Utente result = utenteRepository.save(utente);
        utenteSearchRepository.index(result);
        return result;
    }

    @Override
    public Optional<Utente> partialUpdate(Utente utente) {
        log.debug("Request to partially update Utente : {}", utente);

        return utenteRepository
            .findById(utente.getId())
            .map(existingUtente -> {
                if (utente.getCreated() != null) {
                    existingUtente.setCreated(utente.getCreated());
                }
                if (utente.getModified() != null) {
                    existingUtente.setModified(utente.getModified());
                }
                if (utente.getUsername() != null) {
                    existingUtente.setUsername(utente.getUsername());
                }
                if (utente.getPassword() != null) {
                    existingUtente.setPassword(utente.getPassword());
                }
                if (utente.getMail() != null) {
                    existingUtente.setMail(utente.getMail());
                }
                if (utente.getMobile() != null) {
                    existingUtente.setMobile(utente.getMobile());
                }
                if (utente.getFacebook() != null) {
                    existingUtente.setFacebook(utente.getFacebook());
                }
                if (utente.getGoogle() != null) {
                    existingUtente.setGoogle(utente.getGoogle());
                }
                if (utente.getInstangram() != null) {
                    existingUtente.setInstangram(utente.getInstangram());
                }
                if (utente.getProvider() != null) {
                    existingUtente.setProvider(utente.getProvider());
                }
                if (utente.getAttivo() != null) {
                    existingUtente.setAttivo(utente.getAttivo());
                }
                if (utente.getMotivoBolocco() != null) {
                    existingUtente.setMotivoBolocco(utente.getMotivoBolocco());
                }
                if (utente.getDataBolocco() != null) {
                    existingUtente.setDataBolocco(utente.getDataBolocco());
                }
                if (utente.getRegistrationDate() != null) {
                    existingUtente.setRegistrationDate(utente.getRegistrationDate());
                }
                if (utente.getLastAccess() != null) {
                    existingUtente.setLastAccess(utente.getLastAccess());
                }

                return existingUtente;
            })
            .map(utenteRepository::save)
            .map(savedUtente -> {
                utenteSearchRepository.index(savedUtente);
                return savedUtente;
            });
    }

    @Override
    public List<Utente> findAll() {
        log.debug("Request to get all Utentes");
        return utenteRepository.findAll();
    }

    @Override
    public Optional<Utente> findOne(Long id) {
        log.debug("Request to get Utente : {}", id);
        return utenteRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Utente : {}", id);
        utenteRepository.deleteById(id);
        utenteSearchRepository.deleteFromIndexById(id);
    }

    @Override
    public List<Utente> search(String query) {
        log.debug("Request to search Utentes for query {}", query);
        try {
            return StreamSupport.stream(utenteSearchRepository.search(query).spliterator(), false).toList();
        } catch (RuntimeException e) {
            throw e;
        }
    }
}
