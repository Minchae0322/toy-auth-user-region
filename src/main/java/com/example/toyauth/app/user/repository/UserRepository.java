package com.example.toyauth.app.user.repository;

import com.example.toyauth.app.common.enumuration.Provider;
import com.example.toyauth.app.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByProviderIdAndProviderAndActivated(String providerId, Provider provider, boolean activated);

    Optional<User> findByIdAndActivated(Long userId, boolean activated);

    Optional<User> findByUsernameAndProvider(String username, Provider provider);
}
