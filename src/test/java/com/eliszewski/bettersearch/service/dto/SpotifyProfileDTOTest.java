package com.eliszewski.bettersearch.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.eliszewski.bettersearch.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SpotifyProfileDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SpotifyProfileDTO.class);
        SpotifyProfileDTO spotifyProfileDTO1 = new SpotifyProfileDTO();
        spotifyProfileDTO1.setId(1L);
        SpotifyProfileDTO spotifyProfileDTO2 = new SpotifyProfileDTO();
        assertThat(spotifyProfileDTO1).isNotEqualTo(spotifyProfileDTO2);
        spotifyProfileDTO2.setId(spotifyProfileDTO1.getId());
        assertThat(spotifyProfileDTO1).isEqualTo(spotifyProfileDTO2);
        spotifyProfileDTO2.setId(2L);
        assertThat(spotifyProfileDTO1).isNotEqualTo(spotifyProfileDTO2);
        spotifyProfileDTO1.setId(null);
        assertThat(spotifyProfileDTO1).isNotEqualTo(spotifyProfileDTO2);
    }
}
