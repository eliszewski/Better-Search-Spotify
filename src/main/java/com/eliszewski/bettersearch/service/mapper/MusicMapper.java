package com.eliszewski.bettersearch.service.mapper;

import com.eliszewski.bettersearch.domain.Music;
import com.eliszewski.bettersearch.domain.Search;
import com.eliszewski.bettersearch.service.dto.MusicDTO;
import com.eliszewski.bettersearch.service.dto.SearchDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Music} and its DTO {@link MusicDTO}.
 */
@Mapper(componentModel = "spring")
public interface MusicMapper extends EntityMapper<MusicDTO, Music> {
    @Mapping(target = "search", source = "search", qualifiedByName = "searchId")
    MusicDTO toDto(Music s);

    @Named("searchId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    SearchDTO toDtoSearchId(Search search);
}
