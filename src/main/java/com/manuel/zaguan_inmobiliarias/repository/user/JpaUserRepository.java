package com.manuel.zaguan_inmobiliarias.repository.user;

import com.manuel.zaguan_inmobiliarias.entity.user.User;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository

public interface JpaUserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {

    Optional<User> findUserById(@NonNull Long id);

    Optional<User> findByEmail(String email);

    //Los vigentes. Uno dado de baja se busca con findById pelado, para poder restaurarlo
    Optional<User> findByIdAndActiveTrue(@NonNull Long id);

    //Con true salen los vigentes, con false los dados de baja
    Page<User> findAllByActive(Boolean active, Pageable pageable);

    boolean existsById(@NonNull Long id);

    boolean existsByEmail(@NonNull String email);

    boolean existsByPhoneNumber(@NonNull String phoneNumber);

    //Para editar: busca el valor en otros usuarios, sin contar al que se esta editando
    boolean existsByEmailAndIdNot(@NonNull String email, @NonNull Long id);

    boolean existsByPhoneNumberAndIdNot(@NonNull String phoneNumber, @NonNull Long id);

}
