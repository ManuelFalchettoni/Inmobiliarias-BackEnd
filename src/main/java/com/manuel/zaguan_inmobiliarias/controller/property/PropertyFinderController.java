package com.manuel.zaguan_inmobiliarias.controller.property;

import com.manuel.zaguan_inmobiliarias.dto.response.property.PropertyResponse;
import com.manuel.zaguan_inmobiliarias.enums.property.PropertyCondition;
import com.manuel.zaguan_inmobiliarias.enums.property.PropertyOccupancy;
import com.manuel.zaguan_inmobiliarias.enums.property.PropertyType;
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
            Page<PropertyResponse> properties = (agencyId == null)
                    ? propertyFinderService.findAll(active, pageable)
                    : propertyFinderService.findByAgency(agencyId, active, pageable);

            return ResponseEntity.ok(properties);
    }

    //Todos los filtros son opcionales. Los enums van con el nombre de la constante, por
    //ejemplo type=HOUSE. Ejemplo: /api/properties/search?city=cordoba&rooms=3
    @GetMapping("/search")
    public ResponseEntity<Page<PropertyResponse>> search(
            @RequestParam(required = false) PropertyType type,
            @RequestParam(required = false) String province,
            @RequestParam(required = false) String county,
            @RequestParam(required = false) String city,
            @RequestParam(required = false) Integer rooms,
            @RequestParam(required = false) PropertyCondition condition,
            @RequestParam(required = false) PropertyOccupancy occupancy,
            @RequestParam(required = false) Integer minSize,
            @RequestParam(required = false) Integer maxSize,
            @RequestParam(required = false) Integer minYear,
            @RequestParam(required = false) Integer maxYear,
            @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable){
        Page<PropertyResponse> properties = propertyFinderService.search(type, province, county, city, rooms,
                condition, occupancy, minSize, maxSize, minYear, maxYear, pageable);

        return ResponseEntity.ok(properties);
    }
}
