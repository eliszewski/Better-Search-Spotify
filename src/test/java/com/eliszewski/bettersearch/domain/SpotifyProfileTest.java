package com.eliszewski.bettersearch.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.eliszewski.bettersearch.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SpotifyProfileTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SpotifyProfile.class);
        SpotifyProfile spotifyProfile1 = new SpotifyProfile();
        spotifyProfile1.setId(1L);
        SpotifyProfile spotifyProfile2 = new SpotifyProfile();
        spotifyProfile2.setId(spotifyProfile1.getId());
        assertThat(spotifyProfile1).isEqualTo(spotifyProfile2);
        spotifyProfile2.setId(2L);
        assertThat(spotifyProfile1).isNotEqualTo(spotifyProfile2);
        spotifyProfile1.setId(null);
        assertThat(spotifyProfile1).isNotEqualTo(spotifyProfile2);
    }
}
