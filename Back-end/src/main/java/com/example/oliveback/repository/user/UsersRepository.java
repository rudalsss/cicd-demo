package com.example.oliveback.repository.user;

import com.example.oliveback.domain.user.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UsersRepository extends JpaRepository<Users, Long> {
    Optional<Users> findByUsername(String username);

    Optional<Object> findByEmail(String email);
}
