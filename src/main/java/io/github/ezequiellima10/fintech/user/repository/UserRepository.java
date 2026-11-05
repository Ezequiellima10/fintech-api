package io.github.ezequiellima10.fintech.user.repository;

import io.github.ezequiellima10.fintech.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByEmail(String email);

    boolean existsByUsername(String username);

}

