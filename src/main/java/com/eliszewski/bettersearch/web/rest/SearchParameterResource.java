package com.eliszewski.bettersearch.web.rest;

import com.eliszewski.bettersearch.repository.SearchParameterRepository;
import com.eliszewski.bettersearch.service.SearchParameterService;
import com.eliszewski.bettersearch.service.dto.SearchParameterDTO;
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
 * REST controller for managing {@link com.eliszewski.bettersearch.domain.SearchParameter}.
 */
@RestController
@RequestMapping("/api")
public class SearchParameterResource {

    private final Logger log = LoggerFactory.getLogger(SearchParameterResource.class);

    private static final String ENTITY_NAME = "searchParameter";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SearchParameterService searchParameterService;

    private final SearchParameterRepository searchParameterRepository;

    public SearchParameterResource(SearchParameterService searchParameterService, SearchParameterRepository searchParameterRepository) {
        this.searchParameterService = searchParameterService;
        this.searchParameterRepository = searchParameterRepository;
    }

    /**
     * {@code POST  /search-parameters} : Create a new searchParameter.
     *
     * @param searchParameterDTO the searchParameterDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new searchParameterDTO, or with status {@code 400 (Bad Request)} if the searchParameter has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/search-parameters")
    public ResponseEntity<SearchParameterDTO> createSearchParameter(@RequestBody SearchParameterDTO searchParameterDTO)
        throws URISyntaxException {
        log.debug("REST request to save SearchParameter : {}", searchParameterDTO);
        if (searchParameterDTO.getId() != null) {
            throw new BadRequestAlertException("A new searchParameter cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SearchParameterDTO result = searchParameterService.save(searchParameterDTO);
        return ResponseEntity
            .created(new URI("/api/search-parameters/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /search-parameters/:id} : Updates an existing searchParameter.
     *
     * @param id the id of the searchParameterDTO to save.
     * @param searchParameterDTO the searchParameterDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated searchParameterDTO,
     * or with status {@code 400 (Bad Request)} if the searchParameterDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the searchParameterDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/search-parameters/{id}")
    public ResponseEntity<SearchParameterDTO> updateSearchParameter(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody SearchParameterDTO searchParameterDTO
    ) throws URISyntaxException {
        log.debug("REST request to update SearchParameter : {}, {}", id, searchParameterDTO);
        if (searchParameterDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, searchParameterDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!searchParameterRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        SearchParameterDTO result = searchParameterService.update(searchParameterDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, searchParameterDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /search-parameters/:id} : Partial updates given fields of an existing searchParameter, field will ignore if it is null
     *
     * @param id the id of the searchParameterDTO to save.
     * @param searchParameterDTO the searchParameterDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated searchParameterDTO,
     * or with status {@code 400 (Bad Request)} if the searchParameterDTO is not valid,
     * or with status {@code 404 (Not Found)} if the searchParameterDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the searchParameterDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/search-parameters/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<SearchParameterDTO> partialUpdateSearchParameter(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody SearchParameterDTO searchParameterDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update SearchParameter partially : {}, {}", id, searchParameterDTO);
        if (searchParameterDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, searchParameterDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!searchParameterRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SearchParameterDTO> result = searchParameterService.partialUpdate(searchParameterDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, searchParameterDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /search-parameters} : get all the searchParameters.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of searchParameters in body.
     */
    @GetMapping("/search-parameters")
    public List<SearchParameterDTO> getAllSearchParameters() {
        log.debug("REST request to get all SearchParameters");
        return searchParameterService.findAll();
    }

    /**
     * {@code GET  /search-parameters/:id} : get the "id" searchParameter.
     *
     * @param id the id of the searchParameterDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the searchParameterDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/search-parameters/{id}")
    public ResponseEntity<SearchParameterDTO> getSearchParameter(@PathVariable Long id) {
        log.debug("REST request to get SearchParameter : {}", id);
        Optional<SearchParameterDTO> searchParameterDTO = searchParameterService.findOne(id);
        return ResponseUtil.wrapOrNotFound(searchParameterDTO);
    }

    /**
     * {@code DELETE  /search-parameters/:id} : delete the "id" searchParameter.
     *
     * @param id the id of the searchParameterDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/search-parameters/{id}")
    public ResponseEntity<Void> deleteSearchParameter(@PathVariable Long id) {
        log.debug("REST request to delete SearchParameter : {}", id);
        searchParameterService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
