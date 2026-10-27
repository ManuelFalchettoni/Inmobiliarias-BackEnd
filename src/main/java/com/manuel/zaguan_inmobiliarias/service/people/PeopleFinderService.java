package com.manuel.zaguan_inmobiliarias.service.people;

import com.manuel.zaguan_inmobiliarias.dto.response.people.PeopleResponse;
import com.manuel.zaguan_inmobiliarias.entity.people.People;
import com.manuel.zaguan_inmobiliarias.exception.people.PeopleNotFoundException;
import com.manuel.zaguan_inmobiliarias.mapper.people.PeopleMapper;
import com.manuel.zaguan_inmobiliarias.repository.people.JpaPeopleRepository;
import com.manuel.zaguan_inmobiliarias.service.auth.AgencyScopeService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class PeopleFinderService {
    private final JpaPeopleRepository jpaPeopleRepository;
    private final PeopleMapper peopleMapper;
    private final AgencyScopeService agencyScopeService;

    //Una persona de otra inmobiliaria responde 404, igual que una que no existe
    public PeopleResponse findById(Long id){
        People people = jpaPeopleRepository.findById(id)
                .orElseThrow( () -> new PeopleNotFoundException(id));

        if (!agencyScopeService.canAccess(people.getAgencyId())) {
            throw new PeopleNotFoundException(id);
        }
        return peopleMapper.toResponse(people);
    }

    //El listado es la busqueda sin filtros: asi la regla de la inmobiliaria esta en un solo lugar
    public Page<PeopleResponse> findAll(Pageable pageable){
        return search(null, null, null, null, null, null, null, pageable);
    }

    //agencyId lo respeta solo el admin; en null busca en todas
    public Page<PeopleResponse> search(Long agencyId, String name, String email, String phone,
                                       String address, String dni, String cuit, Pageable pageable){
        Long scopedAgencyId = agencyScopeService.resolveAgencyId(agencyId);
        Page<People> peoples = jpaPeopleRepository.search(scopedAgencyId, name, email, phone, address, dni, cuit, pageable);

        return peoples.map(
                peopleMapper :: toResponse
        );
    }
}
