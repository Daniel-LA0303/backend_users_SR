package com.example.demo.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.models.entities.User;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Long>{


    Optional<User> findByUsername(String username);

    @Query("SELECT u FROM User u WHERE u.username=?1") //jpa query
    Optional<User> getUserByUsername(String username);
	
}
