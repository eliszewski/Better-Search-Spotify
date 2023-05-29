package com.eliszewski.bettersearch.service;

import com.eliszewski.bettersearch.domain.SpotifyProfile;
import com.eliszewski.bettersearch.repository.SpotifyProfileRepository;
import com.eliszewski.bettersearch.service.dto.SpotifyProfileDTO;
import com.eliszewski.bettersearch.service.mapper.SpotifyProfileMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link SpotifyProfile}.
 */
@Service
@Transactional
public class SpotifyProfileService {

    private final Logger log = LoggerFactory.getLogger(SpotifyProfileService.class);

    private final SpotifyProfileRepository spotifyProfileRepository;

    private final SpotifyProfileMapper spotifyProfileMapper;

    public SpotifyProfileService(SpotifyProfileRepository spotifyProfileRepository, SpotifyProfileMapper spotifyProfileMapper) {
        this.spotifyProfileRepository = spotifyProfileRepository;
        this.spotifyProfileMapper = spotifyProfileMapper;
    }

    /**
     * Save a spotifyProfile.
     *
     * @param spotifyProfileDTO the entity to save.
     * @return the persisted entity.
     */
    public SpotifyProfileDTO save(SpotifyProfileDTO spotifyProfileDTO) {
        log.debug("Request to save SpotifyProfile : {}", spotifyProfileDTO);
        SpotifyProfile spotifyProfile = spotifyProfileMapper.toEntity(spotifyProfileDTO);
        spotifyProfile = spotifyProfileRepository.save(spotifyProfile);
        return spotifyProfileMapper.toDto(spotifyProfile);
    }

    /**
     * Update a spotifyProfile.
     *
     * @param spotifyProfileDTO the entity to save.
     * @return the persisted entity.
     */
    public SpotifyProfileDTO update(SpotifyProfileDTO spotifyProfileDTO) {
        log.debug("Request to update SpotifyProfile : {}", spotifyProfileDTO);
        SpotifyProfile spotifyProfile = spotifyProfileMapper.toEntity(spotifyProfileDTO);
        spotifyProfile = spotifyProfileRepository.save(spotifyProfile);
        return spotifyProfileMapper.toDto(spotifyProfile);
    }

    /**
     * Partially update a spotifyProfile.
     *
     * @param spotifyProfileDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<SpotifyProfileDTO> partialUpdate(SpotifyProfileDTO spotifyProfileDTO) {
        log.debug("Request to partially update SpotifyProfile : {}", spotifyProfileDTO);

        return spotifyProfileRepository
            .findById(spotifyProfileDTO.getId())
            .map(existingSpotifyProfile -> {
                spotifyProfileMapper.partialUpdate(existingSpotifyProfile, spotifyProfileDTO);

                return existingSpotifyProfile;
            })
            .map(spotifyProfileRepository::save)
            .map(spotifyProfileMapper::toDto);
    }

    /**
     * Get all the spotifyProfiles.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<SpotifyProfileDTO> findAll() {
        log.debug("Request to get all SpotifyProfiles");
        return spotifyProfileRepository
            .findAll()
            .stream()
            .map(spotifyProfileMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get all the spotifyProfiles with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<SpotifyProfileDTO> findAllWithEagerRelationships(Pageable pageable) {
        return spotifyProfileRepository.findAllWithEagerRelationships(pageable).map(spotifyProfileMapper::toDto);
    }

    /**
     * Get one spotifyProfile by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<SpotifyProfileDTO> findOne(Long id) {
        log.debug("Request to get SpotifyProfile : {}", id);
        return spotifyProfileRepository.findOneWithEagerRelationships(id).map(spotifyProfileMapper::toDto);
    }

    /**
     * Delete the spotifyProfile by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete SpotifyProfile : {}", id);
        spotifyProfileRepository.deleteById(id);
    }
}
