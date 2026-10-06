package com.manuel.zaguan_inmobiliarias.controller.crm;

import com.manuel.zaguan_inmobiliarias.dto.response.crm.CrmPropertyResponse;
import com.manuel.zaguan_inmobiliarias.service.crm.CrmPropertyFinderService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/crm_properties")
@AllArgsConstructor
public class CrmPropertyFinderController {
    private final CrmPropertyFinderService crmPropertyFinderService;

    @GetMapping("/{id}")
    public ResponseEntity<CrmPropertyResponse> findById(@PathVariable Long id){
        return ResponseEntity.ok(crmPropertyFinderService.findById(id));
    }

    //Pageable como en UsersGetController. Con userId salen solo los leads de ese agente
    @GetMapping
    public ResponseEntity<Page<CrmPropertyResponse>> findAll(
            @RequestParam(required = false) Long userId,
            @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable){

        Page<CrmPropertyResponse> responses = (userId == null)
                ? crmPropertyFinderService.findAll(pageable)
                : crmPropertyFinderService.findByUser(userId, pageable);
        return ResponseEntity.ok(responses);
    }
}
