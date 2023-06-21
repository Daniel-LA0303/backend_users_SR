package com.example.demo.services;

import java.util.List;
import java.util.Optional;

import com.example.demo.models.dto.UserDto;
import com.example.demo.models.entities.User;
import com.example.demo.models.request.UserRequest;

public interface UserService {
	
	List<UserDto> findAll();
	
	Optional<UserDto> findById(Long id);
	
	UserDto save(User user);
	
	Optional<UserDto> update(UserRequest user, Long id);
	
	void remove(Long id);

}
