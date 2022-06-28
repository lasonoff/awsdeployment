package ru.yauroff.awsdeployment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.yauroff.awsdeployment.model.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByLogin(String login);

    Optional<User> findByLoginOrEmail(String login, String email);
}
