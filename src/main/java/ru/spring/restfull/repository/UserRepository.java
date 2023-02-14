package ru.spring.restfull.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.spring.restfull.model.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {

    Optional<User> findByLoginAndPassword(String login, String password);
}
