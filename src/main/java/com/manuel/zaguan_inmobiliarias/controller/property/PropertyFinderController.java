package com.manuel.zaguan_inmobiliarias.controller.property;

import com.manuel.zaguan_inmobiliarias.dto.request.property.PropertySearchFilter;
import com.manuel.zaguan_inmobiliarias.dto.response.property.PropertyResponse;
import com.manuel.zaguan_inmobiliarias.service.property.PropertyFinderService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/properties")
public class PropertyFinderController {
    private final PropertyFinderService propertyFinderService;

    public PropertyFinderController (PropertyFinderService propertyFinderService){
        this.propertyFinderService = propertyFinderService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<PropertyResponse> findById(@PathVariable Long id){
        return ResponseEntity.ok(propertyFinderService.findById(id));
    }

    //active por defecto en true: el que no lo manda sigue viendo solo las vigentes, como antes.
    //Con active=false salen las dadas de baja, para poder restaurarlas.
    @GetMapping
    public ResponseEntity<Page<PropertyResponse>> findAll(
            @RequestParam(required = false) Long agencyId,
            @RequestParam(defaultValue = "true") Boolean active,
            @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable){
            //agencyId lo respeta solo el admin: una inmobiliaria ve siempre las suyas
            return ResponseEntity.ok(propertyFinderService.findAll(agencyId, active, pageable));
    }

    //Todos los filtros son opcionales; la lista esta en PropertySearchFilter. Los enums van con el
    //nombre de la constante y aceptan varios: type=HOUSE,PH. Ejemplo: /api/properties/search?city=cordoba&minRooms=3
    //El precio necesita currency: /api/properties/search?operationType=SALE&currency=USD&maxPrice=100000&sort=price,asc
    @GetMapping("/search")
    public ResponseEntity<Page<PropertyResponse>> search(
            PropertySearchFilter filter,
            @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable){
        Page<PropertyResponse> properties = propertyFinderService.search(filter, pageable);

        return ResponseEntity.ok(properties);
    }
}
