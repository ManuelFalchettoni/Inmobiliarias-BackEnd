package com.manuel.zaguan_inmobiliarias.repository.property;

import com.manuel.zaguan_inmobiliarias.entity.property.Property;
import com.manuel.zaguan_inmobiliarias.enums.Currency;
import com.manuel.zaguan_inmobiliarias.enums.property.OperationType;
import com.manuel.zaguan_inmobiliarias.enums.property.PropertyCondition;
import com.manuel.zaguan_inmobiliarias.enums.property.PropertyOccupancy;
import com.manuel.zaguan_inmobiliarias.enums.property.PropertyType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface JpaPropertyRepository  extends JpaRepository<Property, Long> {

    //EntityGraph para traer la propiedad y sus fotos en una sola consulta. En los listados
    //no se usa: un fetch join con Pageable obliga a Hibernate a paginar en memoria.
    //Ahi el N+1 lo corta el @BatchSize de la coleccion en Property
    @EntityGraph(attributePaths = "photos")
    Optional<Property> findByIdAndActiveTrue(Long id);

    //Los listados reciben active por parametro: con true salen las vigentes,
    //con false las dadas de baja, que es la unica forma de encontrarlas para restaurarlas
    Page<Property> findAllByActive(Boolean active, Pageable pageable);

    Page<Property> findAllByAgencyIdAndActive(Long agencyId, Boolean active, Pageable pageable);

    boolean existsByIdAndActiveTrue(Long id);

    //Para Analitica: activas y dadas de baja, sin paginar. agencyId en null (solo el admin) trae todas
    @Query("""
            SELECT p FROM Property p
            WHERE (:agencyId IS NULL OR p.agencyId = :agencyId)
            """)
    List<Property> findAllForAnalytics(@Param("agencyId") Long agencyId);

    //Busqueda con filtros opcionales: el que llega en null no filtra, asi una sola consulta
    //sirve para cualquier combinacion. Solo trae vigentes.
    //Provincia, partido y ciudad con LIKE: "cord" encuentra "Cordoba". Las mayusculas las
    //ignora el LOWER y los acentos la collation de MySQL (utf8mb4_0900_ai_ci)
    //Los filtros de precio van juntos en un EXISTS: el mismo precio tiene que cumplir operacion,
    //moneda y rango, asi "venta en USD hasta 100000" no matchea con el precio del alquiler
    @Query("""
            SELECT p FROM Property p
            WHERE p.active = true
              AND (:agencyId IS NULL OR p.agencyId = :agencyId)
              AND (:type IS NULL OR p.type = :type)
              AND (:province IS NULL OR LOWER(p.province) LIKE LOWER(CONCAT('%', :province, '%')))
              AND (:county IS NULL OR LOWER(p.county) LIKE LOWER(CONCAT('%', :county, '%')))
              AND (:city IS NULL OR LOWER(p.city) LIKE LOWER(CONCAT('%', :city, '%')))
              AND (:minRooms IS NULL OR p.rooms >= :minRooms)
              AND (:maxRooms IS NULL OR p.rooms <= :maxRooms)
              AND (:condition IS NULL OR p.condition = :condition)
              AND (:occupancy IS NULL OR p.occupancy = :occupancy)
              AND (:minSize IS NULL OR p.size >= :minSize)
              AND (:maxSize IS NULL OR p.size <= :maxSize)
              AND (:minYear IS NULL OR p.year >= :minYear)
              AND (:maxYear IS NULL OR p.year <= :maxYear)
              AND ((:operationType IS NULL AND :currency IS NULL AND :minPrice IS NULL AND :maxPrice IS NULL)
                   OR EXISTS (SELECT pp.id FROM PropertyPrice pp
                              WHERE pp.property = p
                                AND (:operationType IS NULL OR pp.operationType = :operationType)
                                AND (:currency IS NULL OR pp.currency = :currency)
                                AND (:minPrice IS NULL OR pp.amount >= :minPrice)
                                AND (:maxPrice IS NULL OR pp.amount <= :maxPrice)))
            """)
    Page<Property> search(@Param("agencyId") Long agencyId,
                          @Param("type") PropertyType type,
                          @Param("province") String province,
                          @Param("county") String county,
                          @Param("city") String city,
                          @Param("minRooms") Integer minRooms,
                          @Param("maxRooms") Integer maxRooms,
                          @Param("condition") PropertyCondition condition,
                          @Param("occupancy") PropertyOccupancy occupancy,
                          @Param("minSize") Integer minSize,
                          @Param("maxSize") Integer maxSize,
                          @Param("minYear") Integer minYear,
                          @Param("maxYear") Integer maxYear,
                          @Param("operationType") OperationType operationType,
                          @Param("currency") Currency currency,
                          @Param("minPrice") BigDecimal minPrice,
                          @Param("maxPrice") BigDecimal maxPrice,
                          Pageable pageable);

}
