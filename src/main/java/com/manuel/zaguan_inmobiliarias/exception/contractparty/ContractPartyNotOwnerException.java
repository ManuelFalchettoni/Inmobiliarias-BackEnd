package com.manuel.zaguan_inmobiliarias.exception.contractparty;

//Se quiso cargar como OWNER (propietario) a una persona que no figura como duena de la
//propiedad del contrato. Los duenos se cargan antes, en la propiedad (property_owners)
public class ContractPartyNotOwnerException extends RuntimeException {
    public ContractPartyNotOwnerException(Long peopleId, Long propertyId) {
        super("Person with id: " + peopleId + " is not an owner of property with id: " + propertyId + ".");
    }
}
