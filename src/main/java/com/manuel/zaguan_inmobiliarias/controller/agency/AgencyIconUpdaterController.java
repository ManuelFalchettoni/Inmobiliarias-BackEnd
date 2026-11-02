package com.manuel.zaguan_inmobiliarias.controller.agency;

import com.manuel.zaguan_inmobiliarias.dto.response.agency.AgencyResponse;
import com.manuel.zaguan_inmobiliarias.service.agency.AgencyIconUpdaterService;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@AllArgsConstructor
@RequestMapping("/api/agencies/{id}/icon")
public class AgencyIconUpdaterController {
    private final AgencyIconUpdaterService agencyIconUpdaterService;

    //PUT y no POST: una inmobiliaria tiene un solo logo, subir otro lo reemplaza
    @PutMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<AgencyResponse> put(@PathVariable Long id,
                                              @RequestPart("file") MultipartFile file) {
        return ResponseEntity.ok(agencyIconUpdaterService.update(id, file));
    }
}
