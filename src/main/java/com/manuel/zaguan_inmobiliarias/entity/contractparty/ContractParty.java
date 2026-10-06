package com.manuel.zaguan_inmobiliarias.entity.contractparty;

import com.manuel.zaguan_inmobiliarias.enums.contractparty.ContractRole;
import jakarta.persistence.*;
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

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long contractId;

    @Column(nullable = false)
    private Long peopleId;

    //varchar y no ENUM nativo, por lo mismo que en User
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, columnDefinition = "varchar(30)")
    private ContractRole role;

    @Column
    private String comments;

}
