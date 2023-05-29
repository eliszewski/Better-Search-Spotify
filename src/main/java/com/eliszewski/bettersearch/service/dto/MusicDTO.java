package com.eliszewski.bettersearch.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.eliszewski.bettersearch.domain.Music} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MusicDTO implements Serializable {

    private Long id;

    @NotNull
    private String artist;

    @NotNull
    private String title;

    @NotNull
    private String album;

    @NotNull
    private String uri;

    @NotNull
    private String releasedYear;

    @NotNull
    private String externalUrl;

    private Boolean explicit;

    private SearchDTO search;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getReleasedYear() {
        return releasedYear;
    }

    public void setReleasedYear(String releasedYear) {
        this.releasedYear = releasedYear;
    }

    public String getExternalUrl() {
        return externalUrl;
    }

    public void setExternalUrl(String externalUrl) {
        this.externalUrl = externalUrl;
    }

    public Boolean getExplicit() {
        return explicit;
    }

    public void setExplicit(Boolean explicit) {
        this.explicit = explicit;
    }

    public SearchDTO getSearch() {
        return search;
    }

    public void setSearch(SearchDTO search) {
        this.search = search;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MusicDTO)) {
            return false;
        }

        MusicDTO musicDTO = (MusicDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, musicDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MusicDTO{" +
            "id=" + getId() +
            ", artist='" + getArtist() + "'" +
            ", title='" + getTitle() + "'" +
            ", album='" + getAlbum() + "'" +
            ", uri='" + getUri() + "'" +
            ", releasedYear='" + getReleasedYear() + "'" +
            ", externalUrl='" + getExternalUrl() + "'" +
            ", explicit='" + getExplicit() + "'" +
            ", search=" + getSearch() +
            "}";
    }
}
