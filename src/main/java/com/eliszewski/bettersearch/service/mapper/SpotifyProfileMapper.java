package com.eliszewski.bettersearch.service.mapper;

import com.eliszewski.bettersearch.domain.SpotifyProfile;
import com.eliszewski.bettersearch.domain.User;
import com.eliszewski.bettersearch.service.dto.SpotifyProfileDTO;
import com.eliszewski.bettersearch.service.dto.UserDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link SpotifyProfile} and its DTO {@link SpotifyProfileDTO}.
 */
@Mapper(componentModel = "spring")
public interface SpotifyProfileMapper extends EntityMapper<SpotifyProfileDTO, SpotifyProfile> {
    @Mapping(target = "user", source = "user", qualifiedByName = "userLogin")
    SpotifyProfileDTO toDto(SpotifyProfile s);

    @Named("userLogin")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "login", source = "login")
    UserDTO toDtoUserLogin(User user);
}
