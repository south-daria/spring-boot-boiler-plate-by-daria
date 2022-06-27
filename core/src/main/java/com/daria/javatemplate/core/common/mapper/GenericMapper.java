package com.daria.javatemplate.core.common.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

public abstract class GenericMapper<D,E> {
    public abstract D toDto(E e);
    public abstract E toEntity(D d);

    public abstract List<D> toDtoList(List<E> entityList);
    public abstract List<E> toEntityList(List<D> dtoList);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    public abstract void updateFromDto(D dto, @MappingTarget E entity);
}
