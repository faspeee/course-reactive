package com.example.stream.spring.courses.reactive.example.converter;

import java.util.Set;
import java.util.stream.Collectors;

public interface Converter<D, E> {
  D toDto(E entity);

  default Set<D> toDto(Set<E> entity) {
    return entity.stream()
      .map(this::toDto)
      .collect(Collectors.toSet());
  }

  default Set<E> toEntity(Set<D> entity) {
    return entity.stream()
      .map(this::toEntity)
      .collect(Collectors.toSet());
  }

  E toEntity(D dto);
}
