package com.manuel.zaguan_inmobiliarias.service.people;

import com.manuel.zaguan_inmobiliarias.dto.response.people.PeopleResponse;
import com.manuel.zaguan_inmobiliarias.entity.people.People;
import com.manuel.zaguan_inmobiliarias.exception.people.PeopleNotFoundException;
import com.manuel.zaguan_inmobiliarias.mapper.people.PeopleMapper;
import com.manuel.zaguan_inmobiliarias.repository.people.JpaPeopleRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class PeopleFinderService {
    private final JpaPeopleRepository jpaPeopleRepository;
    private final PeopleMapper peopleMapper;

    public PeopleResponse findById(Long id){
        People people = jpaPeopleRepository.findById(id)
                .orElseThrow( () -> new PeopleNotFoundException(id));
        return peopleMapper.toResponse(people);
    }

    public Page<PeopleResponse> findAll(Pageable pageable){
        Page<People> peoples = jpaPeopleRepository.findAll(pageable);

        return peoples.map(
                peopleMapper :: toResponse
        );
    }
}
