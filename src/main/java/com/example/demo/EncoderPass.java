package com.example.demo;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class EncoderPass {
    public static void main(String[] args) {

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        // Contraseña original
        String password = "1234";

        // Codifica la contraseña
        String encodedPassword = passwordEncoder.encode(password);

        // Imprime la información
        System.out.println("Contraseña original: " + password);
        System.out.println("Contraseña codificada: " + encodedPassword);
    }
}
