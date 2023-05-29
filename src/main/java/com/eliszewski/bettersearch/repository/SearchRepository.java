package com.eliszewski.bettersearch.repository;

import com.eliszewski.bettersearch.domain.Search;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Search entity.
 */
@Repository
public interface SearchRepository extends JpaRepository<Search, Long> {
    @Query("select search from Search search where search.user.login = ?#{principal.username}")
    List<Search> findByUserIsCurrentUser();

    default Optional<Search> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Search> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Search> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct search from Search search left join fetch search.user",
        countQuery = "select count(distinct search) from Search search"
    )
    Page<Search> findAllWithToOneRelationships(Pageable pageable);

    @Query("select distinct search from Search search left join fetch search.user")
    List<Search> findAllWithToOneRelationships();

    @Query("select search from Search search left join fetch search.user where search.id =:id")
    Optional<Search> findOneWithToOneRelationships(@Param("id") Long id);
}
