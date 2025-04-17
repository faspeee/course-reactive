package com.example.stream.spring.courses.reactive.example.converter;

import com.example.stream.spring.courses.reactive.example.entity.Instructor;
import com.example.stream.spring.courses.reactive.example.model.request.InstructorRequestDto;
import com.example.stream.spring.courses.reactive.example.model.response.InstructorResponseDto;
import org.springframework.stereotype.Component;

/**
 * {@code InstructorConverter} is responsible for transforming between different representations
 * of instructor data:
 * <ul>
 *     <li>{@link InstructorRequestDto} - Incoming client data used to create or update an instructor</li>
 *     <li>{@link InstructorResponseDto} - Outgoing API data returned to the client</li>
 *     <li>{@link Instructor} - The domain entity persisted in the database</li>
 * </ul>
 * <p>
 * Implements the {@link Converter} interface to ensure consistency across DTO-to-entity
 * and entity-to-DTO transformations.
 */
@Component
public class InstructorConverter implements Converter<InstructorRequestDto, InstructorResponseDto, Instructor> {

    /**
     * Converts an {@link Instructor} domain entity to an {@link InstructorResponseDto}.
     * This is typically used to send instructor data back to clients in a structured and safe format.
     *
     * @param entity the {@link Instructor} entity to convert
     * @return the corresponding {@link InstructorResponseDto} populated with selected fields
     */
    @Override
    public InstructorResponseDto toDto(Instructor entity) {
        return new InstructorResponseDto(entity.getId().toString(), entity.getName(), entity.getEmail(),
                entity.getIdentifier(), entity.getCreatedAt(), entity.getUpdatedAt());
    }

    /**
     * Converts an {@link InstructorRequestDto} to an {@link Instructor} domain entity.
     * This is typically used when creating or updating instructors based on client input.
     *
     * @param dto the {@link InstructorRequestDto} containing client-provided data
     * @return a new {@link Instructor} entity populated with fields from the request
     */
    @Override
    public Instructor toEntity(InstructorRequestDto dto) {
        Instructor instructor = new Instructor();
        instructor.setName(dto.name());
        instructor.setEmail(dto.email());
        instructor.setIdentifier(dto.identifier());
        return instructor;
    }
}
