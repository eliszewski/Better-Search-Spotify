package com.eliszewski.bettersearch.service.mapper;

import com.eliszewski.bettersearch.domain.Search;
import com.eliszewski.bettersearch.domain.SearchParameter;
import com.eliszewski.bettersearch.domain.User;
import com.eliszewski.bettersearch.service.dto.SearchDTO;
import com.eliszewski.bettersearch.service.dto.SearchParameterDTO;
import com.eliszewski.bettersearch.service.dto.UserDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Search} and its DTO {@link SearchDTO}.
 */
@Mapper(componentModel = "spring")
public interface SearchMapper extends EntityMapper<SearchDTO, Search> {
    @Mapping(target = "user", source = "user", qualifiedByName = "userLogin")
    @Mapping(target = "searchParameter", source = "searchParameter", qualifiedByName = "searchParameterId")
    SearchDTO toDto(Search s);

    @Named("userLogin")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "login", source = "login")
    UserDTO toDtoUserLogin(User user);

    @Named("searchParameterId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    SearchParameterDTO toDtoSearchParameterId(SearchParameter searchParameter);
}
