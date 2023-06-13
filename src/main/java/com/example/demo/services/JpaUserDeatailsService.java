package com.example.demo.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class JpaUserDeatailsService implements UserDetailsService{

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// TODO Auto-generated method stub
		if(!username.equals("admin")) {
			throw new UsernameNotFoundException(String.format("Username do not exists", username));
		}
		
		List<GrantedAuthority> authorities =new ArrayList<>();
		
		authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
		
		return new User(username, "$2a$10$DOMDxjYyfZ/e7RcBfUpzqeaCs8pLgcizuiQWXPkU35nOhZlFcE9MS", true, true, true, true, authorities);
		
	}

	
	
}
