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
public class PeopleUpdaterService {
    private final JpaPeopleRepository jpaPeopleRepository;
    private final PeopleMapper peopleMapper;

    public PeopleResponse updater(Long id, PeopleRequest peopleRequest){
        People people = jpaPeopleRepository.findById(id)
                .orElseThrow( () -> new PeopleNotFoundException());

        people.setName(peopleRequest.getName());
        people.setEmail(peopleRequest.getEmail());
        people.setCuit(peopleRequest.getCuit());
        people.setAddress(peopleRequest.getAddress());
        people.setPhone(peopleRequest.getPhone());
        people.setDni(peopleRequest.getDni());

        return peopleMapper.toResponse(jpaPeopleRepository.save(people));
    }
}
