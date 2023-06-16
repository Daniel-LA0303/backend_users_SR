package com.example.demo.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.example.demo.models.entities.Role;
import com.example.demo.repositories.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.models.entities.User;
import com.example.demo.models.request.UserRequest;
import com.example.demo.repositories.UserRepository;


@Service
public class UserServiceImpl implements UserService{
	
	
	@Autowired
	private UserRepository repository;

	@Autowired
	private PasswordEncoder passwordEncoder;


	@Autowired
	private RoleRepository roleRepository;

	@Override
	@Transactional(readOnly = true)
	public List<User> findAll() { //we could use a dto in this part
		// TODO Auto-generated method stub
		return (List<User>) repository.findAll();
	}

	@Override
	@Transactional(readOnly = true)
	public Optional<User> findById(Long id) {
		// TODO Auto-generated method stub
		return repository.findById(id);
	}

	@Override
	@Transactional
	public User save(User user) {
		// TODO Auto-generated method stub

		user.setPassword(passwordEncoder.encode(user.getPassword())); //encrypting the password

		Optional<Role> o = roleRepository.findByName("ROLE_USER");

		List<Role> roles = new ArrayList<>();

		if (o.isPresent()) {
			roles.add(o.orElseThrow());
		}
		user.setRoles(roles);


		return repository.save(user);
	}

	@Override
	@Transactional
	public void remove(Long id) {
		// TODO Auto-generated method stub
		repository.deleteById(id);
	}

	@Override
	@Transactional
	public Optional<User> update(UserRequest user, Long id) {
		// TODO Auto-generated method stub
		Optional<User> o = this.findById(id);
		User userOptional = null;
		if (o.isPresent()) {
			User userDB = o.orElseThrow();
			userDB.setUsername(user.getUsername());
			userDB.setEmail(user.getEmail());
			//return Optional.of(this.save(userDB));
			userOptional = this.save(userDB);
		}
		//return Optional.empty();
		return Optional.ofNullable(userOptional);
	}

	
	
}
