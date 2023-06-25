package com.example.demo.auth.filters;

import com.example.demo.auth.SimpleGrantedAuthorityJsonCreator;
import com.example.demo.auth.TokenJwtConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.jackson2.SimpleGrantedAuthorityMixin;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import java.io.IOException;
import java.util.*;


//This class and its methods are executed when I am already authenticated or logged in, so I would have to send a token and confirm the request if it asks for it.
public class JtwValidationFilter extends BasicAuthenticationFilter {

    //token validation
    public JtwValidationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    //here the token is validated
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {

        String header = request.getHeader(TokenJwtConfig.SECRET_KEY);
        if (header == null || !header.startsWith(TokenJwtConfig.PREFIX_TOKEN)) {
            chain.doFilter(request, response);
            return;
        }

        String token = header.replace(TokenJwtConfig.PREFIX_TOKEN, "");

        try {

            Claims claims = Jwts.parserBuilder().setSigningKey(TokenJwtConfig.SECRET).build().parseClaimsJws(token).getBody();

            //getting roles from token
            Object authoritiesClaims = claims.get("authorities");


            String username = claims.getSubject();

            //convierten el valor de authoritiesClaims en una colección de objetos GrantedAuthority
            //json to java object
            Collection<? extends GrantedAuthority> authorities = Arrays.asList(
                    new ObjectMapper() //we use ObjectMappper to transform from json to a java object and vice versa
                            .addMixIn(SimpleGrantedAuthority.class,
                                    SimpleGrantedAuthorityJsonCreator.class) //we use the SimpleGrantedAuthorityJsonCreator class to create the object personalized
                            .readValue(authoritiesClaims.toString().getBytes(), SimpleGrantedAuthority[].class));

            //authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
            //basically we send the data as username autorities and if this exists continuous
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, null,authorities);
            //auth static here!
            SecurityContextHolder.getContext().setAuthentication(authenticationToken); //If everything goes well when this line is executed, it means that the user is authenticated
            chain.doFilter(request, response); //if the user is authenticated, the request is allowed to continue its journey
        }catch (JwtException e){
            Map<String, String> body = new HashMap<>();
            body.put("error", e.getMessage());
            body.put("message", "invalid token");

            response.getWriter().write(new ObjectMapper().writeValueAsString(body));
            response.setStatus(401); //this explain that the token has expired but not as a Error
            response.setContentType("application/json");
        }

    }
}
