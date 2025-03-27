package com.example.stream.spring.courses.reactive.example.repository;

import com.example.stream.spring.courses.reactive.example.entity.Building;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface BuildingRepository extends ReactiveCrudRepository<Building, Long> {

}
