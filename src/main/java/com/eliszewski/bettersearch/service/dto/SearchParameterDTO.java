package com.eliszewski.bettersearch.service.dto;

import com.eliszewski.bettersearch.domain.enumeration.AlbumType;
import com.eliszewski.bettersearch.domain.enumeration.SearchParameterAttribute;
import com.eliszewski.bettersearch.domain.enumeration.SearchParameterType;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.eliszewski.bettersearch.domain.SearchParameter} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SearchParameterDTO implements Serializable {

    private Long id;

    private SearchParameterType type;

    private SearchParameterAttribute attributeName;

    private AlbumType albumType;

    private String searchValue;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public SearchParameterType getType() {
        return type;
    }

    public void setType(SearchParameterType type) {
        this.type = type;
    }

    public SearchParameterAttribute getAttributeName() {
        return attributeName;
    }

    public void setAttributeName(SearchParameterAttribute attributeName) {
        this.attributeName = attributeName;
    }

    public AlbumType getAlbumType() {
        return albumType;
    }

    public void setAlbumType(AlbumType albumType) {
        this.albumType = albumType;
    }

    public String getSearchValue() {
        return searchValue;
    }

    public void setSearchValue(String searchValue) {
        this.searchValue = searchValue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SearchParameterDTO)) {
            return false;
        }

        SearchParameterDTO searchParameterDTO = (SearchParameterDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, searchParameterDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SearchParameterDTO{" +
            "id=" + getId() +
            ", type='" + getType() + "'" +
            ", attributeName='" + getAttributeName() + "'" +
            ", albumType='" + getAlbumType() + "'" +
            ", searchValue='" + getSearchValue() + "'" +
            "}";
    }
}
