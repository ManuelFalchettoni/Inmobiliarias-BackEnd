package com.manuel.zaguan_inmobiliarias.service.people;

import com.manuel.zaguan_inmobiliarias.dto.request.people.PeopleRequest;
import com.manuel.zaguan_inmobiliarias.dto.response.people.PeopleResponse;
import com.manuel.zaguan_inmobiliarias.entity.people.People;
import com.manuel.zaguan_inmobiliarias.mapper.people.PeopleMapper;
import com.manuel.zaguan_inmobiliarias.repository.people.JpaPeopleRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class PeopleCreatorService {
    private final JpaPeopleRepository jpaPeopleRepository;
    private final PeopleMapper peopleMapper;

    public PeopleResponse create(PeopleRequest peopleRequest){

        People people = peopleMapper.toEntity(peopleRequest);

        return peopleMapper.toResponse(jpaPeopleRepository.save(people));
    }
}
