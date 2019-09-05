package com.infy.tele.service;

import com.infy.tele.domain.AcctDetl;
import com.infy.tele.repository.AcctDetlRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing {@link AcctDetl}.
 */
@Service
public class AcctDetlService {

    private final Logger log = LoggerFactory.getLogger(AcctDetlService.class);

    private final AcctDetlRepository acctDetlRepository;

    public AcctDetlService(AcctDetlRepository acctDetlRepository) {
        this.acctDetlRepository = acctDetlRepository;
    }

    /**
     * Save a acctDetl.
     *
     * @param acctDetl the entity to save.
     * @return the persisted entity.
     */
    public AcctDetl save(AcctDetl acctDetl) {
        log.debug("Request to save AcctDetl : {}", acctDetl);
        return acctDetlRepository.save(acctDetl);
    }

    /**
     * Get all the acctDetls.
     *
     * @return the list of entities.
     */
    public List<AcctDetl> findAll() {
        log.debug("Request to get all AcctDetls");
        return acctDetlRepository.findAll();
    }


    /**
     * Get one acctDetl by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    public Optional<AcctDetl> findOne(String id) {
        log.debug("Request to get AcctDetl : {}", id);
        return acctDetlRepository.findById(id);
    }

    /**
     * Delete the acctDetl by id.
     *
     * @param id the id of the entity.
     */
    public void delete(String id) {
        log.debug("Request to delete AcctDetl : {}", id);
        acctDetlRepository.deleteById(id);
    }
}
