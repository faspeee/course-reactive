package com.example.stream.spring.courses.reactive.example.converter;

import com.example.stream.spring.courses.reactive.example.entity.University;
import com.example.stream.spring.courses.reactive.example.model.request.UniversityRequestDto;
import com.example.stream.spring.courses.reactive.example.model.response.UniversityResponseDto;
import org.springframework.stereotype.Component;

@Component
public class UniversityConverter implements Converter<UniversityRequestDto, UniversityResponseDto, University> {
    @Override
    public UniversityResponseDto toDto(University entity) {
        return new UniversityResponseDto(entity.getId().toString(), entity.getName(), entity.getLocation(), entity.getEstablished(),
                entity.getAccreditation(), entity.getPresident(), entity.getStudentCount(), entity.getWebsite(), entity.getContactEmail(),
                entity.getPhoneNumber(), entity.getMotto(), entity.getColors(), entity.getMascot(), entity.getCampusArea(),
                entity.getNumFaculties(), entity.getNumPrograms(), entity.getInternational(), entity.getRanking(), entity.getCreatedAt(),
                entity.getUpdatedAt(), entity.getCountry(), entity.getCity(), entity.getIdentifier());
    }

    @Override
    public University toEntity(UniversityRequestDto dto) {
        University entity = new University();
        entity.setAccreditation(dto.accreditation());
        entity.setContactEmail(dto.contactEmail());
        entity.setCountry(dto.country());
        entity.setCity(dto.city());
        entity.setColors(dto.colors());
        entity.setCampusArea(dto.campusArea());
        entity.setEstablished(dto.established());
        entity.setCountry(dto.country());
        entity.setMotto(dto.motto());
        entity.setPhoneNumber(dto.phoneNumber());
        entity.setMascot(dto.mascot());
        entity.setRanking(dto.ranking());
        entity.setInternational(dto.international());
        entity.setName(dto.name());
        entity.setNumFaculties(dto.numFaculties());
        entity.setNumPrograms(dto.numPrograms());
        entity.setPresident(dto.president());
        entity.setStudentCount(dto.studentCount());
        entity.setWebsite(dto.website());
        entity.setLocation(dto.location());
        return entity;
    }
}
