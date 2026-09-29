package com.manuel.zaguan_inmobiliarias.repository.agency;

import com.manuel.zaguan_inmobiliarias.entity.agency.Agency;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JpaAgencyRepository extends JpaRepository<Agency, Long> {

    //Las vigentes. Una dada de baja se busca con findById pelado, para poder restaurarla
    Optional<Agency> findByIdAndActiveTrue(@NonNull Long id);

    //Con true salen las vigentes, con false las dadas de baja
    Page<Agency> findAllByActive(Boolean active, Pageable pageable);

    boolean existsByIdAndActiveTrue(@NonNull Long id);

    boolean existsByCuit(@NonNull String cuit);

    boolean existsByCompanyName(@NonNull String companyName);

    boolean existsByEmail(@NonNull String email);

    boolean existsByPhoneNumber(@NonNull String phoneNumber);

    boolean existsByAddress(@NonNull String address);

    //Para editar: busca el valor en otras inmobiliarias, sin contar la que se esta editando
    boolean existsByCuitAndIdNot(@NonNull String cuit, @NonNull Long id);

    boolean existsByCompanyNameAndIdNot(@NonNull String companyName, @NonNull Long id);

    boolean existsByEmailAndIdNot(@NonNull String email, @NonNull Long id);

    boolean existsByPhoneNumberAndIdNot(@NonNull String phoneNumber, @NonNull Long id);

    boolean existsByAddressAndIdNot(@NonNull String address, @NonNull Long id);
}
