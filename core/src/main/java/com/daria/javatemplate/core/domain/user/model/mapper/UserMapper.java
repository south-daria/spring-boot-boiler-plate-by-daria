package com.daria.javatemplate.core.domain.user.model.mapper;

import com.daria.javatemplate.core.common.mapper.GenericMapper;
import com.daria.javatemplate.core.domain.user.model.dto.UserDTO;
import com.daria.javatemplate.core.domain.user.model.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public abstract class UserMapper extends GenericMapper<UserDTO, UserEntity> {

    @Override
    public abstract UserEntity toEntity(final UserDTO dto);
}
