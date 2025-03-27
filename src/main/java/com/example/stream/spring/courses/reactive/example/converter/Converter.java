package com.example.stream.spring.courses.reactive.example.converter;

import java.util.Set;
import java.util.stream.Collectors;

public interface Converter<DI, DO, E> {
    DO toDto(E entity);

    default Set<DO> toDto(Set<E> entity) {
        return entity.stream()
                .map(this::toDto)
                .collect(Collectors.toSet());
    }

    default Set<E> toEntity(Set<DI> entity) {
        return entity.stream()
                .map(this::toEntity)
                .collect(Collectors.toSet());
    }

    E toEntity(DI dto);
}
