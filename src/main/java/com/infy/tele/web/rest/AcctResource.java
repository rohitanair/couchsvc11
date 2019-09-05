package com.infy.tele.web.rest;

import com.infy.tele.domain.Acct;
import com.infy.tele.service.AcctService;
import com.infy.tele.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.infy.tele.domain.Acct}.
 */
@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*", allowedHeaders = "*") public class AcctResource {

    private final Logger log = LoggerFactory.getLogger(AcctResource.class);

    private static final String ENTITY_NAME = "couchsvc11Acct";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AcctService acctService;

    public AcctResource(AcctService acctService) {
        this.acctService = acctService;
    }

    /**
     * {@code POST  /accts} : Create a new acct.
     *
     * @param acct the acct to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new acct, or with status {@code 400 (Bad Request)} if the acct has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/accts")
    public ResponseEntity<Acct> createAcct(@RequestBody Acct acct) throws URISyntaxException {
        log.debug("REST request to save Acct : {}", acct);
        if (acct.getId() != null) {
            throw new BadRequestAlertException("A new acct cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Acct result = acctService.save(acct);
        return ResponseEntity.created(new URI("/api/accts/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /accts} : Updates an existing acct.
     *
     * @param acct the acct to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated acct,
     * or with status {@code 400 (Bad Request)} if the acct is not valid,
     * or with status {@code 500 (Internal Server Error)} if the acct couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/accts")
    public ResponseEntity<Acct> updateAcct(@RequestBody Acct acct) throws URISyntaxException {
        log.debug("REST request to update Acct : {}", acct);
        if (acct.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Acct result = acctService.save(acct);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, acct.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /accts} : get all the accts.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of accts in body.
     */
    @GetMapping("/accts")
    public List<Acct> getAllAccts() {
        log.debug("REST request to get all Accts");
        return acctService.findAll();
    }

    /**
     * {@code GET  /accts/:id} : get the "id" acct.
     *
     * @param id the id of the acct to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the acct, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/accts/{id}")
    public ResponseEntity<Acct> getAcct(@PathVariable String id) {
        log.debug("REST request to get Acct : {}", id);
        Optional<Acct> acct = acctService.findOne(id);
        return ResponseUtil.wrapOrNotFound(acct);
    }

    /**
     * {@code DELETE  /accts/:id} : delete the "id" acct.
     *
     * @param id the id of the acct to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/accts/{id}")
    public ResponseEntity<Void> deleteAcct(@PathVariable String id) {
        log.debug("REST request to delete Acct : {}", id);
        acctService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id)).build();
    }
}
