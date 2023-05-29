package com.eliszewski.bettersearch.repository;

import com.eliszewski.bettersearch.domain.SearchParameter;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the SearchParameter entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SearchParameterRepository extends JpaRepository<SearchParameter, Long> {}
