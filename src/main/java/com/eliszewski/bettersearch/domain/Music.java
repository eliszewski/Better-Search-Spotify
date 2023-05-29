package com.eliszewski.bettersearch.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Music.
 */
@Entity
@Table(name = "music")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Music implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "artist", nullable = false)
    private String artist;

    @NotNull
    @Column(name = "title", nullable = false)
    private String title;

    @NotNull
    @Column(name = "album", nullable = false)
    private String album;

    @NotNull
    @Column(name = "uri", nullable = false)
    private String uri;

    @NotNull
    @Column(name = "released_year", nullable = false)
    private String releasedYear;

    @NotNull
    @Column(name = "external_url", nullable = false)
    private String externalUrl;

    @Column(name = "explicit")
    private Boolean explicit;

    @ManyToOne
    @JsonIgnoreProperties(value = { "user", "searchParameter", "music" }, allowSetters = true)
    private Search search;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Music id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getArtist() {
        return this.artist;
    }

    public Music artist(String artist) {
        this.setArtist(artist);
        return this;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getTitle() {
        return this.title;
    }

    public Music title(String title) {
        this.setTitle(title);
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAlbum() {
        return this.album;
    }

    public Music album(String album) {
        this.setAlbum(album);
        return this;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getUri() {
        return this.uri;
    }

    public Music uri(String uri) {
        this.setUri(uri);
        return this;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getReleasedYear() {
        return this.releasedYear;
    }

    public Music releasedYear(String releasedYear) {
        this.setReleasedYear(releasedYear);
        return this;
    }

    public void setReleasedYear(String releasedYear) {
        this.releasedYear = releasedYear;
    }

    public String getExternalUrl() {
        return this.externalUrl;
    }

    public Music externalUrl(String externalUrl) {
        this.setExternalUrl(externalUrl);
        return this;
    }

    public void setExternalUrl(String externalUrl) {
        this.externalUrl = externalUrl;
    }

    public Boolean getExplicit() {
        return this.explicit;
    }

    public Music explicit(Boolean explicit) {
        this.setExplicit(explicit);
        return this;
    }

    public void setExplicit(Boolean explicit) {
        this.explicit = explicit;
    }

    public Search getSearch() {
        return this.search;
    }

    public void setSearch(Search search) {
        this.search = search;
    }

    public Music search(Search search) {
        this.setSearch(search);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Music)) {
            return false;
        }
        return id != null && id.equals(((Music) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Music{" +
            "id=" + getId() +
            ", artist='" + getArtist() + "'" +
            ", title='" + getTitle() + "'" +
            ", album='" + getAlbum() + "'" +
            ", uri='" + getUri() + "'" +
            ", releasedYear='" + getReleasedYear() + "'" +
            ", externalUrl='" + getExternalUrl() + "'" +
            ", explicit='" + getExplicit() + "'" +
            "}";
    }
}
