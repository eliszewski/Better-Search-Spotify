package com.eliszewski.bettersearch.repository;

import com.eliszewski.bettersearch.domain.SpotifyProfile;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the SpotifyProfile entity.
 */
@Repository
public interface SpotifyProfileRepository extends JpaRepository<SpotifyProfile, Long> {
    default Optional<SpotifyProfile> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<SpotifyProfile> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<SpotifyProfile> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct spotifyProfile from SpotifyProfile spotifyProfile left join fetch spotifyProfile.user",
        countQuery = "select count(distinct spotifyProfile) from SpotifyProfile spotifyProfile"
    )
    Page<SpotifyProfile> findAllWithToOneRelationships(Pageable pageable);

    @Query("select distinct spotifyProfile from SpotifyProfile spotifyProfile left join fetch spotifyProfile.user")
    List<SpotifyProfile> findAllWithToOneRelationships();

    @Query("select spotifyProfile from SpotifyProfile spotifyProfile left join fetch spotifyProfile.user where spotifyProfile.id =:id")
    Optional<SpotifyProfile> findOneWithToOneRelationships(@Param("id") Long id);
}
