package com.manuel.zaguan_inmobiliarias.util;

import com.manuel.zaguan_inmobiliarias.exception.property.InvalidSearchFilterException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

//Para los endpoints que reciben page y size como @RequestParam y arman el PageRequest a mano.
//PageRequest.of tira IllegalArgumentException con page negativo o size menor a 1, y eso salia como 500.
//Los que reciben un Pageable no lo necesitan: Spring ya corrige page y size antes de llegar al controller
public class PageRequests {

    public static PageRequest of(int page, int size) {
        return of(page, size, Sort.unsorted());
    }

    public static PageRequest of(int page, int size, Sort sort) {
        if (page < 0) {
            throw new InvalidSearchFilterException("page cannot be negative");
        }
        if (size < 1) {
            throw new InvalidSearchFilterException("size must be at least 1");
        }
        return PageRequest.of(page, size, sort);
    }
}
