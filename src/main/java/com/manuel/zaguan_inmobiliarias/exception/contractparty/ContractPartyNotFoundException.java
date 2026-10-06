package com.manuel.zaguan_inmobiliarias.exception.contractparty;

public class ContractPartyNotFoundException extends RuntimeException {
    public ContractPartyNotFoundException(Long id) {
        super("Contract party with id: " + id + " not found.");
    }
}
