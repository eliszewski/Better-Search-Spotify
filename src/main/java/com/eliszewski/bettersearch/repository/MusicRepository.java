package com.eliszewski.bettersearch.repository;

import com.eliszewski.bettersearch.domain.Music;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Music entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MusicRepository extends JpaRepository<Music, Long> {}
