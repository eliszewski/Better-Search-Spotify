package com.eliszewski.bettersearch.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.eliszewski.bettersearch.IntegrationTest;
import com.eliszewski.bettersearch.domain.SpotifyProfile;
import com.eliszewski.bettersearch.repository.SpotifyProfileRepository;
import com.eliszewski.bettersearch.service.SpotifyProfileService;
import com.eliszewski.bettersearch.service.dto.SpotifyProfileDTO;
import com.eliszewski.bettersearch.service.mapper.SpotifyProfileMapper;
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
 * Integration tests for the {@link SpotifyProfileResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class SpotifyProfileResourceIT {

    private static final String DEFAULT_ACCESS_TOKEN = "AAAAAAAAAA";
    private static final String UPDATED_ACCESS_TOKEN = "BBBBBBBBBB";

    private static final String DEFAULT_REFRESH_TOKEN = "AAAAAAAAAA";
    private static final String UPDATED_REFRESH_TOKEN = "BBBBBBBBBB";

    private static final Instant DEFAULT_ACCESS_TOKEN_EXPIRATION = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_ACCESS_TOKEN_EXPIRATION = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_REFRESH_TOKEN_EXPIRATION = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_REFRESH_TOKEN_EXPIRATION = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/spotify-profiles";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SpotifyProfileRepository spotifyProfileRepository;

    @Mock
    private SpotifyProfileRepository spotifyProfileRepositoryMock;

    @Autowired
    private SpotifyProfileMapper spotifyProfileMapper;

    @Mock
    private SpotifyProfileService spotifyProfileServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSpotifyProfileMockMvc;

    private SpotifyProfile spotifyProfile;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SpotifyProfile createEntity(EntityManager em) {
        SpotifyProfile spotifyProfile = new SpotifyProfile()
            .accessToken(DEFAULT_ACCESS_TOKEN)
            .refreshToken(DEFAULT_REFRESH_TOKEN)
            .accessTokenExpiration(DEFAULT_ACCESS_TOKEN_EXPIRATION)
            .refreshTokenExpiration(DEFAULT_REFRESH_TOKEN_EXPIRATION);
        return spotifyProfile;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SpotifyProfile createUpdatedEntity(EntityManager em) {
        SpotifyProfile spotifyProfile = new SpotifyProfile()
            .accessToken(UPDATED_ACCESS_TOKEN)
            .refreshToken(UPDATED_REFRESH_TOKEN)
            .accessTokenExpiration(UPDATED_ACCESS_TOKEN_EXPIRATION)
            .refreshTokenExpiration(UPDATED_REFRESH_TOKEN_EXPIRATION);
        return spotifyProfile;
    }

    @BeforeEach
    public void initTest() {
        spotifyProfile = createEntity(em);
    }

    @Test
    @Transactional
    void createSpotifyProfile() throws Exception {
        int databaseSizeBeforeCreate = spotifyProfileRepository.findAll().size();
        // Create the SpotifyProfile
        SpotifyProfileDTO spotifyProfileDTO = spotifyProfileMapper.toDto(spotifyProfile);
        restSpotifyProfileMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(spotifyProfileDTO))
            )
            .andExpect(status().isCreated());

        // Validate the SpotifyProfile in the database
        List<SpotifyProfile> spotifyProfileList = spotifyProfileRepository.findAll();
        assertThat(spotifyProfileList).hasSize(databaseSizeBeforeCreate + 1);
        SpotifyProfile testSpotifyProfile = spotifyProfileList.get(spotifyProfileList.size() - 1);
        assertThat(testSpotifyProfile.getAccessToken()).isEqualTo(DEFAULT_ACCESS_TOKEN);
        assertThat(testSpotifyProfile.getRefreshToken()).isEqualTo(DEFAULT_REFRESH_TOKEN);
        assertThat(testSpotifyProfile.getAccessTokenExpiration()).isEqualTo(DEFAULT_ACCESS_TOKEN_EXPIRATION);
        assertThat(testSpotifyProfile.getRefreshTokenExpiration()).isEqualTo(DEFAULT_REFRESH_TOKEN_EXPIRATION);
    }

    @Test
    @Transactional
    void createSpotifyProfileWithExistingId() throws Exception {
        // Create the SpotifyProfile with an existing ID
        spotifyProfile.setId(1L);
        SpotifyProfileDTO spotifyProfileDTO = spotifyProfileMapper.toDto(spotifyProfile);

        int databaseSizeBeforeCreate = spotifyProfileRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSpotifyProfileMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(spotifyProfileDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SpotifyProfile in the database
        List<SpotifyProfile> spotifyProfileList = spotifyProfileRepository.findAll();
        assertThat(spotifyProfileList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllSpotifyProfiles() throws Exception {
        // Initialize the database
        spotifyProfileRepository.saveAndFlush(spotifyProfile);

        // Get all the spotifyProfileList
        restSpotifyProfileMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(spotifyProfile.getId().intValue())))
            .andExpect(jsonPath("$.[*].accessToken").value(hasItem(DEFAULT_ACCESS_TOKEN)))
            .andExpect(jsonPath("$.[*].refreshToken").value(hasItem(DEFAULT_REFRESH_TOKEN)))
            .andExpect(jsonPath("$.[*].accessTokenExpiration").value(hasItem(DEFAULT_ACCESS_TOKEN_EXPIRATION.toString())))
            .andExpect(jsonPath("$.[*].refreshTokenExpiration").value(hasItem(DEFAULT_REFRESH_TOKEN_EXPIRATION.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllSpotifyProfilesWithEagerRelationshipsIsEnabled() throws Exception {
        when(spotifyProfileServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restSpotifyProfileMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(spotifyProfileServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllSpotifyProfilesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(spotifyProfileServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restSpotifyProfileMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(spotifyProfileRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getSpotifyProfile() throws Exception {
        // Initialize the database
        spotifyProfileRepository.saveAndFlush(spotifyProfile);

        // Get the spotifyProfile
        restSpotifyProfileMockMvc
            .perform(get(ENTITY_API_URL_ID, spotifyProfile.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(spotifyProfile.getId().intValue()))
            .andExpect(jsonPath("$.accessToken").value(DEFAULT_ACCESS_TOKEN))
            .andExpect(jsonPath("$.refreshToken").value(DEFAULT_REFRESH_TOKEN))
            .andExpect(jsonPath("$.accessTokenExpiration").value(DEFAULT_ACCESS_TOKEN_EXPIRATION.toString()))
            .andExpect(jsonPath("$.refreshTokenExpiration").value(DEFAULT_REFRESH_TOKEN_EXPIRATION.toString()));
    }

    @Test
    @Transactional
    void getNonExistingSpotifyProfile() throws Exception {
        // Get the spotifyProfile
        restSpotifyProfileMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingSpotifyProfile() throws Exception {
        // Initialize the database
        spotifyProfileRepository.saveAndFlush(spotifyProfile);

        int databaseSizeBeforeUpdate = spotifyProfileRepository.findAll().size();

        // Update the spotifyProfile
        SpotifyProfile updatedSpotifyProfile = spotifyProfileRepository.findById(spotifyProfile.getId()).get();
        // Disconnect from session so that the updates on updatedSpotifyProfile are not directly saved in db
        em.detach(updatedSpotifyProfile);
        updatedSpotifyProfile
            .accessToken(UPDATED_ACCESS_TOKEN)
            .refreshToken(UPDATED_REFRESH_TOKEN)
            .accessTokenExpiration(UPDATED_ACCESS_TOKEN_EXPIRATION)
            .refreshTokenExpiration(UPDATED_REFRESH_TOKEN_EXPIRATION);
        SpotifyProfileDTO spotifyProfileDTO = spotifyProfileMapper.toDto(updatedSpotifyProfile);

        restSpotifyProfileMockMvc
            .perform(
                put(ENTITY_API_URL_ID, spotifyProfileDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(spotifyProfileDTO))
            )
            .andExpect(status().isOk());

        // Validate the SpotifyProfile in the database
        List<SpotifyProfile> spotifyProfileList = spotifyProfileRepository.findAll();
        assertThat(spotifyProfileList).hasSize(databaseSizeBeforeUpdate);
        SpotifyProfile testSpotifyProfile = spotifyProfileList.get(spotifyProfileList.size() - 1);
        assertThat(testSpotifyProfile.getAccessToken()).isEqualTo(UPDATED_ACCESS_TOKEN);
        assertThat(testSpotifyProfile.getRefreshToken()).isEqualTo(UPDATED_REFRESH_TOKEN);
        assertThat(testSpotifyProfile.getAccessTokenExpiration()).isEqualTo(UPDATED_ACCESS_TOKEN_EXPIRATION);
        assertThat(testSpotifyProfile.getRefreshTokenExpiration()).isEqualTo(UPDATED_REFRESH_TOKEN_EXPIRATION);
    }

    @Test
    @Transactional
    void putNonExistingSpotifyProfile() throws Exception {
        int databaseSizeBeforeUpdate = spotifyProfileRepository.findAll().size();
        spotifyProfile.setId(count.incrementAndGet());

        // Create the SpotifyProfile
        SpotifyProfileDTO spotifyProfileDTO = spotifyProfileMapper.toDto(spotifyProfile);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSpotifyProfileMockMvc
            .perform(
                put(ENTITY_API_URL_ID, spotifyProfileDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(spotifyProfileDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SpotifyProfile in the database
        List<SpotifyProfile> spotifyProfileList = spotifyProfileRepository.findAll();
        assertThat(spotifyProfileList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSpotifyProfile() throws Exception {
        int databaseSizeBeforeUpdate = spotifyProfileRepository.findAll().size();
        spotifyProfile.setId(count.incrementAndGet());

        // Create the SpotifyProfile
        SpotifyProfileDTO spotifyProfileDTO = spotifyProfileMapper.toDto(spotifyProfile);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSpotifyProfileMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(spotifyProfileDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SpotifyProfile in the database
        List<SpotifyProfile> spotifyProfileList = spotifyProfileRepository.findAll();
        assertThat(spotifyProfileList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSpotifyProfile() throws Exception {
        int databaseSizeBeforeUpdate = spotifyProfileRepository.findAll().size();
        spotifyProfile.setId(count.incrementAndGet());

        // Create the SpotifyProfile
        SpotifyProfileDTO spotifyProfileDTO = spotifyProfileMapper.toDto(spotifyProfile);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSpotifyProfileMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(spotifyProfileDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SpotifyProfile in the database
        List<SpotifyProfile> spotifyProfileList = spotifyProfileRepository.findAll();
        assertThat(spotifyProfileList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSpotifyProfileWithPatch() throws Exception {
        // Initialize the database
        spotifyProfileRepository.saveAndFlush(spotifyProfile);

        int databaseSizeBeforeUpdate = spotifyProfileRepository.findAll().size();

        // Update the spotifyProfile using partial update
        SpotifyProfile partialUpdatedSpotifyProfile = new SpotifyProfile();
        partialUpdatedSpotifyProfile.setId(spotifyProfile.getId());

        partialUpdatedSpotifyProfile
            .accessToken(UPDATED_ACCESS_TOKEN)
            .refreshToken(UPDATED_REFRESH_TOKEN)
            .refreshTokenExpiration(UPDATED_REFRESH_TOKEN_EXPIRATION);

        restSpotifyProfileMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSpotifyProfile.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSpotifyProfile))
            )
            .andExpect(status().isOk());

        // Validate the SpotifyProfile in the database
        List<SpotifyProfile> spotifyProfileList = spotifyProfileRepository.findAll();
        assertThat(spotifyProfileList).hasSize(databaseSizeBeforeUpdate);
        SpotifyProfile testSpotifyProfile = spotifyProfileList.get(spotifyProfileList.size() - 1);
        assertThat(testSpotifyProfile.getAccessToken()).isEqualTo(UPDATED_ACCESS_TOKEN);
        assertThat(testSpotifyProfile.getRefreshToken()).isEqualTo(UPDATED_REFRESH_TOKEN);
        assertThat(testSpotifyProfile.getAccessTokenExpiration()).isEqualTo(DEFAULT_ACCESS_TOKEN_EXPIRATION);
        assertThat(testSpotifyProfile.getRefreshTokenExpiration()).isEqualTo(UPDATED_REFRESH_TOKEN_EXPIRATION);
    }

    @Test
    @Transactional
    void fullUpdateSpotifyProfileWithPatch() throws Exception {
        // Initialize the database
        spotifyProfileRepository.saveAndFlush(spotifyProfile);

        int databaseSizeBeforeUpdate = spotifyProfileRepository.findAll().size();

        // Update the spotifyProfile using partial update
        SpotifyProfile partialUpdatedSpotifyProfile = new SpotifyProfile();
        partialUpdatedSpotifyProfile.setId(spotifyProfile.getId());

        partialUpdatedSpotifyProfile
            .accessToken(UPDATED_ACCESS_TOKEN)
            .refreshToken(UPDATED_REFRESH_TOKEN)
            .accessTokenExpiration(UPDATED_ACCESS_TOKEN_EXPIRATION)
            .refreshTokenExpiration(UPDATED_REFRESH_TOKEN_EXPIRATION);

        restSpotifyProfileMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSpotifyProfile.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSpotifyProfile))
            )
            .andExpect(status().isOk());

        // Validate the SpotifyProfile in the database
        List<SpotifyProfile> spotifyProfileList = spotifyProfileRepository.findAll();
        assertThat(spotifyProfileList).hasSize(databaseSizeBeforeUpdate);
        SpotifyProfile testSpotifyProfile = spotifyProfileList.get(spotifyProfileList.size() - 1);
        assertThat(testSpotifyProfile.getAccessToken()).isEqualTo(UPDATED_ACCESS_TOKEN);
        assertThat(testSpotifyProfile.getRefreshToken()).isEqualTo(UPDATED_REFRESH_TOKEN);
        assertThat(testSpotifyProfile.getAccessTokenExpiration()).isEqualTo(UPDATED_ACCESS_TOKEN_EXPIRATION);
        assertThat(testSpotifyProfile.getRefreshTokenExpiration()).isEqualTo(UPDATED_REFRESH_TOKEN_EXPIRATION);
    }

    @Test
    @Transactional
    void patchNonExistingSpotifyProfile() throws Exception {
        int databaseSizeBeforeUpdate = spotifyProfileRepository.findAll().size();
        spotifyProfile.setId(count.incrementAndGet());

        // Create the SpotifyProfile
        SpotifyProfileDTO spotifyProfileDTO = spotifyProfileMapper.toDto(spotifyProfile);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSpotifyProfileMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, spotifyProfileDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(spotifyProfileDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SpotifyProfile in the database
        List<SpotifyProfile> spotifyProfileList = spotifyProfileRepository.findAll();
        assertThat(spotifyProfileList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSpotifyProfile() throws Exception {
        int databaseSizeBeforeUpdate = spotifyProfileRepository.findAll().size();
        spotifyProfile.setId(count.incrementAndGet());

        // Create the SpotifyProfile
        SpotifyProfileDTO spotifyProfileDTO = spotifyProfileMapper.toDto(spotifyProfile);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSpotifyProfileMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(spotifyProfileDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SpotifyProfile in the database
        List<SpotifyProfile> spotifyProfileList = spotifyProfileRepository.findAll();
        assertThat(spotifyProfileList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSpotifyProfile() throws Exception {
        int databaseSizeBeforeUpdate = spotifyProfileRepository.findAll().size();
        spotifyProfile.setId(count.incrementAndGet());

        // Create the SpotifyProfile
        SpotifyProfileDTO spotifyProfileDTO = spotifyProfileMapper.toDto(spotifyProfile);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSpotifyProfileMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(spotifyProfileDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SpotifyProfile in the database
        List<SpotifyProfile> spotifyProfileList = spotifyProfileRepository.findAll();
        assertThat(spotifyProfileList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSpotifyProfile() throws Exception {
        // Initialize the database
        spotifyProfileRepository.saveAndFlush(spotifyProfile);

        int databaseSizeBeforeDelete = spotifyProfileRepository.findAll().size();

        // Delete the spotifyProfile
        restSpotifyProfileMockMvc
            .perform(delete(ENTITY_API_URL_ID, spotifyProfile.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<SpotifyProfile> spotifyProfileList = spotifyProfileRepository.findAll();
        assertThat(spotifyProfileList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
