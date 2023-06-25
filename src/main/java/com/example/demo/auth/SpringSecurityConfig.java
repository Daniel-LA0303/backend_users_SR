package com.example.demo.auth;

import com.example.demo.auth.filters.JtwValidationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.example.demo.auth.filters.JwtAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.ArrayList;
import java.util.Arrays;

@Configuration //indicates that this class is a Spring configuration and contains one or more beans.
public class SpringSecurityConfig {
	
	@Autowired //This means that Spring is expected to solve and inject an instance of AuthenticationConfiguration into this field.
	private AuthenticationConfiguration authenticationConfiguration;

	@Bean
	//this method only encrypts the password
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();

	}

	@Bean
	//is responsible for authenticating authentication requests
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
				//we define permissions depending on roles
				authRules
				        .requestMatchers(HttpMethod.GET, "/users").permitAll()
						.requestMatchers(HttpMethod.GET, "/users/{id}").hasAnyRole("USER", "ADMIN")
						.requestMatchers(HttpMethod.POST, "/users").hasRole("ADMIN")
						.requestMatchers("/users/**").hasRole("ADMIN") //this represents the delete and put method
						//.requestMatchers(HttpMethod.PUT, "/users/{id}").hasRole("ADMIN")
						//.requestMatchers(HttpMethod.DELETE, "/users/{id}").hasRole("ADMIN")
				        .anyRequest().authenticated()
				        	.and()
				        	.addFilter(new JwtAuthenticationFilter(authenticationConfiguration.getAuthenticationManager())) //as an argument in the constructor. This allows the filter to access the AuthenticationManager to perform authentication of user credentials.
						.addFilter(new JtwValidationFilter(authenticationConfiguration.getAuthenticationManager()));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		})
                .csrf(config -> config.disable())
                .sessionManagement(managment -> managment.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.cors(cors -> cors.configurationSource(corsConfigurationSource())) //add cors configuration
                .build();
        
    }

	//Cors configuration
	@Bean
	CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration corsConfiguration = new CorsConfiguration();
		corsConfiguration.setAllowedOrigins(Arrays.asList("http://127.0.0.1:5173"));

		corsConfiguration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE"));
		corsConfiguration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));
		corsConfiguration.setAllowCredentials(true);

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", corsConfiguration);
		return source;
	}

	@Bean
	FilterRegistrationBean<CorsFilter> corsFilter(){
		FilterRegistrationBean<CorsFilter> bean = new FilterRegistrationBean<>(new CorsFilter(corsConfigurationSource()));
		bean.setOrder(Ordered.HIGHEST_PRECEDENCE);
		return bean;
	}
}
