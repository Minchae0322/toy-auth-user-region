package com.example.toyauth.app.user.repository;

import com.example.toyauth.app.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
