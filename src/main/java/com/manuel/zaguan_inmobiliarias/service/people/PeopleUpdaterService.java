package com.manuel.zaguan_inmobiliarias.service.people;

import com.manuel.zaguan_inmobiliarias.dto.request.people.PeopleRequest;
import com.manuel.zaguan_inmobiliarias.dto.response.people.PeopleResponse;
import com.manuel.zaguan_inmobiliarias.entity.people.People;
import com.manuel.zaguan_inmobiliarias.exception.people.PeopleNotFoundException;
import com.manuel.zaguan_inmobiliarias.mapper.people.PeopleMapper;
import com.manuel.zaguan_inmobiliarias.repository.people.JpaPeopleRepository;
import com.manuel.zaguan_inmobiliarias.service.auth.AgencyScopeService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class PeopleUpdaterService {
    private final JpaPeopleRepository jpaPeopleRepository;
    private final PeopleMapper peopleMapper;
    private final AgencyScopeService agencyScopeService;

    public PeopleResponse updater(Long id, PeopleRequest peopleRequest){
        People people = jpaPeopleRepository.findById(id)
                .orElseThrow( () -> new PeopleNotFoundException(id));

        //Una persona de otra inmobiliaria responde 404, igual que una que no existe
        if (!agencyScopeService.canAccess(people.getAgencyId())) {
            throw new PeopleNotFoundException(id);
        }

        people.setName(peopleRequest.getName());
        people.setEmail(peopleRequest.getEmail());
        people.setCuit(peopleRequest.getCuit());
        people.setAddress(peopleRequest.getAddress());
        people.setPhone(peopleRequest.getPhone());
        people.setDni(peopleRequest.getDni());

        return peopleMapper.toResponse(jpaPeopleRepository.save(people));
    }
}
