package com.example.demo.auth;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.security.Key;

public class TokenJwtConfig {

    //public final static String SECRET = "secreta";
    public final static Key SECRET = Keys.secretKeyFor(SignatureAlgorithm.ES256); //encrypts the secret word;
    public final static String PREFIX_TOKEN = "Bearer ";
    public final static String SECRET_KEY = "Authorization";


}
