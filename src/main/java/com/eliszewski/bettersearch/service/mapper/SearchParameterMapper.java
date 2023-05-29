package com.eliszewski.bettersearch.service.mapper;

import com.eliszewski.bettersearch.domain.SearchParameter;
import com.eliszewski.bettersearch.service.dto.SearchParameterDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link SearchParameter} and its DTO {@link SearchParameterDTO}.
 */
@Mapper(componentModel = "spring")
public interface SearchParameterMapper extends EntityMapper<SearchParameterDTO, SearchParameter> {}
