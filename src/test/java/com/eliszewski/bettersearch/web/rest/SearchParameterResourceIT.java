package com.eliszewski.bettersearch.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.eliszewski.bettersearch.IntegrationTest;
import com.eliszewski.bettersearch.domain.SearchParameter;
import com.eliszewski.bettersearch.domain.enumeration.AlbumType;
import com.eliszewski.bettersearch.domain.enumeration.SearchParameterAttribute;
import com.eliszewski.bettersearch.domain.enumeration.SearchParameterType;
import com.eliszewski.bettersearch.repository.SearchParameterRepository;
import com.eliszewski.bettersearch.service.dto.SearchParameterDTO;
import com.eliszewski.bettersearch.service.mapper.SearchParameterMapper;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link SearchParameterResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SearchParameterResourceIT {

    private static final SearchParameterType DEFAULT_TYPE = SearchParameterType.STARTSWITH;
    private static final SearchParameterType UPDATED_TYPE = SearchParameterType.CONTAINS;

    private static final SearchParameterAttribute DEFAULT_ATTRIBUTE_NAME = SearchParameterAttribute.ALBUM;
    private static final SearchParameterAttribute UPDATED_ATTRIBUTE_NAME = SearchParameterAttribute.TITLE;

    private static final AlbumType DEFAULT_ALBUM_TYPE = AlbumType.ALBUM;
    private static final AlbumType UPDATED_ALBUM_TYPE = AlbumType.COMPILATION;

    private static final String DEFAULT_SEARCH_VALUE = "AAAAAAAAAA";
    private static final String UPDATED_SEARCH_VALUE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/search-parameters";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SearchParameterRepository searchParameterRepository;

    @Autowired
    private SearchParameterMapper searchParameterMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSearchParameterMockMvc;

    private SearchParameter searchParameter;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SearchParameter createEntity(EntityManager em) {
        SearchParameter searchParameter = new SearchParameter()
            .type(DEFAULT_TYPE)
            .attributeName(DEFAULT_ATTRIBUTE_NAME)
            .albumType(DEFAULT_ALBUM_TYPE)
            .searchValue(DEFAULT_SEARCH_VALUE);
        return searchParameter;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SearchParameter createUpdatedEntity(EntityManager em) {
        SearchParameter searchParameter = new SearchParameter()
            .type(UPDATED_TYPE)
            .attributeName(UPDATED_ATTRIBUTE_NAME)
            .albumType(UPDATED_ALBUM_TYPE)
            .searchValue(UPDATED_SEARCH_VALUE);
        return searchParameter;
    }

    @BeforeEach
    public void initTest() {
        searchParameter = createEntity(em);
    }

    @Test
    @Transactional
    void createSearchParameter() throws Exception {
        int databaseSizeBeforeCreate = searchParameterRepository.findAll().size();
        // Create the SearchParameter
        SearchParameterDTO searchParameterDTO = searchParameterMapper.toDto(searchParameter);
        restSearchParameterMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(searchParameterDTO))
            )
            .andExpect(status().isCreated());

        // Validate the SearchParameter in the database
        List<SearchParameter> searchParameterList = searchParameterRepository.findAll();
        assertThat(searchParameterList).hasSize(databaseSizeBeforeCreate + 1);
        SearchParameter testSearchParameter = searchParameterList.get(searchParameterList.size() - 1);
        assertThat(testSearchParameter.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testSearchParameter.getAttributeName()).isEqualTo(DEFAULT_ATTRIBUTE_NAME);
        assertThat(testSearchParameter.getAlbumType()).isEqualTo(DEFAULT_ALBUM_TYPE);
        assertThat(testSearchParameter.getSearchValue()).isEqualTo(DEFAULT_SEARCH_VALUE);
    }

    @Test
    @Transactional
    void createSearchParameterWithExistingId() throws Exception {
        // Create the SearchParameter with an existing ID
        searchParameter.setId(1L);
        SearchParameterDTO searchParameterDTO = searchParameterMapper.toDto(searchParameter);

        int databaseSizeBeforeCreate = searchParameterRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSearchParameterMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(searchParameterDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SearchParameter in the database
        List<SearchParameter> searchParameterList = searchParameterRepository.findAll();
        assertThat(searchParameterList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllSearchParameters() throws Exception {
        // Initialize the database
        searchParameterRepository.saveAndFlush(searchParameter);

        // Get all the searchParameterList
        restSearchParameterMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(searchParameter.getId().intValue())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].attributeName").value(hasItem(DEFAULT_ATTRIBUTE_NAME.toString())))
            .andExpect(jsonPath("$.[*].albumType").value(hasItem(DEFAULT_ALBUM_TYPE.toString())))
            .andExpect(jsonPath("$.[*].searchValue").value(hasItem(DEFAULT_SEARCH_VALUE)));
    }

    @Test
    @Transactional
    void getSearchParameter() throws Exception {
        // Initialize the database
        searchParameterRepository.saveAndFlush(searchParameter);

        // Get the searchParameter
        restSearchParameterMockMvc
            .perform(get(ENTITY_API_URL_ID, searchParameter.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(searchParameter.getId().intValue()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.attributeName").value(DEFAULT_ATTRIBUTE_NAME.toString()))
            .andExpect(jsonPath("$.albumType").value(DEFAULT_ALBUM_TYPE.toString()))
            .andExpect(jsonPath("$.searchValue").value(DEFAULT_SEARCH_VALUE));
    }

    @Test
    @Transactional
    void getNonExistingSearchParameter() throws Exception {
        // Get the searchParameter
        restSearchParameterMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingSearchParameter() throws Exception {
        // Initialize the database
        searchParameterRepository.saveAndFlush(searchParameter);

        int databaseSizeBeforeUpdate = searchParameterRepository.findAll().size();

        // Update the searchParameter
        SearchParameter updatedSearchParameter = searchParameterRepository.findById(searchParameter.getId()).get();
        // Disconnect from session so that the updates on updatedSearchParameter are not directly saved in db
        em.detach(updatedSearchParameter);
        updatedSearchParameter
            .type(UPDATED_TYPE)
            .attributeName(UPDATED_ATTRIBUTE_NAME)
            .albumType(UPDATED_ALBUM_TYPE)
            .searchValue(UPDATED_SEARCH_VALUE);
        SearchParameterDTO searchParameterDTO = searchParameterMapper.toDto(updatedSearchParameter);

        restSearchParameterMockMvc
            .perform(
                put(ENTITY_API_URL_ID, searchParameterDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(searchParameterDTO))
            )
            .andExpect(status().isOk());

        // Validate the SearchParameter in the database
        List<SearchParameter> searchParameterList = searchParameterRepository.findAll();
        assertThat(searchParameterList).hasSize(databaseSizeBeforeUpdate);
        SearchParameter testSearchParameter = searchParameterList.get(searchParameterList.size() - 1);
        assertThat(testSearchParameter.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testSearchParameter.getAttributeName()).isEqualTo(UPDATED_ATTRIBUTE_NAME);
        assertThat(testSearchParameter.getAlbumType()).isEqualTo(UPDATED_ALBUM_TYPE);
        assertThat(testSearchParameter.getSearchValue()).isEqualTo(UPDATED_SEARCH_VALUE);
    }

    @Test
    @Transactional
    void putNonExistingSearchParameter() throws Exception {
        int databaseSizeBeforeUpdate = searchParameterRepository.findAll().size();
        searchParameter.setId(count.incrementAndGet());

        // Create the SearchParameter
        SearchParameterDTO searchParameterDTO = searchParameterMapper.toDto(searchParameter);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSearchParameterMockMvc
            .perform(
                put(ENTITY_API_URL_ID, searchParameterDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(searchParameterDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SearchParameter in the database
        List<SearchParameter> searchParameterList = searchParameterRepository.findAll();
        assertThat(searchParameterList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSearchParameter() throws Exception {
        int databaseSizeBeforeUpdate = searchParameterRepository.findAll().size();
        searchParameter.setId(count.incrementAndGet());

        // Create the SearchParameter
        SearchParameterDTO searchParameterDTO = searchParameterMapper.toDto(searchParameter);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSearchParameterMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(searchParameterDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SearchParameter in the database
        List<SearchParameter> searchParameterList = searchParameterRepository.findAll();
        assertThat(searchParameterList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSearchParameter() throws Exception {
        int databaseSizeBeforeUpdate = searchParameterRepository.findAll().size();
        searchParameter.setId(count.incrementAndGet());

        // Create the SearchParameter
        SearchParameterDTO searchParameterDTO = searchParameterMapper.toDto(searchParameter);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSearchParameterMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(searchParameterDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SearchParameter in the database
        List<SearchParameter> searchParameterList = searchParameterRepository.findAll();
        assertThat(searchParameterList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSearchParameterWithPatch() throws Exception {
        // Initialize the database
        searchParameterRepository.saveAndFlush(searchParameter);

        int databaseSizeBeforeUpdate = searchParameterRepository.findAll().size();

        // Update the searchParameter using partial update
        SearchParameter partialUpdatedSearchParameter = new SearchParameter();
        partialUpdatedSearchParameter.setId(searchParameter.getId());

        partialUpdatedSearchParameter.type(UPDATED_TYPE);

        restSearchParameterMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSearchParameter.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSearchParameter))
            )
            .andExpect(status().isOk());

        // Validate the SearchParameter in the database
        List<SearchParameter> searchParameterList = searchParameterRepository.findAll();
        assertThat(searchParameterList).hasSize(databaseSizeBeforeUpdate);
        SearchParameter testSearchParameter = searchParameterList.get(searchParameterList.size() - 1);
        assertThat(testSearchParameter.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testSearchParameter.getAttributeName()).isEqualTo(DEFAULT_ATTRIBUTE_NAME);
        assertThat(testSearchParameter.getAlbumType()).isEqualTo(DEFAULT_ALBUM_TYPE);
        assertThat(testSearchParameter.getSearchValue()).isEqualTo(DEFAULT_SEARCH_VALUE);
    }

    @Test
    @Transactional
    void fullUpdateSearchParameterWithPatch() throws Exception {
        // Initialize the database
        searchParameterRepository.saveAndFlush(searchParameter);

        int databaseSizeBeforeUpdate = searchParameterRepository.findAll().size();

        // Update the searchParameter using partial update
        SearchParameter partialUpdatedSearchParameter = new SearchParameter();
        partialUpdatedSearchParameter.setId(searchParameter.getId());

        partialUpdatedSearchParameter
            .type(UPDATED_TYPE)
            .attributeName(UPDATED_ATTRIBUTE_NAME)
            .albumType(UPDATED_ALBUM_TYPE)
            .searchValue(UPDATED_SEARCH_VALUE);

        restSearchParameterMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSearchParameter.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSearchParameter))
            )
            .andExpect(status().isOk());

        // Validate the SearchParameter in the database
        List<SearchParameter> searchParameterList = searchParameterRepository.findAll();
        assertThat(searchParameterList).hasSize(databaseSizeBeforeUpdate);
        SearchParameter testSearchParameter = searchParameterList.get(searchParameterList.size() - 1);
        assertThat(testSearchParameter.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testSearchParameter.getAttributeName()).isEqualTo(UPDATED_ATTRIBUTE_NAME);
        assertThat(testSearchParameter.getAlbumType()).isEqualTo(UPDATED_ALBUM_TYPE);
        assertThat(testSearchParameter.getSearchValue()).isEqualTo(UPDATED_SEARCH_VALUE);
    }

    @Test
    @Transactional
    void patchNonExistingSearchParameter() throws Exception {
        int databaseSizeBeforeUpdate = searchParameterRepository.findAll().size();
        searchParameter.setId(count.incrementAndGet());

        // Create the SearchParameter
        SearchParameterDTO searchParameterDTO = searchParameterMapper.toDto(searchParameter);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSearchParameterMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, searchParameterDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(searchParameterDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SearchParameter in the database
        List<SearchParameter> searchParameterList = searchParameterRepository.findAll();
        assertThat(searchParameterList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSearchParameter() throws Exception {
        int databaseSizeBeforeUpdate = searchParameterRepository.findAll().size();
        searchParameter.setId(count.incrementAndGet());

        // Create the SearchParameter
        SearchParameterDTO searchParameterDTO = searchParameterMapper.toDto(searchParameter);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSearchParameterMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(searchParameterDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SearchParameter in the database
        List<SearchParameter> searchParameterList = searchParameterRepository.findAll();
        assertThat(searchParameterList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSearchParameter() throws Exception {
        int databaseSizeBeforeUpdate = searchParameterRepository.findAll().size();
        searchParameter.setId(count.incrementAndGet());

        // Create the SearchParameter
        SearchParameterDTO searchParameterDTO = searchParameterMapper.toDto(searchParameter);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSearchParameterMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(searchParameterDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SearchParameter in the database
        List<SearchParameter> searchParameterList = searchParameterRepository.findAll();
        assertThat(searchParameterList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSearchParameter() throws Exception {
        // Initialize the database
        searchParameterRepository.saveAndFlush(searchParameter);

        int databaseSizeBeforeDelete = searchParameterRepository.findAll().size();

        // Delete the searchParameter
        restSearchParameterMockMvc
            .perform(delete(ENTITY_API_URL_ID, searchParameter.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<SearchParameter> searchParameterList = searchParameterRepository.findAll();
        assertThat(searchParameterList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
