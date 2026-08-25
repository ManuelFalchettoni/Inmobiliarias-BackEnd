package com.manuel.zaguan_inmobiliarias.controller.property;

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
public class PropertyGetController {
    private final PropertyFinderService propertyFinderService;

    public PropertyGetController (PropertyFinderService propertyFinderService){
        this.propertyFinderService = propertyFinderService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<PropertyResponse> findById(@PathVariable Long id){
        return ResponseEntity.ok(propertyFinderService.findById(id));
    }

    @GetMapping
    public ResponseEntity<Page<PropertyResponse>> findAll(
            @RequestParam(required = false) Long idAgency,
            @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable){
            Page<PropertyResponse> properties = (idAgency == null)
                    ? propertyFinderService.findAll(pageable)
                    : propertyFinderService.findByAgency(idAgency, pageable);

            return ResponseEntity.ok(properties);
    }
}
