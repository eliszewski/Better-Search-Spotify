package com.eliszewski.bettersearch.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.eliszewski.bettersearch.IntegrationTest;
import com.eliszewski.bettersearch.domain.Music;
import com.eliszewski.bettersearch.repository.MusicRepository;
import com.eliszewski.bettersearch.service.dto.MusicDTO;
import com.eliszewski.bettersearch.service.mapper.MusicMapper;
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
 * Integration tests for the {@link MusicResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class MusicResourceIT {

    private static final String DEFAULT_ARTIST = "AAAAAAAAAA";
    private static final String UPDATED_ARTIST = "BBBBBBBBBB";

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_ALBUM = "AAAAAAAAAA";
    private static final String UPDATED_ALBUM = "BBBBBBBBBB";

    private static final String DEFAULT_URI = "AAAAAAAAAA";
    private static final String UPDATED_URI = "BBBBBBBBBB";

    private static final String DEFAULT_RELEASED_YEAR = "AAAAAAAAAA";
    private static final String UPDATED_RELEASED_YEAR = "BBBBBBBBBB";

    private static final String DEFAULT_EXTERNAL_URL = "AAAAAAAAAA";
    private static final String UPDATED_EXTERNAL_URL = "BBBBBBBBBB";

    private static final Boolean DEFAULT_EXPLICIT = false;
    private static final Boolean UPDATED_EXPLICIT = true;

    private static final String ENTITY_API_URL = "/api/music";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private MusicRepository musicRepository;

    @Autowired
    private MusicMapper musicMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMusicMockMvc;

    private Music music;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Music createEntity(EntityManager em) {
        Music music = new Music()
            .artist(DEFAULT_ARTIST)
            .title(DEFAULT_TITLE)
            .album(DEFAULT_ALBUM)
            .uri(DEFAULT_URI)
            .releasedYear(DEFAULT_RELEASED_YEAR)
            .externalUrl(DEFAULT_EXTERNAL_URL)
            .explicit(DEFAULT_EXPLICIT);
        return music;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Music createUpdatedEntity(EntityManager em) {
        Music music = new Music()
            .artist(UPDATED_ARTIST)
            .title(UPDATED_TITLE)
            .album(UPDATED_ALBUM)
            .uri(UPDATED_URI)
            .releasedYear(UPDATED_RELEASED_YEAR)
            .externalUrl(UPDATED_EXTERNAL_URL)
            .explicit(UPDATED_EXPLICIT);
        return music;
    }

    @BeforeEach
    public void initTest() {
        music = createEntity(em);
    }

    @Test
    @Transactional
    void createMusic() throws Exception {
        int databaseSizeBeforeCreate = musicRepository.findAll().size();
        // Create the Music
        MusicDTO musicDTO = musicMapper.toDto(music);
        restMusicMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(musicDTO)))
            .andExpect(status().isCreated());

        // Validate the Music in the database
        List<Music> musicList = musicRepository.findAll();
        assertThat(musicList).hasSize(databaseSizeBeforeCreate + 1);
        Music testMusic = musicList.get(musicList.size() - 1);
        assertThat(testMusic.getArtist()).isEqualTo(DEFAULT_ARTIST);
        assertThat(testMusic.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testMusic.getAlbum()).isEqualTo(DEFAULT_ALBUM);
        assertThat(testMusic.getUri()).isEqualTo(DEFAULT_URI);
        assertThat(testMusic.getReleasedYear()).isEqualTo(DEFAULT_RELEASED_YEAR);
        assertThat(testMusic.getExternalUrl()).isEqualTo(DEFAULT_EXTERNAL_URL);
        assertThat(testMusic.getExplicit()).isEqualTo(DEFAULT_EXPLICIT);
    }

    @Test
    @Transactional
    void createMusicWithExistingId() throws Exception {
        // Create the Music with an existing ID
        music.setId(1L);
        MusicDTO musicDTO = musicMapper.toDto(music);

        int databaseSizeBeforeCreate = musicRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMusicMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(musicDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Music in the database
        List<Music> musicList = musicRepository.findAll();
        assertThat(musicList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkArtistIsRequired() throws Exception {
        int databaseSizeBeforeTest = musicRepository.findAll().size();
        // set the field null
        music.setArtist(null);

        // Create the Music, which fails.
        MusicDTO musicDTO = musicMapper.toDto(music);

        restMusicMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(musicDTO)))
            .andExpect(status().isBadRequest());

        List<Music> musicList = musicRepository.findAll();
        assertThat(musicList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = musicRepository.findAll().size();
        // set the field null
        music.setTitle(null);

        // Create the Music, which fails.
        MusicDTO musicDTO = musicMapper.toDto(music);

        restMusicMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(musicDTO)))
            .andExpect(status().isBadRequest());

        List<Music> musicList = musicRepository.findAll();
        assertThat(musicList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAlbumIsRequired() throws Exception {
        int databaseSizeBeforeTest = musicRepository.findAll().size();
        // set the field null
        music.setAlbum(null);

        // Create the Music, which fails.
        MusicDTO musicDTO = musicMapper.toDto(music);

        restMusicMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(musicDTO)))
            .andExpect(status().isBadRequest());

        List<Music> musicList = musicRepository.findAll();
        assertThat(musicList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkUriIsRequired() throws Exception {
        int databaseSizeBeforeTest = musicRepository.findAll().size();
        // set the field null
        music.setUri(null);

        // Create the Music, which fails.
        MusicDTO musicDTO = musicMapper.toDto(music);

        restMusicMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(musicDTO)))
            .andExpect(status().isBadRequest());

        List<Music> musicList = musicRepository.findAll();
        assertThat(musicList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkReleasedYearIsRequired() throws Exception {
        int databaseSizeBeforeTest = musicRepository.findAll().size();
        // set the field null
        music.setReleasedYear(null);

        // Create the Music, which fails.
        MusicDTO musicDTO = musicMapper.toDto(music);

        restMusicMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(musicDTO)))
            .andExpect(status().isBadRequest());

        List<Music> musicList = musicRepository.findAll();
        assertThat(musicList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkExternalUrlIsRequired() throws Exception {
        int databaseSizeBeforeTest = musicRepository.findAll().size();
        // set the field null
        music.setExternalUrl(null);

        // Create the Music, which fails.
        MusicDTO musicDTO = musicMapper.toDto(music);

        restMusicMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(musicDTO)))
            .andExpect(status().isBadRequest());

        List<Music> musicList = musicRepository.findAll();
        assertThat(musicList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllMusic() throws Exception {
        // Initialize the database
        musicRepository.saveAndFlush(music);

        // Get all the musicList
        restMusicMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(music.getId().intValue())))
            .andExpect(jsonPath("$.[*].artist").value(hasItem(DEFAULT_ARTIST)))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].album").value(hasItem(DEFAULT_ALBUM)))
            .andExpect(jsonPath("$.[*].uri").value(hasItem(DEFAULT_URI)))
            .andExpect(jsonPath("$.[*].releasedYear").value(hasItem(DEFAULT_RELEASED_YEAR)))
            .andExpect(jsonPath("$.[*].externalUrl").value(hasItem(DEFAULT_EXTERNAL_URL)))
            .andExpect(jsonPath("$.[*].explicit").value(hasItem(DEFAULT_EXPLICIT.booleanValue())));
    }

    @Test
    @Transactional
    void getMusic() throws Exception {
        // Initialize the database
        musicRepository.saveAndFlush(music);

        // Get the music
        restMusicMockMvc
            .perform(get(ENTITY_API_URL_ID, music.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(music.getId().intValue()))
            .andExpect(jsonPath("$.artist").value(DEFAULT_ARTIST))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.album").value(DEFAULT_ALBUM))
            .andExpect(jsonPath("$.uri").value(DEFAULT_URI))
            .andExpect(jsonPath("$.releasedYear").value(DEFAULT_RELEASED_YEAR))
            .andExpect(jsonPath("$.externalUrl").value(DEFAULT_EXTERNAL_URL))
            .andExpect(jsonPath("$.explicit").value(DEFAULT_EXPLICIT.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingMusic() throws Exception {
        // Get the music
        restMusicMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingMusic() throws Exception {
        // Initialize the database
        musicRepository.saveAndFlush(music);

        int databaseSizeBeforeUpdate = musicRepository.findAll().size();

        // Update the music
        Music updatedMusic = musicRepository.findById(music.getId()).get();
        // Disconnect from session so that the updates on updatedMusic are not directly saved in db
        em.detach(updatedMusic);
        updatedMusic
            .artist(UPDATED_ARTIST)
            .title(UPDATED_TITLE)
            .album(UPDATED_ALBUM)
            .uri(UPDATED_URI)
            .releasedYear(UPDATED_RELEASED_YEAR)
            .externalUrl(UPDATED_EXTERNAL_URL)
            .explicit(UPDATED_EXPLICIT);
        MusicDTO musicDTO = musicMapper.toDto(updatedMusic);

        restMusicMockMvc
            .perform(
                put(ENTITY_API_URL_ID, musicDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(musicDTO))
            )
            .andExpect(status().isOk());

        // Validate the Music in the database
        List<Music> musicList = musicRepository.findAll();
        assertThat(musicList).hasSize(databaseSizeBeforeUpdate);
        Music testMusic = musicList.get(musicList.size() - 1);
        assertThat(testMusic.getArtist()).isEqualTo(UPDATED_ARTIST);
        assertThat(testMusic.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testMusic.getAlbum()).isEqualTo(UPDATED_ALBUM);
        assertThat(testMusic.getUri()).isEqualTo(UPDATED_URI);
        assertThat(testMusic.getReleasedYear()).isEqualTo(UPDATED_RELEASED_YEAR);
        assertThat(testMusic.getExternalUrl()).isEqualTo(UPDATED_EXTERNAL_URL);
        assertThat(testMusic.getExplicit()).isEqualTo(UPDATED_EXPLICIT);
    }

    @Test
    @Transactional
    void putNonExistingMusic() throws Exception {
        int databaseSizeBeforeUpdate = musicRepository.findAll().size();
        music.setId(count.incrementAndGet());

        // Create the Music
        MusicDTO musicDTO = musicMapper.toDto(music);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMusicMockMvc
            .perform(
                put(ENTITY_API_URL_ID, musicDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(musicDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Music in the database
        List<Music> musicList = musicRepository.findAll();
        assertThat(musicList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchMusic() throws Exception {
        int databaseSizeBeforeUpdate = musicRepository.findAll().size();
        music.setId(count.incrementAndGet());

        // Create the Music
        MusicDTO musicDTO = musicMapper.toDto(music);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMusicMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(musicDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Music in the database
        List<Music> musicList = musicRepository.findAll();
        assertThat(musicList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMusic() throws Exception {
        int databaseSizeBeforeUpdate = musicRepository.findAll().size();
        music.setId(count.incrementAndGet());

        // Create the Music
        MusicDTO musicDTO = musicMapper.toDto(music);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMusicMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(musicDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Music in the database
        List<Music> musicList = musicRepository.findAll();
        assertThat(musicList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateMusicWithPatch() throws Exception {
        // Initialize the database
        musicRepository.saveAndFlush(music);

        int databaseSizeBeforeUpdate = musicRepository.findAll().size();

        // Update the music using partial update
        Music partialUpdatedMusic = new Music();
        partialUpdatedMusic.setId(music.getId());

        partialUpdatedMusic.artist(UPDATED_ARTIST).title(UPDATED_TITLE).uri(UPDATED_URI);

        restMusicMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMusic.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMusic))
            )
            .andExpect(status().isOk());

        // Validate the Music in the database
        List<Music> musicList = musicRepository.findAll();
        assertThat(musicList).hasSize(databaseSizeBeforeUpdate);
        Music testMusic = musicList.get(musicList.size() - 1);
        assertThat(testMusic.getArtist()).isEqualTo(UPDATED_ARTIST);
        assertThat(testMusic.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testMusic.getAlbum()).isEqualTo(DEFAULT_ALBUM);
        assertThat(testMusic.getUri()).isEqualTo(UPDATED_URI);
        assertThat(testMusic.getReleasedYear()).isEqualTo(DEFAULT_RELEASED_YEAR);
        assertThat(testMusic.getExternalUrl()).isEqualTo(DEFAULT_EXTERNAL_URL);
        assertThat(testMusic.getExplicit()).isEqualTo(DEFAULT_EXPLICIT);
    }

    @Test
    @Transactional
    void fullUpdateMusicWithPatch() throws Exception {
        // Initialize the database
        musicRepository.saveAndFlush(music);

        int databaseSizeBeforeUpdate = musicRepository.findAll().size();

        // Update the music using partial update
        Music partialUpdatedMusic = new Music();
        partialUpdatedMusic.setId(music.getId());

        partialUpdatedMusic
            .artist(UPDATED_ARTIST)
            .title(UPDATED_TITLE)
            .album(UPDATED_ALBUM)
            .uri(UPDATED_URI)
            .releasedYear(UPDATED_RELEASED_YEAR)
            .externalUrl(UPDATED_EXTERNAL_URL)
            .explicit(UPDATED_EXPLICIT);

        restMusicMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMusic.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMusic))
            )
            .andExpect(status().isOk());

        // Validate the Music in the database
        List<Music> musicList = musicRepository.findAll();
        assertThat(musicList).hasSize(databaseSizeBeforeUpdate);
        Music testMusic = musicList.get(musicList.size() - 1);
        assertThat(testMusic.getArtist()).isEqualTo(UPDATED_ARTIST);
        assertThat(testMusic.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testMusic.getAlbum()).isEqualTo(UPDATED_ALBUM);
        assertThat(testMusic.getUri()).isEqualTo(UPDATED_URI);
        assertThat(testMusic.getReleasedYear()).isEqualTo(UPDATED_RELEASED_YEAR);
        assertThat(testMusic.getExternalUrl()).isEqualTo(UPDATED_EXTERNAL_URL);
        assertThat(testMusic.getExplicit()).isEqualTo(UPDATED_EXPLICIT);
    }

    @Test
    @Transactional
    void patchNonExistingMusic() throws Exception {
        int databaseSizeBeforeUpdate = musicRepository.findAll().size();
        music.setId(count.incrementAndGet());

        // Create the Music
        MusicDTO musicDTO = musicMapper.toDto(music);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMusicMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, musicDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(musicDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Music in the database
        List<Music> musicList = musicRepository.findAll();
        assertThat(musicList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMusic() throws Exception {
        int databaseSizeBeforeUpdate = musicRepository.findAll().size();
        music.setId(count.incrementAndGet());

        // Create the Music
        MusicDTO musicDTO = musicMapper.toDto(music);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMusicMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(musicDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Music in the database
        List<Music> musicList = musicRepository.findAll();
        assertThat(musicList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMusic() throws Exception {
        int databaseSizeBeforeUpdate = musicRepository.findAll().size();
        music.setId(count.incrementAndGet());

        // Create the Music
        MusicDTO musicDTO = musicMapper.toDto(music);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMusicMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(musicDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Music in the database
        List<Music> musicList = musicRepository.findAll();
        assertThat(musicList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteMusic() throws Exception {
        // Initialize the database
        musicRepository.saveAndFlush(music);

        int databaseSizeBeforeDelete = musicRepository.findAll().size();

        // Delete the music
        restMusicMockMvc
            .perform(delete(ENTITY_API_URL_ID, music.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Music> musicList = musicRepository.findAll();
        assertThat(musicList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
