package com.example.toyauth.app.user.repository;


import com.example.toyauth.app.auth.domain.MyUserDetails;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RedisUserRepository extends CrudRepository<MyUserDetails, String> {
}

