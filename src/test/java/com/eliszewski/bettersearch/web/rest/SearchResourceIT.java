package com.eliszewski.bettersearch.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.eliszewski.bettersearch.IntegrationTest;
import com.eliszewski.bettersearch.domain.Search;
import com.eliszewski.bettersearch.repository.SearchRepository;
import com.eliszewski.bettersearch.service.SearchService;
import com.eliszewski.bettersearch.service.dto.SearchDTO;
import com.eliszewski.bettersearch.service.mapper.SearchMapper;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link SearchResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class SearchResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Instant DEFAULT_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Boolean DEFAULT_CREATE_PLAYIST = false;
    private static final Boolean UPDATED_CREATE_PLAYIST = true;

    private static final String ENTITY_API_URL = "/api/searches";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SearchRepository searchRepository;

    @Mock
    private SearchRepository searchRepositoryMock;

    @Autowired
    private SearchMapper searchMapper;

    @Mock
    private SearchService searchServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSearchMockMvc;

    private Search search;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Search createEntity(EntityManager em) {
        Search search = new Search().name(DEFAULT_NAME).date(DEFAULT_DATE).createPlayist(DEFAULT_CREATE_PLAYIST);
        return search;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Search createUpdatedEntity(EntityManager em) {
        Search search = new Search().name(UPDATED_NAME).date(UPDATED_DATE).createPlayist(UPDATED_CREATE_PLAYIST);
        return search;
    }

    @BeforeEach
    public void initTest() {
        search = createEntity(em);
    }

    @Test
    @Transactional
    void createSearch() throws Exception {
        int databaseSizeBeforeCreate = searchRepository.findAll().size();
        // Create the Search
        SearchDTO searchDTO = searchMapper.toDto(search);
        restSearchMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(searchDTO)))
            .andExpect(status().isCreated());

        // Validate the Search in the database
        List<Search> searchList = searchRepository.findAll();
        assertThat(searchList).hasSize(databaseSizeBeforeCreate + 1);
        Search testSearch = searchList.get(searchList.size() - 1);
        assertThat(testSearch.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testSearch.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testSearch.getCreatePlayist()).isEqualTo(DEFAULT_CREATE_PLAYIST);
    }

    @Test
    @Transactional
    void createSearchWithExistingId() throws Exception {
        // Create the Search with an existing ID
        search.setId(1L);
        SearchDTO searchDTO = searchMapper.toDto(search);

        int databaseSizeBeforeCreate = searchRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSearchMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(searchDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Search in the database
        List<Search> searchList = searchRepository.findAll();
        assertThat(searchList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = searchRepository.findAll().size();
        // set the field null
        search.setName(null);

        // Create the Search, which fails.
        SearchDTO searchDTO = searchMapper.toDto(search);

        restSearchMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(searchDTO)))
            .andExpect(status().isBadRequest());

        List<Search> searchList = searchRepository.findAll();
        assertThat(searchList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = searchRepository.findAll().size();
        // set the field null
        search.setDate(null);

        // Create the Search, which fails.
        SearchDTO searchDTO = searchMapper.toDto(search);

        restSearchMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(searchDTO)))
            .andExpect(status().isBadRequest());

        List<Search> searchList = searchRepository.findAll();
        assertThat(searchList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatePlayistIsRequired() throws Exception {
        int databaseSizeBeforeTest = searchRepository.findAll().size();
        // set the field null
        search.setCreatePlayist(null);

        // Create the Search, which fails.
        SearchDTO searchDTO = searchMapper.toDto(search);

        restSearchMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(searchDTO)))
            .andExpect(status().isBadRequest());

        List<Search> searchList = searchRepository.findAll();
        assertThat(searchList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllSearches() throws Exception {
        // Initialize the database
        searchRepository.saveAndFlush(search);

        // Get all the searchList
        restSearchMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(search.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].createPlayist").value(hasItem(DEFAULT_CREATE_PLAYIST.booleanValue())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllSearchesWithEagerRelationshipsIsEnabled() throws Exception {
        when(searchServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restSearchMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(searchServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllSearchesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(searchServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restSearchMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(searchRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getSearch() throws Exception {
        // Initialize the database
        searchRepository.saveAndFlush(search);

        // Get the search
        restSearchMockMvc
            .perform(get(ENTITY_API_URL_ID, search.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(search.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()))
            .andExpect(jsonPath("$.createPlayist").value(DEFAULT_CREATE_PLAYIST.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingSearch() throws Exception {
        // Get the search
        restSearchMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingSearch() throws Exception {
        // Initialize the database
        searchRepository.saveAndFlush(search);

        int databaseSizeBeforeUpdate = searchRepository.findAll().size();

        // Update the search
        Search updatedSearch = searchRepository.findById(search.getId()).get();
        // Disconnect from session so that the updates on updatedSearch are not directly saved in db
        em.detach(updatedSearch);
        updatedSearch.name(UPDATED_NAME).date(UPDATED_DATE).createPlayist(UPDATED_CREATE_PLAYIST);
        SearchDTO searchDTO = searchMapper.toDto(updatedSearch);

        restSearchMockMvc
            .perform(
                put(ENTITY_API_URL_ID, searchDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(searchDTO))
            )
            .andExpect(status().isOk());

        // Validate the Search in the database
        List<Search> searchList = searchRepository.findAll();
        assertThat(searchList).hasSize(databaseSizeBeforeUpdate);
        Search testSearch = searchList.get(searchList.size() - 1);
        assertThat(testSearch.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testSearch.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testSearch.getCreatePlayist()).isEqualTo(UPDATED_CREATE_PLAYIST);
    }

    @Test
    @Transactional
    void putNonExistingSearch() throws Exception {
        int databaseSizeBeforeUpdate = searchRepository.findAll().size();
        search.setId(count.incrementAndGet());

        // Create the Search
        SearchDTO searchDTO = searchMapper.toDto(search);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSearchMockMvc
            .perform(
                put(ENTITY_API_URL_ID, searchDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(searchDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Search in the database
        List<Search> searchList = searchRepository.findAll();
        assertThat(searchList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSearch() throws Exception {
        int databaseSizeBeforeUpdate = searchRepository.findAll().size();
        search.setId(count.incrementAndGet());

        // Create the Search
        SearchDTO searchDTO = searchMapper.toDto(search);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSearchMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(searchDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Search in the database
        List<Search> searchList = searchRepository.findAll();
        assertThat(searchList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSearch() throws Exception {
        int databaseSizeBeforeUpdate = searchRepository.findAll().size();
        search.setId(count.incrementAndGet());

        // Create the Search
        SearchDTO searchDTO = searchMapper.toDto(search);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSearchMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(searchDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Search in the database
        List<Search> searchList = searchRepository.findAll();
        assertThat(searchList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSearchWithPatch() throws Exception {
        // Initialize the database
        searchRepository.saveAndFlush(search);

        int databaseSizeBeforeUpdate = searchRepository.findAll().size();

        // Update the search using partial update
        Search partialUpdatedSearch = new Search();
        partialUpdatedSearch.setId(search.getId());

        partialUpdatedSearch.date(UPDATED_DATE).createPlayist(UPDATED_CREATE_PLAYIST);

        restSearchMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSearch.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSearch))
            )
            .andExpect(status().isOk());

        // Validate the Search in the database
        List<Search> searchList = searchRepository.findAll();
        assertThat(searchList).hasSize(databaseSizeBeforeUpdate);
        Search testSearch = searchList.get(searchList.size() - 1);
        assertThat(testSearch.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testSearch.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testSearch.getCreatePlayist()).isEqualTo(UPDATED_CREATE_PLAYIST);
    }

    @Test
    @Transactional
    void fullUpdateSearchWithPatch() throws Exception {
        // Initialize the database
        searchRepository.saveAndFlush(search);

        int databaseSizeBeforeUpdate = searchRepository.findAll().size();

        // Update the search using partial update
        Search partialUpdatedSearch = new Search();
        partialUpdatedSearch.setId(search.getId());

        partialUpdatedSearch.name(UPDATED_NAME).date(UPDATED_DATE).createPlayist(UPDATED_CREATE_PLAYIST);

        restSearchMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSearch.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSearch))
            )
            .andExpect(status().isOk());

        // Validate the Search in the database
        List<Search> searchList = searchRepository.findAll();
        assertThat(searchList).hasSize(databaseSizeBeforeUpdate);
        Search testSearch = searchList.get(searchList.size() - 1);
        assertThat(testSearch.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testSearch.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testSearch.getCreatePlayist()).isEqualTo(UPDATED_CREATE_PLAYIST);
    }

    @Test
    @Transactional
    void patchNonExistingSearch() throws Exception {
        int databaseSizeBeforeUpdate = searchRepository.findAll().size();
        search.setId(count.incrementAndGet());

        // Create the Search
        SearchDTO searchDTO = searchMapper.toDto(search);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSearchMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, searchDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(searchDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Search in the database
        List<Search> searchList = searchRepository.findAll();
        assertThat(searchList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSearch() throws Exception {
        int databaseSizeBeforeUpdate = searchRepository.findAll().size();
        search.setId(count.incrementAndGet());

        // Create the Search
        SearchDTO searchDTO = searchMapper.toDto(search);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSearchMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(searchDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Search in the database
        List<Search> searchList = searchRepository.findAll();
        assertThat(searchList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSearch() throws Exception {
        int databaseSizeBeforeUpdate = searchRepository.findAll().size();
        search.setId(count.incrementAndGet());

        // Create the Search
        SearchDTO searchDTO = searchMapper.toDto(search);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSearchMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(searchDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Search in the database
        List<Search> searchList = searchRepository.findAll();
        assertThat(searchList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSearch() throws Exception {
        // Initialize the database
        searchRepository.saveAndFlush(search);

        int databaseSizeBeforeDelete = searchRepository.findAll().size();

        // Delete the search
        restSearchMockMvc
            .perform(delete(ENTITY_API_URL_ID, search.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Search> searchList = searchRepository.findAll();
        assertThat(searchList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
