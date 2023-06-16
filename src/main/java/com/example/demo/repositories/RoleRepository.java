package com.example.demo.repositories;

import com.example.demo.models.entities.Role;
import com.example.demo.models.entities.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface RoleRepository extends CrudRepository<Role, Long>{


    Optional<Role> findByName(String username);

	
}
