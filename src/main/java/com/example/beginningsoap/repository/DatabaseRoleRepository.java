package com.example.beginningsoap.repository;

import com.example.beginningsoap.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DatabaseRoleRepository extends JpaRepository<Role, Long> {
    public Role getRoleById(Long id);
}
