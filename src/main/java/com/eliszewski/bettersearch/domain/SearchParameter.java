package com.eliszewski.bettersearch.domain;

import com.eliszewski.bettersearch.domain.enumeration.AlbumType;
import com.eliszewski.bettersearch.domain.enumeration.SearchParameterAttribute;
import com.eliszewski.bettersearch.domain.enumeration.SearchParameterType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A SearchParameter.
 */
@Entity
@Table(name = "search_parameter")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SearchParameter implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private SearchParameterType type;

    @Enumerated(EnumType.STRING)
    @Column(name = "attribute_name")
    private SearchParameterAttribute attributeName;

    @Enumerated(EnumType.STRING)
    @Column(name = "album_type")
    private AlbumType albumType;

    @Column(name = "search_value")
    private String searchValue;

    @OneToMany(mappedBy = "searchParameter")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "user", "searchParameter", "music" }, allowSetters = true)
    private Set<Search> searches = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public SearchParameter id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public SearchParameterType getType() {
        return this.type;
    }

    public SearchParameter type(SearchParameterType type) {
        this.setType(type);
        return this;
    }

    public void setType(SearchParameterType type) {
        this.type = type;
    }

    public SearchParameterAttribute getAttributeName() {
        return this.attributeName;
    }

    public SearchParameter attributeName(SearchParameterAttribute attributeName) {
        this.setAttributeName(attributeName);
        return this;
    }

    public void setAttributeName(SearchParameterAttribute attributeName) {
        this.attributeName = attributeName;
    }

    public AlbumType getAlbumType() {
        return this.albumType;
    }

    public SearchParameter albumType(AlbumType albumType) {
        this.setAlbumType(albumType);
        return this;
    }

    public void setAlbumType(AlbumType albumType) {
        this.albumType = albumType;
    }

    public String getSearchValue() {
        return this.searchValue;
    }

    public SearchParameter searchValue(String searchValue) {
        this.setSearchValue(searchValue);
        return this;
    }

    public void setSearchValue(String searchValue) {
        this.searchValue = searchValue;
    }

    public Set<Search> getSearches() {
        return this.searches;
    }

    public void setSearches(Set<Search> searches) {
        if (this.searches != null) {
            this.searches.forEach(i -> i.setSearchParameter(null));
        }
        if (searches != null) {
            searches.forEach(i -> i.setSearchParameter(this));
        }
        this.searches = searches;
    }

    public SearchParameter searches(Set<Search> searches) {
        this.setSearches(searches);
        return this;
    }

    public SearchParameter addSearch(Search search) {
        this.searches.add(search);
        search.setSearchParameter(this);
        return this;
    }

    public SearchParameter removeSearch(Search search) {
        this.searches.remove(search);
        search.setSearchParameter(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SearchParameter)) {
            return false;
        }
        return id != null && id.equals(((SearchParameter) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SearchParameter{" +
            "id=" + getId() +
            ", type='" + getType() + "'" +
            ", attributeName='" + getAttributeName() + "'" +
            ", albumType='" + getAlbumType() + "'" +
            ", searchValue='" + getSearchValue() + "'" +
            "}";
    }
}
