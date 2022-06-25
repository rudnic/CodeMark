package com.example.beginningsoap.repository;

import com.example.beginningsoap.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DatabaseUserRepository extends JpaRepository<User, Long> {
    public User getUserByLogin(String login);
    public User getUserById(Long id);
    public Optional<User> findByLogin(String login);
    public void deleteUserByLogin(String login);
}
