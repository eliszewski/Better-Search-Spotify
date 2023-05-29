package com.eliszewski.bettersearch.service;

import com.eliszewski.bettersearch.domain.Search;
import com.eliszewski.bettersearch.repository.SearchRepository;
import com.eliszewski.bettersearch.service.dto.SearchDTO;
import com.eliszewski.bettersearch.service.mapper.SearchMapper;
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
 * Service Implementation for managing {@link Search}.
 */
@Service
@Transactional
public class SearchService {

    private final Logger log = LoggerFactory.getLogger(SearchService.class);

    private final SearchRepository searchRepository;

    private final SearchMapper searchMapper;

    public SearchService(SearchRepository searchRepository, SearchMapper searchMapper) {
        this.searchRepository = searchRepository;
        this.searchMapper = searchMapper;
    }

    /**
     * Save a search.
     *
     * @param searchDTO the entity to save.
     * @return the persisted entity.
     */
    public SearchDTO save(SearchDTO searchDTO) {
        log.debug("Request to save Search : {}", searchDTO);
        Search search = searchMapper.toEntity(searchDTO);
        search = searchRepository.save(search);
        return searchMapper.toDto(search);
    }

    /**
     * Update a search.
     *
     * @param searchDTO the entity to save.
     * @return the persisted entity.
     */
    public SearchDTO update(SearchDTO searchDTO) {
        log.debug("Request to update Search : {}", searchDTO);
        Search search = searchMapper.toEntity(searchDTO);
        search = searchRepository.save(search);
        return searchMapper.toDto(search);
    }

    /**
     * Partially update a search.
     *
     * @param searchDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<SearchDTO> partialUpdate(SearchDTO searchDTO) {
        log.debug("Request to partially update Search : {}", searchDTO);

        return searchRepository
            .findById(searchDTO.getId())
            .map(existingSearch -> {
                searchMapper.partialUpdate(existingSearch, searchDTO);

                return existingSearch;
            })
            .map(searchRepository::save)
            .map(searchMapper::toDto);
    }

    /**
     * Get all the searches.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<SearchDTO> findAll() {
        log.debug("Request to get all Searches");
        return searchRepository.findAll().stream().map(searchMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get all the searches with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<SearchDTO> findAllWithEagerRelationships(Pageable pageable) {
        return searchRepository.findAllWithEagerRelationships(pageable).map(searchMapper::toDto);
    }

    /**
     * Get one search by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<SearchDTO> findOne(Long id) {
        log.debug("Request to get Search : {}", id);
        return searchRepository.findOneWithEagerRelationships(id).map(searchMapper::toDto);
    }

    /**
     * Delete the search by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Search : {}", id);
        searchRepository.deleteById(id);
    }
}
