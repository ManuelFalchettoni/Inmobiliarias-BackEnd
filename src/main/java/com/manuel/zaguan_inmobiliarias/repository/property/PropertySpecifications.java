package com.manuel.zaguan_inmobiliarias.repository.property;

import com.manuel.zaguan_inmobiliarias.dto.request.property.PropertySearchFilter;
import com.manuel.zaguan_inmobiliarias.entity.property.Property;
import com.manuel.zaguan_inmobiliarias.entity.property.price.PropertyPrice;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Order;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.query.QueryUtils;

import java.util.ArrayList;
import java.util.List;

//La busqueda de propiedades armada con Criteria y no con @Query: con listas de enums y el
//orden por precio una consulta fija no alcanza (en Hibernate ":lista IS NULL" falla con mas
//de un valor). Cada filtro que llega en null simplemente no se agrega
public final class PropertySpecifications {

    //Sort.Order con esta propiedad ordena por el monto del precio, no por un campo de Property
    public static final String PRICE_SORT = "price";

    private PropertySpecifications() {
    }

    //Solo trae vigentes. agencyId ya viene resuelto por AgencyScopeService; en null no filtra
    public static Specification<Property> search(PropertySearchFilter filter, Long agencyId) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(cb.isTrue(root.get("active")));

            if (agencyId != null) {
                predicates.add(cb.equal(root.get("agencyId"), agencyId));
            }

            addIn(predicates, root.get("type"), filter.getType());
            addIn(predicates, root.get("condition"), filter.getCondition());
            addIn(predicates, root.get("occupancy"), filter.getOccupancy());

            //LIKE parcial: "cord" encuentra "Cordoba". Las mayusculas las ignora el LOWER y
            //los acentos la collation de MySQL (utf8mb4_0900_ai_ci)
            addLike(predicates, cb, root.get("address"), filter.getAddress());
            addLike(predicates, cb, root.get("province"), filter.getProvince());
            addLike(predicates, cb, root.get("county"), filter.getCounty());
            addLike(predicates, cb, root.get("city"), filter.getCity());

            addRange(predicates, cb, root.get("rooms"), filter.getMinRooms(), filter.getMaxRooms());
            addRange(predicates, cb, root.get("size"), filter.getMinSize(), filter.getMaxSize());
            addRange(predicates, cb, root.get("year"), filter.getMinYear(), filter.getMaxYear());
            addRange(predicates, cb, root.get("floorNumber"), filter.getMinFloor(), filter.getMaxFloor());
            addRange(predicates, cb, root.get("latitude"), filter.getMinLat(), filter.getMaxLat());
            addRange(predicates, cb, root.get("longitude"), filter.getMinLng(), filter.getMaxLng());

            if (hasPriceFilter(filter)) {
                predicates.add(cb.exists(priceSubquery(root, query.subquery(Long.class), cb, filter)));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    //Ordena por el precio de la operacion y moneda del filtro (el service exige las dos). Hay a
    //lo sumo un precio por operacion, asi que el LEFT JOIN no duplica filas. Las demas
    //propiedades del sort se respetan en el orden en que vinieron.
    //Va aparte de search porque la consulta del count no lleva ni el join ni el orden
    public static Specification<Property> sortedByPrice(Specification<Property> base,
                                                        PropertySearchFilter filter, Sort sort) {
        return (root, query, cb) -> {
            Join<Property, PropertyPrice> price = root.join("prices", JoinType.LEFT);
            price.on(cb.equal(price.get("operationType"), filter.getOperationType()),
                    cb.equal(price.get("currency"), filter.getCurrency()));

            List<Order> orders = new ArrayList<>();
            for (Sort.Order order : sort) {
                if (PRICE_SORT.equals(order.getProperty())) {
                    Path<?> amount = price.get("amount");
                    orders.add(order.isAscending() ? cb.asc(amount) : cb.desc(amount));
                } else {
                    orders.addAll(QueryUtils.toOrders(Sort.by(order), root, cb));
                }
            }
            query.orderBy(orders);

            return base.toPredicate(root, query, cb);
        };
    }

    //Los filtros de precio van juntos en un EXISTS: el mismo precio tiene que cumplir operacion,
    //moneda y rango, asi "venta en USD hasta 100000" no matchea con el precio del alquiler
    private static Subquery<Long> priceSubquery(Root<Property> root, Subquery<Long> subquery,
                                                CriteriaBuilder cb, PropertySearchFilter filter) {
        Root<PropertyPrice> price = subquery.from(PropertyPrice.class);
        List<Predicate> predicates = new ArrayList<>();
        predicates.add(cb.equal(price.get("property"), root));

        if (filter.getOperationType() != null) {
            predicates.add(cb.equal(price.get("operationType"), filter.getOperationType()));
        }
        if (filter.getCurrency() != null) {
            predicates.add(cb.equal(price.get("currency"), filter.getCurrency()));
        }
        addRange(predicates, cb, price.get("amount"), filter.getMinPrice(), filter.getMaxPrice());

        return subquery.select(price.get("id")).where(predicates.toArray(new Predicate[0]));
    }

    private static boolean hasPriceFilter(PropertySearchFilter filter) {
        return filter.getOperationType() != null || filter.getCurrency() != null
                || filter.getMinPrice() != null || filter.getMaxPrice() != null;
    }

    private static void addIn(List<Predicate> predicates, Path<?> path, List<?> values) {
        if (values != null && !values.isEmpty()) {
            predicates.add(path.in(values));
        }
    }

    private static void addLike(List<Predicate> predicates, CriteriaBuilder cb, Path<String> path, String value) {
        if (value != null && !value.isBlank()) {
            predicates.add(cb.like(cb.lower(path), "%" + value.toLowerCase() + "%"));
        }
    }

    //Incluye los extremos
    private static <T extends Comparable<? super T>> void addRange(List<Predicate> predicates, CriteriaBuilder cb,
                                                                   Path<T> path, T min, T max) {
        if (min != null) {
            predicates.add(cb.greaterThanOrEqualTo(path, min));
        }
        if (max != null) {
            predicates.add(cb.lessThanOrEqualTo(path, max));
        }
    }
}
