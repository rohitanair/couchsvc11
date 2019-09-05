package com.infy.tele.repository;

import com.infy.tele.domain.Acct;
import org.springframework.data.couchbase.core.query.Query;
import org.springframework.stereotype.Repository;


/**
 * Spring Data Couchbase repository for the Acct entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AcctRepository extends N1qlCouchbaseRepository<Acct, String> {

}
