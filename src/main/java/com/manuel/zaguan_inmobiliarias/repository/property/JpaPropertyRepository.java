package com.manuel.zaguan_inmobiliarias.repository.property;

import com.manuel.zaguan_inmobiliarias.entity.property.Property;
import org.hibernate.annotations.BatchSize;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JpaPropertyRepository  extends JpaRepository<Property, Long> {

    @EntityGraph(attributePaths = "photos") //problema N+1 preguntar
    Optional<Property> findByIdAndActiveTrue(Long id);

    //Los listados reciben active por parametro: con true salen las vigentes,
    //con false las dadas de baja, que es la unica forma de encontrarlas para restaurarlas
    @BatchSize(size = 20)
    Page<Property> findAllByActive(Boolean active, Pageable pageable);

    Page<Property> findAllByIdAgencyAndActive(Long idAgency, Boolean active, Pageable pageable);

    boolean existsByIdAndActiveTrue(Long id);
}
