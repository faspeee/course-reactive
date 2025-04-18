package com.example.stream.spring.courses.reactive.example.service;

import com.example.stream.spring.courses.reactive.example.converter.UniversityConverter;
import com.example.stream.spring.courses.reactive.example.entity.University;
import com.example.stream.spring.courses.reactive.example.functional.Either;
import com.example.stream.spring.courses.reactive.example.model.error.Error;
import com.example.stream.spring.courses.reactive.example.model.error.*;
import com.example.stream.spring.courses.reactive.example.model.request.UniversityRequestDto;
import com.example.stream.spring.courses.reactive.example.model.response.UniversityResponseDto;
import com.example.stream.spring.courses.reactive.example.repository.UniversityRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

import static com.example.stream.spring.courses.reactive.example.utility.UtilMono.createMonoWithError;

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

    public Mono<Either<Error, UniversityResponseDto>> findUniversityById(String universityId) {
        return getUniversityById(UUID.fromString(universityId))
                .map(errorUniversityEither -> errorUniversityEither.map(universityConverter::toDto));
    }

    public Mono<Either<Error, UniversityResponseDto>> createUniversity(UniversityRequestDto universityRequestDto) {
        return universityRepository.save(universityConverter.toEntity(universityRequestDto))
                .<Either<Error, UniversityResponseDto>>map(university -> Either.right(universityConverter.toDto(university)))
                .switchIfEmpty(Mono.just(Either.left(new GenericError())));
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

    private Mono<Either<Error, University>> getUniversityById(UUID universityId) {
        return universityRepository.findById(universityId)
                .<Either<Error, University>>map(Either::right)
                .switchIfEmpty(Mono.just(Either.left(new UniversityNotFound())));
    }

    public Mono<Either<Error, UniversityResponseDto>> updateUniversity(String universityId, UniversityRequestDto universityRequestDto) {
        return getUniversityById(UUID.fromString(universityId))
                .flatMap(either -> either.getRight()
                        .map(university -> universityRepository.save(updateUniversity(university, universityRequestDto))
                                .<Either<Error, UniversityResponseDto>>map(university1 -> Either.right(universityConverter.toDto(university1))))
                        .orElse(createMonoWithError(either)));
    }

    public Mono<Either<Error, Boolean>> existUniversityById(String universityId) {
        return universityRepository.existsById(UUID.fromString(universityId))
                .filter(Boolean::booleanValue)
                .<Either<Error, Boolean>>map(Either::right)
                .switchIfEmpty(Mono.just(Either.left(new UniversityNotFound())));
    }

    public Mono<Either<Error, Success>> deleteUniversity(String universityId) {
        return existUniversityById(universityId)
                .flatMap(either -> either.getRight()
                        .<Mono<Either<Error, Success>>>map(building ->
                                universityRepository.deleteById(UUID.fromString(universityId))
                                        .then(Mono.just(Either.right(new UniversityDeleteOk()))))
                        .orElse(createMonoWithError(either)));
    }
}
