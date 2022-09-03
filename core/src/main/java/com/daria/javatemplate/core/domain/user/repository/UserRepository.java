package com.daria.javatemplate.core.domain.user.repository;

import com.daria.javatemplate.core.domain.user.model.entity.UserEntity;
import com.daria.javatemplate.core.domain.user.type.UserProvider;
import io.micrometer.core.instrument.config.validate.Validated;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByEmail(String email);

    Optional<UserEntity> findByProviderAndProviderId(UserProvider provider, String providerId);
}
