package com.infy.tele.repository;

import com.infy.tele.domain.AcctDetl;
import org.springframework.data.couchbase.core.query.Query;
import org.springframework.stereotype.Repository;


/**
 * Spring Data Couchbase repository for the AcctDetl entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AcctDetlRepository extends N1qlCouchbaseRepository<AcctDetl, String> {

}
