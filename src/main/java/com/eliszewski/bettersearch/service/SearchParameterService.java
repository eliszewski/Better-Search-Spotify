package com.eliszewski.bettersearch.service;

import com.eliszewski.bettersearch.domain.SearchParameter;
import com.eliszewski.bettersearch.repository.SearchParameterRepository;
import com.eliszewski.bettersearch.service.dto.SearchParameterDTO;
import com.eliszewski.bettersearch.service.mapper.SearchParameterMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link SearchParameter}.
 */
@Service
@Transactional
public class SearchParameterService {

    private final Logger log = LoggerFactory.getLogger(SearchParameterService.class);

    private final SearchParameterRepository searchParameterRepository;

    private final SearchParameterMapper searchParameterMapper;

    public SearchParameterService(SearchParameterRepository searchParameterRepository, SearchParameterMapper searchParameterMapper) {
        this.searchParameterRepository = searchParameterRepository;
        this.searchParameterMapper = searchParameterMapper;
    }

    /**
     * Save a searchParameter.
     *
     * @param searchParameterDTO the entity to save.
     * @return the persisted entity.
     */
    public SearchParameterDTO save(SearchParameterDTO searchParameterDTO) {
        log.debug("Request to save SearchParameter : {}", searchParameterDTO);
        SearchParameter searchParameter = searchParameterMapper.toEntity(searchParameterDTO);
        searchParameter = searchParameterRepository.save(searchParameter);
        return searchParameterMapper.toDto(searchParameter);
    }

    /**
     * Update a searchParameter.
     *
     * @param searchParameterDTO the entity to save.
     * @return the persisted entity.
     */
    public SearchParameterDTO update(SearchParameterDTO searchParameterDTO) {
        log.debug("Request to update SearchParameter : {}", searchParameterDTO);
        SearchParameter searchParameter = searchParameterMapper.toEntity(searchParameterDTO);
        searchParameter = searchParameterRepository.save(searchParameter);
        return searchParameterMapper.toDto(searchParameter);
    }

    /**
     * Partially update a searchParameter.
     *
     * @param searchParameterDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<SearchParameterDTO> partialUpdate(SearchParameterDTO searchParameterDTO) {
        log.debug("Request to partially update SearchParameter : {}", searchParameterDTO);

        return searchParameterRepository
            .findById(searchParameterDTO.getId())
            .map(existingSearchParameter -> {
                searchParameterMapper.partialUpdate(existingSearchParameter, searchParameterDTO);

                return existingSearchParameter;
            })
            .map(searchParameterRepository::save)
            .map(searchParameterMapper::toDto);
    }

    /**
     * Get all the searchParameters.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<SearchParameterDTO> findAll() {
        log.debug("Request to get all SearchParameters");
        return searchParameterRepository
            .findAll()
            .stream()
            .map(searchParameterMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one searchParameter by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<SearchParameterDTO> findOne(Long id) {
        log.debug("Request to get SearchParameter : {}", id);
        return searchParameterRepository.findById(id).map(searchParameterMapper::toDto);
    }

    /**
     * Delete the searchParameter by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete SearchParameter : {}", id);
        searchParameterRepository.deleteById(id);
    }
}
