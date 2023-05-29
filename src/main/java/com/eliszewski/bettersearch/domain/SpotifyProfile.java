package com.eliszewski.bettersearch.domain;

import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A SpotifyProfile.
 */
@Entity
@Table(name = "spotify_profile")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SpotifyProfile implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "access_token")
    private String accessToken;

    @Column(name = "refresh_token")
    private String refreshToken;

    @Column(name = "access_token_expiration")
    private Instant accessTokenExpiration;

    @Column(name = "refresh_token_expiration")
    private Instant refreshTokenExpiration;

    @OneToOne
    @JoinColumn(unique = true)
    private User user;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public SpotifyProfile id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAccessToken() {
        return this.accessToken;
    }

    public SpotifyProfile accessToken(String accessToken) {
        this.setAccessToken(accessToken);
        return this;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getRefreshToken() {
        return this.refreshToken;
    }

    public SpotifyProfile refreshToken(String refreshToken) {
        this.setRefreshToken(refreshToken);
        return this;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public Instant getAccessTokenExpiration() {
        return this.accessTokenExpiration;
    }

    public SpotifyProfile accessTokenExpiration(Instant accessTokenExpiration) {
        this.setAccessTokenExpiration(accessTokenExpiration);
        return this;
    }

    public void setAccessTokenExpiration(Instant accessTokenExpiration) {
        this.accessTokenExpiration = accessTokenExpiration;
    }

    public Instant getRefreshTokenExpiration() {
        return this.refreshTokenExpiration;
    }

    public SpotifyProfile refreshTokenExpiration(Instant refreshTokenExpiration) {
        this.setRefreshTokenExpiration(refreshTokenExpiration);
        return this;
    }

    public void setRefreshTokenExpiration(Instant refreshTokenExpiration) {
        this.refreshTokenExpiration = refreshTokenExpiration;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public SpotifyProfile user(User user) {
        this.setUser(user);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SpotifyProfile)) {
            return false;
        }
        return id != null && id.equals(((SpotifyProfile) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SpotifyProfile{" +
            "id=" + getId() +
            ", accessToken='" + getAccessToken() + "'" +
            ", refreshToken='" + getRefreshToken() + "'" +
            ", accessTokenExpiration='" + getAccessTokenExpiration() + "'" +
            ", refreshTokenExpiration='" + getRefreshTokenExpiration() + "'" +
            "}";
    }
}
