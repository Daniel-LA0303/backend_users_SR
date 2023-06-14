package com.example.demo.auth;

import com.example.demo.auth.filters.JtwValidationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.example.demo.auth.filters.JwtAuthenticationFilter;

@Configuration //indicates that this class is a Spring configuration and contains one or more beans.
public class SpringSecurityConfig {
	
	@Autowired //This means that Spring is expected to solve and inject an instance of AuthenticationConfiguration into this field.
	private AuthenticationConfiguration authenticationConfiguration;

	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();

	}

	@Bean
	AuthenticationManager authenticationManager() throws Exception {
		return authenticationConfiguration.getAuthenticationManager();
	}

	@Bean
	/*based on the configuration provided by the HttpSecurity object.
	Here, authorization is configured for different routes and HTTP methods.
	In your case, all GET requests to the "/users" path are allowed to be made without authentication,
	while any other request requires//authentication. A JwtAuthenticationFilter is also added as a custom filter to authenticate requests using JWT.*/
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http.authorizeHttpRequests(authRules -> {
			try {
				authRules
				        .requestMatchers(HttpMethod.GET, "/users").permitAll()
				        .anyRequest().authenticated()
				        	.and()
				        	.addFilter(new JwtAuthenticationFilter(authenticationConfiguration.getAuthenticationManager()))
						.addFilter(new JtwValidationFilter(authenticationConfiguration.getAuthenticationManager()));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		})
                .csrf(config -> config.disable())
                .sessionManagement(managment -> managment.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .build();
        
    }
}
