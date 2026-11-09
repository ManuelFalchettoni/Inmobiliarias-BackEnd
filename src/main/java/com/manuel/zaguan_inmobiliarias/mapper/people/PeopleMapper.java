package com.manuel.zaguan_inmobiliarias.mapper.people;

import com.manuel.zaguan_inmobiliarias.dto.request.people.PeopleRequest;
import com.manuel.zaguan_inmobiliarias.dto.response.people.PeopleResponse;
import com.manuel.zaguan_inmobiliarias.dto.response.people.PeopleSummaryResponse;
import com.manuel.zaguan_inmobiliarias.entity.people.People;
import org.springframework.stereotype.Component;

@Component
public class PeopleMapper {

    public People toEntity(PeopleRequest peopleRequest){
        People people = new People();
        people.setName(peopleRequest.getName());
        people.setEmail(peopleRequest.getEmail());
        people.setCuit(peopleRequest.getCuit());
        people.setAddress(peopleRequest.getAddress());
        people.setPhone(peopleRequest.getPhone());
        people.setDni(peopleRequest.getDni());
        people.setAgencyId(peopleRequest.getAgencyId());

        return people;
    }

    public PeopleResponse toResponse(People people){
        PeopleResponse peopleResponse = new PeopleResponse();
        peopleResponse.setId(people.getId());
        peopleResponse.setName(people.getName());
        peopleResponse.setEmail(people.getEmail());
        peopleResponse.setDni(people.getDni());
        peopleResponse.setPhone(people.getPhone());
        peopleResponse.setCuit(people.getCuit());
        peopleResponse.setAddress(people.getAddress());
        peopleResponse.setAgencyId(people.getAgencyId());
        peopleResponse.setCreatedAt(people.getCreatedAt());
        peopleResponse.setUpdatedAt(people.getUpdatedAt());

        return peopleResponse;
    }

    //Para mostrar la persona dentro de un lead o de una parte de contrato
    public PeopleSummaryResponse toSummary(People people){
        PeopleSummaryResponse summary = new PeopleSummaryResponse();
        summary.setId(people.getId());
        summary.setName(people.getName());
        summary.setPhone(people.getPhone());
        summary.setEmail(people.getEmail());
        summary.setDni(people.getDni());

        return summary;
    }
}
