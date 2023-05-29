package com.eliszewski.bettersearch.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Search.
 */
@Entity
@Table(name = "search")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Search implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Column(name = "date", nullable = false)
    private Instant date;

    @NotNull
    @Column(name = "create_playist", nullable = false)
    private Boolean createPlayist;

    @ManyToOne
    private User user;

    @ManyToOne
    @JsonIgnoreProperties(value = { "searches" }, allowSetters = true)
    private SearchParameter searchParameter;

    @OneToMany(mappedBy = "search")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "search" }, allowSetters = true)
    private Set<Music> music = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Search id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Search name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Instant getDate() {
        return this.date;
    }

    public Search date(Instant date) {
        this.setDate(date);
        return this;
    }

    public void setDate(Instant date) {
        this.date = date;
    }

    public Boolean getCreatePlayist() {
        return this.createPlayist;
    }

    public Search createPlayist(Boolean createPlayist) {
        this.setCreatePlayist(createPlayist);
        return this;
    }

    public void setCreatePlayist(Boolean createPlayist) {
        this.createPlayist = createPlayist;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Search user(User user) {
        this.setUser(user);
        return this;
    }

    public SearchParameter getSearchParameter() {
        return this.searchParameter;
    }

    public void setSearchParameter(SearchParameter searchParameter) {
        this.searchParameter = searchParameter;
    }

    public Search searchParameter(SearchParameter searchParameter) {
        this.setSearchParameter(searchParameter);
        return this;
    }

    public Set<Music> getMusic() {
        return this.music;
    }

    public void setMusic(Set<Music> music) {
        if (this.music != null) {
            this.music.forEach(i -> i.setSearch(null));
        }
        if (music != null) {
            music.forEach(i -> i.setSearch(this));
        }
        this.music = music;
    }

    public Search music(Set<Music> music) {
        this.setMusic(music);
        return this;
    }

    public Search addMusic(Music music) {
        this.music.add(music);
        music.setSearch(this);
        return this;
    }

    public Search removeMusic(Music music) {
        this.music.remove(music);
        music.setSearch(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Search)) {
            return false;
        }
        return id != null && id.equals(((Search) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Search{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", date='" + getDate() + "'" +
            ", createPlayist='" + getCreatePlayist() + "'" +
            "}";
    }
}
