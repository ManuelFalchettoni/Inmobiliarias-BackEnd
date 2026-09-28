package com.manuel.zaguan_inmobiliarias.controller.agency;

import com.manuel.zaguan_inmobiliarias.dto.request.agency.AgencyRequest;
import com.manuel.zaguan_inmobiliarias.dto.response.agency.AgencyResponse;
import com.manuel.zaguan_inmobiliarias.service.agency.AgencyUpdaterService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api/agencies")
public class AgencyPutController {
    private final AgencyUpdaterService agencyUpdaterService;

    @PutMapping("/{id}")
    public ResponseEntity<AgencyResponse> put (@PathVariable Long id,
                                               @Valid @RequestBody AgencyRequest agencyRequest){
       AgencyResponse agencyResponse = agencyUpdaterService.update(id, agencyRequest);

       return ResponseEntity.ok(agencyResponse);
    }
}
