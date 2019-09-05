package com.infy.tele.service;

import com.infy.tele.domain.Acct;
import com.infy.tele.repository.AcctRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing {@link Acct}.
 */
@Service
public class AcctService {

    private final Logger log = LoggerFactory.getLogger(AcctService.class);

    private final AcctRepository acctRepository;

    public AcctService(AcctRepository acctRepository) {
        this.acctRepository = acctRepository;
    }

    /**
     * Save a acct.
     *
     * @param acct the entity to save.
     * @return the persisted entity.
     */
    public Acct save(Acct acct) {
        log.debug("Request to save Acct : {}", acct);
        return acctRepository.save(acct);
    }

    /**
     * Get all the accts.
     *
     * @return the list of entities.
     */
    public List<Acct> findAll() {
        log.debug("Request to get all Accts");
        return acctRepository.findAll();
    }


    /**
     * Get one acct by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    public Optional<Acct> findOne(String id) {
        log.debug("Request to get Acct : {}", id);
        return acctRepository.findById(id);
    }

    /**
     * Delete the acct by id.
     *
     * @param id the id of the entity.
     */
    public void delete(String id) {
        log.debug("Request to delete Acct : {}", id);
        acctRepository.deleteById(id);
    }
}
