package com.manuel.zaguan_inmobiliarias.exception.crm;

public class OfferNotFoundException extends RuntimeException {
    public OfferNotFoundException(Long id) {
        super("Offer with id: " + id + " not found.");
    }
}
