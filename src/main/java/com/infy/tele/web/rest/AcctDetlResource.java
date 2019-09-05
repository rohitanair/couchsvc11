package com.infy.tele.web.rest;

import com.infy.tele.domain.AcctDetl;
import com.infy.tele.service.AcctDetlService;
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
 * REST controller for managing {@link com.infy.tele.domain.AcctDetl}.
 */
@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*", allowedHeaders = "*") public class AcctDetlResource {

    private final Logger log = LoggerFactory.getLogger(AcctDetlResource.class);

    private static final String ENTITY_NAME = "couchsvc11AcctDetl";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AcctDetlService acctDetlService;

    public AcctDetlResource(AcctDetlService acctDetlService) {
        this.acctDetlService = acctDetlService;
    }

    /**
     * {@code POST  /acct-detls} : Create a new acctDetl.
     *
     * @param acctDetl the acctDetl to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new acctDetl, or with status {@code 400 (Bad Request)} if the acctDetl has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/acct-detls")
    public ResponseEntity<AcctDetl> createAcctDetl(@RequestBody AcctDetl acctDetl) throws URISyntaxException {
        log.debug("REST request to save AcctDetl : {}", acctDetl);
        if (acctDetl.getId() != null) {
            throw new BadRequestAlertException("A new acctDetl cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AcctDetl result = acctDetlService.save(acctDetl);
        return ResponseEntity.created(new URI("/api/acct-detls/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /acct-detls} : Updates an existing acctDetl.
     *
     * @param acctDetl the acctDetl to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated acctDetl,
     * or with status {@code 400 (Bad Request)} if the acctDetl is not valid,
     * or with status {@code 500 (Internal Server Error)} if the acctDetl couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/acct-detls")
    public ResponseEntity<AcctDetl> updateAcctDetl(@RequestBody AcctDetl acctDetl) throws URISyntaxException {
        log.debug("REST request to update AcctDetl : {}", acctDetl);
        if (acctDetl.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        AcctDetl result = acctDetlService.save(acctDetl);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, acctDetl.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /acct-detls} : get all the acctDetls.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of acctDetls in body.
     */
    @GetMapping("/acct-detls")
    public List<AcctDetl> getAllAcctDetls() {
        log.debug("REST request to get all AcctDetls");
        return acctDetlService.findAll();
    }

    /**
     * {@code GET  /acct-detls/:id} : get the "id" acctDetl.
     *
     * @param id the id of the acctDetl to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the acctDetl, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/acct-detls/{id}")
    public ResponseEntity<AcctDetl> getAcctDetl(@PathVariable String id) {
        log.debug("REST request to get AcctDetl : {}", id);
        Optional<AcctDetl> acctDetl = acctDetlService.findOne(id);
        return ResponseUtil.wrapOrNotFound(acctDetl);
    }

    /**
     * {@code DELETE  /acct-detls/:id} : delete the "id" acctDetl.
     *
     * @param id the id of the acctDetl to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/acct-detls/{id}")
    public ResponseEntity<Void> deleteAcctDetl(@PathVariable String id) {
        log.debug("REST request to delete AcctDetl : {}", id);
        acctDetlService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id)).build();
    }
}
