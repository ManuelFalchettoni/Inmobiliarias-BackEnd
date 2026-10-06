package com.manuel.zaguan_inmobiliarias.dto.request.contractparty;

import com.manuel.zaguan_inmobiliarias.enums.contractparty.ContractRole;
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

    @NotNull
    private ContractRole role;

    private String comments;
}
