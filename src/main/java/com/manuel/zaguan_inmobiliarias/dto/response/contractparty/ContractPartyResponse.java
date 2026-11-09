package com.manuel.zaguan_inmobiliarias.dto.response.contractparty;

import com.manuel.zaguan_inmobiliarias.dto.response.people.PeopleSummaryResponse;
import com.manuel.zaguan_inmobiliarias.enums.contractparty.ContractRole;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class ContractPartyResponse {
    private Long id;
    private Long contractId;
    private Long peopleId;
    //En null solo si la persona ya no existe en la base
    private PeopleSummaryResponse person;
    private ContractRole role;
    private String comments;
}
