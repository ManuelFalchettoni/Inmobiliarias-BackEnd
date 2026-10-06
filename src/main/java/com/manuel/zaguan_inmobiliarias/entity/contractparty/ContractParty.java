package com.manuel.zaguan_inmobiliarias.entity.contractparty;

import com.manuel.zaguan_inmobiliarias.enums.contractparty.ContractRole;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "contract_parties")
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class ContractParty {

    private Long id;

    private Long contractId;

    private Long peopleId;

    private ContractRole role;

    private String comments;

}
