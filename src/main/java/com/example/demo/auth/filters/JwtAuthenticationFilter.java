package com.example.demo.auth.filters;

import java.io.IOException;
import java.util.*;

import com.example.demo.auth.TokenJwtConfig;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.example.demo.models.entities.User;
import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter{

	
	private AuthenticationManager authenticationManager;
	
	public JwtAuthenticationFilter(AuthenticationManager authenticationManager) {
		this.authenticationManager = authenticationManager;
	}

	@Override
	//in this part the user is authenticated
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {
		// TODO Auto-generated method stub
		
		User user = null;
		String username = null;
		String password = null;
		

		try {
			user = new ObjectMapper().readValue(request.getInputStream(), User.class);
			username = user.getUsername();
			password = user.getPassword();
			
			//logger.info("username from request InputStream(), " + username);
			//logger.info("password form re..." + password);
		} catch (StreamReadException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DatabindException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		UsernamePasswordAuthenticationToken authnToken = new UsernamePasswordAuthenticationToken(username, password);
		return authenticationManager.authenticate(authnToken);
	}

	@Override
	//in this part the token is signed
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
		Authentication authResult) throws IOException, ServletException {
		
		String username = ((org.springframework.security.core.userdetails.User) authResult.getPrincipal()).getUsername();

		//getting roles of user
		Collection <? extends GrantedAuthority> roles = authResult.getAuthorities();
		boolean isAdmin = roles.stream().anyMatch(role -> role.getAuthority().equals("ROLE_ADMIN"));
		Claims claims = Jwts.claims();
		claims.put("authorities", new ObjectMapper().writeValueAsString(roles)); //convert json to string
		claims.put("isAdmin", isAdmin);

		//String originalInputString = TokenJwtConfig.SECRET + "." + username;
		String token= Jwts.builder()
				.setClaims(claims)
				.setSubject(username)
				.signWith(TokenJwtConfig.SECRET)
				.setIssuedAt(new Date())
				.setExpiration(new Date(System.currentTimeMillis() + 3600000))
				.compact();
		
		response.addHeader(TokenJwtConfig.SECRET_KEY, TokenJwtConfig.PREFIX_TOKEN + token);
		
		Map<String, Object> body = new HashMap<>();
		body.put("token", token);
		body.put("message", String.format("Hi $s, you have started session", username));
		body.put("username", username);
		response.getWriter().write(new ObjectMapper().writeValueAsString(body));
		
		response.setStatus(200);
		
		response.setContentType("application/json");
	}

	@Override
	//in this part is ejected when there is an error
	protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException failed) throws IOException, ServletException {
		// TODO Auto-generated method stub
		
		Map<String,Object> body = new HashMap<>();
		body.put("message", "Error");
		body.put("error", failed.getMessage());
		response.getWriter().write(new ObjectMapper().writeValueAsString(body));
		response.setStatus(401);
		
		response.setContentType("application/json");
		
	
	}

	
	
}
