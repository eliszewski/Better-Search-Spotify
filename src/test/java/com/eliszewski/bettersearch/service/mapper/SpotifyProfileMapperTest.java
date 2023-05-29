package com.eliszewski.bettersearch.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SpotifyProfileMapperTest {

    private SpotifyProfileMapper spotifyProfileMapper;

    @BeforeEach
    public void setUp() {
        spotifyProfileMapper = new SpotifyProfileMapperImpl();
    }
}
