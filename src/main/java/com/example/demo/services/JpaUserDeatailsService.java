package com.example.demo.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.example.demo.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class JpaUserDeatailsService implements UserDetailsService{

	@Autowired
	private UserRepository userRepository;

	@Override
	@Transactional(readOnly = true)
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// TODO Auto-generated method stub

		Optional<com.example.demo.models.entities.User> o = userRepository.findByUsername(username);
		if(!o.isPresent()) {
			throw new UsernameNotFoundException(String.format("Username do not exists", username));
		}

		com.example.demo.models.entities.User user = o.orElseThrow();

		List<GrantedAuthority> authorities =new ArrayList<>();

		authorities.add(new SimpleGrantedAuthority("ROLE_USER"));

		return new User(user.getUsername(),
				user.getPassword(),
				true,
				true,
				true,
				true,
				authorities);

	}

	
	
}
