package com.eliszewski.bettersearch.service.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.eliszewski.bettersearch.domain.SpotifyProfile} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SpotifyProfileDTO implements Serializable {

    private Long id;

    private String accessToken;

    private String refreshToken;

    private Instant accessTokenExpiration;

    private Instant refreshTokenExpiration;

    private UserDTO user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public Instant getAccessTokenExpiration() {
        return accessTokenExpiration;
    }

    public void setAccessTokenExpiration(Instant accessTokenExpiration) {
        this.accessTokenExpiration = accessTokenExpiration;
    }

    public Instant getRefreshTokenExpiration() {
        return refreshTokenExpiration;
    }

    public void setRefreshTokenExpiration(Instant refreshTokenExpiration) {
        this.refreshTokenExpiration = refreshTokenExpiration;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SpotifyProfileDTO)) {
            return false;
        }

        SpotifyProfileDTO spotifyProfileDTO = (SpotifyProfileDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, spotifyProfileDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SpotifyProfileDTO{" +
            "id=" + getId() +
            ", accessToken='" + getAccessToken() + "'" +
            ", refreshToken='" + getRefreshToken() + "'" +
            ", accessTokenExpiration='" + getAccessTokenExpiration() + "'" +
            ", refreshTokenExpiration='" + getRefreshTokenExpiration() + "'" +
            ", user=" + getUser() +
            "}";
    }
}
