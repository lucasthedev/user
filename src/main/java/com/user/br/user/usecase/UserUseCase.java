package com.user.br.user.usecase;

import com.user.br.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserUseCase {
    List<User> findAll();
    Page<User> findAll(Pageable pageable);

    User create(User user);

    Optional<User> findById(UUID userId);

    void delete(UUID userId);

    void save(User user);

    boolean existsByUserName(String s);

    boolean existsByEmail(String email);
}
