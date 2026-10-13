package com.manuel.zaguan_inmobiliarias.repository.people;

import com.manuel.zaguan_inmobiliarias.entity.people.People;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JpaPeopleRepository extends JpaRepository<People, Long> {
    Optional<People> findByName(@NonNull String name);
    Optional<People> findByPhone(@NonNull String phone);
    Optional<People> findByEmail(@NonNull String email);
    Optional<People> findByAddress(@NonNull String address);
    Optional<People> findByDni(@NonNull String dni);
    Optional<People> findByCuit(@NonNull String cuit);

    boolean existsByName(@NonNull String name);
    boolean existsByPhone(@NonNull String phone);
    boolean existsByEmail(@NonNull String email);
    boolean existsByAddress(@NonNull String address);
    boolean existsByDni(@NonNull String dni);
    boolean existsByCuit(@NonNull String cuit);
}
