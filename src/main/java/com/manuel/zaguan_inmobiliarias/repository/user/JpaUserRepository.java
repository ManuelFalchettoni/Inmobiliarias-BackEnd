package com.manuel.zaguan_inmobiliarias.repository.user;

import com.manuel.zaguan_inmobiliarias.entity.user.User;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository

public interface JpaUserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {

    Optional<User> findUserById(@NonNull Long id);

    Optional<User> findByEmail(String email);

    boolean existsById(@NonNull Long id);

    boolean existsByEmail(@NonNull String email);

}
