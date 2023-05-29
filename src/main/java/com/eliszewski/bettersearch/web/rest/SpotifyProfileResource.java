package com.eliszewski.bettersearch.web.rest;

import com.eliszewski.bettersearch.repository.SpotifyProfileRepository;
import com.eliszewski.bettersearch.service.SpotifyProfileService;
import com.eliszewski.bettersearch.service.dto.SpotifyProfileDTO;
import com.eliszewski.bettersearch.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.eliszewski.bettersearch.domain.SpotifyProfile}.
 */
@RestController
@RequestMapping("/api")
public class SpotifyProfileResource {

    private final Logger log = LoggerFactory.getLogger(SpotifyProfileResource.class);

    private static final String ENTITY_NAME = "spotifyProfile";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SpotifyProfileService spotifyProfileService;

    private final SpotifyProfileRepository spotifyProfileRepository;

    public SpotifyProfileResource(SpotifyProfileService spotifyProfileService, SpotifyProfileRepository spotifyProfileRepository) {
        this.spotifyProfileService = spotifyProfileService;
        this.spotifyProfileRepository = spotifyProfileRepository;
    }

    /**
     * {@code POST  /spotify-profiles} : Create a new spotifyProfile.
     *
     * @param spotifyProfileDTO the spotifyProfileDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new spotifyProfileDTO, or with status {@code 400 (Bad Request)} if the spotifyProfile has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/spotify-profiles")
    public ResponseEntity<SpotifyProfileDTO> createSpotifyProfile(@RequestBody SpotifyProfileDTO spotifyProfileDTO)
        throws URISyntaxException {
        log.debug("REST request to save SpotifyProfile : {}", spotifyProfileDTO);
        if (spotifyProfileDTO.getId() != null) {
            throw new BadRequestAlertException("A new spotifyProfile cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SpotifyProfileDTO result = spotifyProfileService.save(spotifyProfileDTO);
        return ResponseEntity
            .created(new URI("/api/spotify-profiles/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /spotify-profiles/:id} : Updates an existing spotifyProfile.
     *
     * @param id the id of the spotifyProfileDTO to save.
     * @param spotifyProfileDTO the spotifyProfileDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated spotifyProfileDTO,
     * or with status {@code 400 (Bad Request)} if the spotifyProfileDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the spotifyProfileDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/spotify-profiles/{id}")
    public ResponseEntity<SpotifyProfileDTO> updateSpotifyProfile(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody SpotifyProfileDTO spotifyProfileDTO
    ) throws URISyntaxException {
        log.debug("REST request to update SpotifyProfile : {}, {}", id, spotifyProfileDTO);
        if (spotifyProfileDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, spotifyProfileDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!spotifyProfileRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        SpotifyProfileDTO result = spotifyProfileService.update(spotifyProfileDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, spotifyProfileDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /spotify-profiles/:id} : Partial updates given fields of an existing spotifyProfile, field will ignore if it is null
     *
     * @param id the id of the spotifyProfileDTO to save.
     * @param spotifyProfileDTO the spotifyProfileDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated spotifyProfileDTO,
     * or with status {@code 400 (Bad Request)} if the spotifyProfileDTO is not valid,
     * or with status {@code 404 (Not Found)} if the spotifyProfileDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the spotifyProfileDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/spotify-profiles/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<SpotifyProfileDTO> partialUpdateSpotifyProfile(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody SpotifyProfileDTO spotifyProfileDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update SpotifyProfile partially : {}, {}", id, spotifyProfileDTO);
        if (spotifyProfileDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, spotifyProfileDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!spotifyProfileRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SpotifyProfileDTO> result = spotifyProfileService.partialUpdate(spotifyProfileDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, spotifyProfileDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /spotify-profiles} : get all the spotifyProfiles.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of spotifyProfiles in body.
     */
    @GetMapping("/spotify-profiles")
    public List<SpotifyProfileDTO> getAllSpotifyProfiles(@RequestParam(required = false, defaultValue = "false") boolean eagerload) {
        log.debug("REST request to get all SpotifyProfiles");
        return spotifyProfileService.findAll();
    }

    /**
     * {@code GET  /spotify-profiles/:id} : get the "id" spotifyProfile.
     *
     * @param id the id of the spotifyProfileDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the spotifyProfileDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/spotify-profiles/{id}")
    public ResponseEntity<SpotifyProfileDTO> getSpotifyProfile(@PathVariable Long id) {
        log.debug("REST request to get SpotifyProfile : {}", id);
        Optional<SpotifyProfileDTO> spotifyProfileDTO = spotifyProfileService.findOne(id);
        return ResponseUtil.wrapOrNotFound(spotifyProfileDTO);
    }

    /**
     * {@code DELETE  /spotify-profiles/:id} : delete the "id" spotifyProfile.
     *
     * @param id the id of the spotifyProfileDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/spotify-profiles/{id}")
    public ResponseEntity<Void> deleteSpotifyProfile(@PathVariable Long id) {
        log.debug("REST request to delete SpotifyProfile : {}", id);
        spotifyProfileService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
