package com.manuel.zaguan_inmobiliarias.repository.property;

import com.manuel.zaguan_inmobiliarias.entity.property.Property;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
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


    Optional<Property> findByAddress(@NonNull String address);
    Optional<Property> findByType(@NonNull String type);
    Optional<Property> findByProvince(@NonNull String province);
    Optional<Property> findByCounty(@NonNull String county);
    Optional<Property> findByCity(@NonNull String city);
    Optional<Property> findByLatitude(@NonNull Double latitude);
    Optional<Property> findByLongitude(@NonNull Double longitude);
    Optional<Property> findByYear(@NonNull Integer year);
    Optional<Property> findBySize(@NonNull Integer size);
    Optional<Property> findByCondition(@NonNull String condition);
    Optional<Property> findByOccupancy(@NonNull String occupancy);
    Optional<Property> findByRooms(@NonNull Integer rooms);
    Optional<Property> findByFloorNumber(@NonNull Integer floorNumber);

    boolean existsByAddress(@NonNull String address);
    boolean existsByType(@NonNull String type);
    boolean existsByProvince(@NonNull String province);
    boolean existsByCounty(@NonNull String county);
    boolean existsByCity(@NonNull String city);
    boolean existsByLatitude(@NonNull Double latitude);
    boolean existsByLongitude(@NonNull Double longitude);
    boolean existsByYear(@NonNull Integer year);
    boolean existsBySize(@NonNull Integer size);
    boolean existsByCondition(@NonNull String condition);
    boolean existsByOccupancy(@NonNull String occupancy);
    boolean existsByRooms(@NonNull Integer rooms);
    boolean existsByFloorNumber(@NonNull Integer floorNumber);

}
