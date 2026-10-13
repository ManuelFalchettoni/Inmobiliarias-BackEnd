package com.manuel.zaguan_inmobiliarias.repository.people;

import com.manuel.zaguan_inmobiliarias.entity.people.People;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaPeopleRepository extends JpaRepository<People, Long> {

    //Busqueda dentro de la cartera de una inmobiliaria: agencyId es obligatorio, asi una
    //inmobiliaria no ve las personas de otra. Los demas filtros son opcionales, el que llega
    //en null no filtra. Todos con LIKE: "garc" encuentra "Garcia", "3012" un DNI que lo contenga
    @Query("""
            SELECT p FROM People p
            WHERE p.agencyId = :agencyId
              AND (:name IS NULL OR LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%')))
              AND (:email IS NULL OR LOWER(p.email) LIKE LOWER(CONCAT('%', :email, '%')))
              AND (:phone IS NULL OR p.phone LIKE CONCAT('%', :phone, '%'))
              AND (:address IS NULL OR LOWER(p.address) LIKE LOWER(CONCAT('%', :address, '%')))
              AND (:dni IS NULL OR p.dni LIKE CONCAT('%', :dni, '%'))
              AND (:cuit IS NULL OR p.cuit LIKE CONCAT('%', :cuit, '%'))
            """)
    Page<People> search(@Param("agencyId") Long agencyId,
                        @Param("name") String name,
                        @Param("email") String email,
                        @Param("phone") String phone,
                        @Param("address") String address,
                        @Param("dni") String dni,
                        @Param("cuit") String cuit,
                        Pageable pageable);
}
