package com.example.stream.spring.courses.reactive.example.converter;

import com.example.stream.spring.courses.reactive.example.entity.Campus;
import com.example.stream.spring.courses.reactive.example.model.request.CampusRequestDto;
import org.springframework.stereotype.Component;

@Component
public class CampusConverter implements Converter<CampusRequestDto, Campus> {
}
