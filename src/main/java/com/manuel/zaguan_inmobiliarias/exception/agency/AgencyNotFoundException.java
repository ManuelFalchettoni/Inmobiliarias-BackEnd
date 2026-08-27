package com.manuel.zaguan_inmobiliarias.exception.agency;

import lombok.Getter;


@Getter
public class AgencyNotFoundException extends RuntimeException {
    private final Long agencyId;

    public AgencyNotFoundException(Long agencyId){
        super("Agency not found with id: " + agencyId);
        this.agencyId = agencyId;
    }
    public AgencyNotFoundException(String message) {
        super(message);
        this.agencyId = null;
    }
}
