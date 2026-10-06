package com.manuel.zaguan_inmobiliarias.dto.request.contractparty;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class ContractPartyRequest {
    @NotNull
    private Long contractId;

    @NotNull
    private Long peopleId;

    private String comments;
}
