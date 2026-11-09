package com.manuel.zaguan_inmobiliarias.repository.auth;

import com.manuel.zaguan_inmobiliarias.entity.auth.PasswordResetToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface JpaPasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {

    Optional<PasswordResetToken> findByTokenHash(String tokenHash);

    //Para no mandar un mail tras otro si alguien aprieta el boton varias veces
    boolean existsByUserIdAndUsedAtIsNullAndCreatedAtAfter(Long userId, LocalDateTime after);

    //Pedir un link nuevo o cambiar la contraseña invalida los que quedaban sin usar
    @Modifying
    @Query("UPDATE PasswordResetToken t SET t.usedAt = :now WHERE t.userId = :userId AND t.usedAt IS NULL")
    void invalidateAllForUser(@Param("userId") Long userId, @Param("now") LocalDateTime now);
}
