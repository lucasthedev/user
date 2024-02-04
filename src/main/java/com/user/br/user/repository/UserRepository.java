package com.user.br.user.repository;

import com.user.br.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    boolean existsByUserName(String userName);
    boolean existsByEmail(String email);
}
