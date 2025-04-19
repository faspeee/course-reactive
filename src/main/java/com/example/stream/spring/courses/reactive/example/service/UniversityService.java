package com.example.stream.spring.courses.reactive.example.service;

import com.example.stream.spring.courses.reactive.example.converter.UniversityConverter;
import com.example.stream.spring.courses.reactive.example.entity.University;
import com.example.stream.spring.courses.reactive.example.model.request.UniversityRequestDto;
import com.example.stream.spring.courses.reactive.example.model.response.UniversityResponseDto;
import com.example.stream.spring.courses.reactive.example.repository.UniversityRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
public class UniversityService {
    private final UniversityRepository universityRepository;
    private final UniversityConverter universityConverter;


    public UniversityService(UniversityRepository universityRepository, UniversityConverter universityConverter) {
        this.universityRepository = universityRepository;
        this.universityConverter = universityConverter;
    }

    public Flux<UniversityResponseDto> findAllUniversity() {
        return universityRepository.findAll()
                .map(universityConverter::toDto);
    }

    public Mono<UniversityResponseDto> findUniversityById(String universityId) {
        return getUniversityById(UUID.fromString(universityId))
                .map(universityConverter::toDto);
    }

    public Mono<UniversityResponseDto> createUniversity(UniversityRequestDto universityRequestDto) {
        return universityRepository.save(universityConverter.toEntity(universityRequestDto))
                .map(universityConverter::toDto);
    }

    private University updateUniversity(University university, UniversityRequestDto universityRequestDto) {
        university.setAccreditation(universityRequestDto.accreditation());
        university.setContactEmail(universityRequestDto.contactEmail());
        university.setCountry(universityRequestDto.country());
        university.setCity(universityRequestDto.city());
        university.setColors(universityRequestDto.colors());
        university.setCampusArea(universityRequestDto.campusArea());
        university.setEstablished(universityRequestDto.established());
        university.setCountry(universityRequestDto.country());
        university.setMotto(universityRequestDto.motto());
        university.setPhoneNumber(universityRequestDto.phoneNumber());
        university.setMascot(universityRequestDto.mascot());
        university.setRanking(universityRequestDto.ranking());
        university.setInternational(universityRequestDto.international());
        university.setName(universityRequestDto.name());
        university.setNumFaculties(universityRequestDto.numFaculties());
        university.setNumPrograms(universityRequestDto.numPrograms());
        university.setPresident(universityRequestDto.president());
        university.setStudentCount(universityRequestDto.studentCount());
        university.setWebsite(universityRequestDto.website());
        return university;
    }

    private Mono<University> getUniversityById(UUID universityId) {
        return universityRepository.findById(universityId);
    }

    public Mono<UniversityResponseDto> updateUniversity(String universityId, UniversityRequestDto universityRequestDto) {
        return getUniversityById(UUID.fromString(universityId))
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "university not found")))
                .flatMap(university -> universityRepository.save(updateUniversity(university, universityRequestDto))
                        .map(universityConverter::toDto));
    }

    public Mono<Void> deleteUniversity(String universityId) {
        return universityRepository.deleteById(UUID.fromString(universityId));
    }
}
