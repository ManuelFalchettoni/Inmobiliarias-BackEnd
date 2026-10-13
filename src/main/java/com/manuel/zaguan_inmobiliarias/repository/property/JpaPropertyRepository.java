package com.manuel.zaguan_inmobiliarias.repository.property;

import com.manuel.zaguan_inmobiliarias.entity.property.Property;
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

    //Busqueda con filtros opcionales: el que llega en null no filtra, asi una sola consulta
    //sirve para cualquier combinacion. Solo trae vigentes.
    //Provincia, partido y ciudad con LIKE: "cord" encuentra "Cordoba". Las mayusculas las
    //ignora el LOWER y los acentos la collation de MySQL (utf8mb4_0900_ai_ci)
    @Query("""
            SELECT p FROM Property p
            WHERE p.active = true
              AND (:type IS NULL OR p.type = :type)
              AND (:province IS NULL OR LOWER(p.province) LIKE LOWER(CONCAT('%', :province, '%')))
              AND (:county IS NULL OR LOWER(p.county) LIKE LOWER(CONCAT('%', :county, '%')))
              AND (:city IS NULL OR LOWER(p.city) LIKE LOWER(CONCAT('%', :city, '%')))
              AND (:rooms IS NULL OR p.rooms = :rooms)
              AND (:condition IS NULL OR p.condition = :condition)
              AND (:occupancy IS NULL OR p.occupancy = :occupancy)
              AND (:minSize IS NULL OR p.size >= :minSize)
              AND (:maxSize IS NULL OR p.size <= :maxSize)
              AND (:minYear IS NULL OR p.year >= :minYear)
              AND (:maxYear IS NULL OR p.year <= :maxYear)
            """)
    Page<Property> search(@Param("type") PropertyType type,
                          @Param("province") String province,
                          @Param("county") String county,
                          @Param("city") String city,
                          @Param("rooms") Integer rooms,
                          @Param("condition") PropertyCondition condition,
                          @Param("occupancy") PropertyOccupancy occupancy,
                          @Param("minSize") Integer minSize,
                          @Param("maxSize") Integer maxSize,
                          @Param("minYear") Integer minYear,
                          @Param("maxYear") Integer maxYear,
                          Pageable pageable);

}
