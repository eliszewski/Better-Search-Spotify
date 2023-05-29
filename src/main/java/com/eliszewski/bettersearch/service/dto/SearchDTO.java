package com.eliszewski.bettersearch.service.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.eliszewski.bettersearch.domain.Search} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SearchDTO implements Serializable {

    private Long id;

    @NotNull
    private String name;

    @NotNull
    private Instant date;

    @NotNull
    private Boolean createPlayist;

    private UserDTO user;

    private SearchParameterDTO searchParameter;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Instant getDate() {
        return date;
    }

    public void setDate(Instant date) {
        this.date = date;
    }

    public Boolean getCreatePlayist() {
        return createPlayist;
    }

    public void setCreatePlayist(Boolean createPlayist) {
        this.createPlayist = createPlayist;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    public SearchParameterDTO getSearchParameter() {
        return searchParameter;
    }

    public void setSearchParameter(SearchParameterDTO searchParameter) {
        this.searchParameter = searchParameter;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SearchDTO)) {
            return false;
        }

        SearchDTO searchDTO = (SearchDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, searchDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SearchDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", date='" + getDate() + "'" +
            ", createPlayist='" + getCreatePlayist() + "'" +
            ", user=" + getUser() +
            ", searchParameter=" + getSearchParameter() +
            "}";
    }
}
