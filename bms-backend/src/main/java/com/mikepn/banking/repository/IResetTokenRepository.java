package com.mikepn.banking.repository;

import com.mikepn.banking.model.ResetToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface IResetTokenRepository extends JpaRepository<ResetToken, UUID> {
    Optional<ResetToken> findByToken(String token);
}
