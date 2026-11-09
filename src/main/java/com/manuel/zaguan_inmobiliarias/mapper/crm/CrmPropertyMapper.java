package com.manuel.zaguan_inmobiliarias.mapper.crm;

import com.manuel.zaguan_inmobiliarias.dto.request.crm.CrmPropertyRequest;
import com.manuel.zaguan_inmobiliarias.dto.response.crm.CrmPropertyResponse;
import com.manuel.zaguan_inmobiliarias.dto.response.user.UserSummaryResponse;
import com.manuel.zaguan_inmobiliarias.entity.crm.CrmProperty;
import com.manuel.zaguan_inmobiliarias.entity.people.People;
import com.manuel.zaguan_inmobiliarias.entity.property.Property;
import com.manuel.zaguan_inmobiliarias.entity.user.User;
import com.manuel.zaguan_inmobiliarias.mapper.people.PeopleMapper;
import com.manuel.zaguan_inmobiliarias.mapper.property.PropertyMapper;
import com.manuel.zaguan_inmobiliarias.repository.people.JpaPeopleRepository;
import com.manuel.zaguan_inmobiliarias.repository.property.JpaPropertyRepository;
import com.manuel.zaguan_inmobiliarias.repository.user.JpaUserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@AllArgsConstructor
public class CrmPropertyMapper {
    private final JpaPropertyRepository jpaPropertyRepository;
    private final JpaPeopleRepository jpaPeopleRepository;
    private final JpaUserRepository jpaUserRepository;
    private final PropertyMapper propertyMapper;
    private final PeopleMapper peopleMapper;

    public CrmProperty toEntity(CrmPropertyRequest request){
        CrmProperty crmProperty = new CrmProperty();
        updateEntity(request, crmProperty);
        return crmProperty;
    }

    public void updateEntity(CrmPropertyRequest request, CrmProperty crmProperty){
        crmProperty.setPropertyId(request.getPropertyId());
        crmProperty.setPeopleId(request.getPeopleId());
        crmProperty.setUserId(request.getUserId());
        crmProperty.setStage(request.getStage());
    }

    public CrmPropertyResponse toResponse(CrmProperty crmProperty){
        return toResponses(List.of(crmProperty)).get(0);
    }

    //Varios leads juntos, con su propiedad, su persona y su agente. Cada tabla se consulta una sola vez
    //para la pagina entera (findAllById), en vez de una vez por lead
    public List<CrmPropertyResponse> toResponses(List<CrmProperty> crmProperties){
        List<Long> propertyIds = crmProperties.stream().map(CrmProperty::getPropertyId).distinct().toList();
        List<Long> peopleIds = crmProperties.stream().map(CrmProperty::getPeopleId).distinct().toList();
        List<Long> userIds = crmProperties.stream().map(CrmProperty::getUserId).distinct().toList();

        //findAllById y no el de activas: la propiedad archivada o el agente dado de baja se siguen mostrando
        Map<Long, Property> propertyById = new HashMap<>();
        for (Property property : jpaPropertyRepository.findAllById(propertyIds)) {
            propertyById.put(property.getId(), property);
        }
        Map<Long, People> peopleById = new HashMap<>();
        for (People people : jpaPeopleRepository.findAllById(peopleIds)) {
            peopleById.put(people.getId(), people);
        }
        Map<Long, User> userById = new HashMap<>();
        for (User user : jpaUserRepository.findAllById(userIds)) {
            userById.put(user.getId(), user);
        }

        List<CrmPropertyResponse> responses = new ArrayList<>();
        for (CrmProperty crmProperty : crmProperties) {
            CrmPropertyResponse response = new CrmPropertyResponse();
            response.setId(crmProperty.getId());
            response.setPropertyId(crmProperty.getPropertyId());
            response.setPeopleId(crmProperty.getPeopleId());
            response.setUserId(crmProperty.getUserId());
            response.setStage(crmProperty.getStage());
            response.setCreatedAt(crmProperty.getCreatedAt());
            response.setUpdatedAt(crmProperty.getUpdatedAt());

            Property property = propertyById.get(crmProperty.getPropertyId());
            if (property != null) {
                response.setProperty(propertyMapper.toSummary(property));
            }
            People people = peopleById.get(crmProperty.getPeopleId());
            if (people != null) {
                response.setPerson(peopleMapper.toSummary(people));
            }
            User user = userById.get(crmProperty.getUserId());
            if (user != null) {
                response.setAgent(new UserSummaryResponse(user.getId(), user.getName()));
            }

            responses.add(response);
        }
        return responses;
    }
}
