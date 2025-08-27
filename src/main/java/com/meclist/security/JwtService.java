package com.meclist.security;

import com.meclist.domain.Adm;
import com.meclist.domain.Mecanico;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.crypto.SecretKey;
import java.util.Date;

@Service
public class JwtService {

    @Value("${jwt.secret:}") // fallback vazio
    private String secret;

    private SecretKey secretKey;

    private final long EXPIRATION_TIME = 86400000; // 1 dia

    @PostConstruct
    public void init() {
        try {
            if (secret != null && !secret.trim().isEmpty()) {
                byte[] keyBytes = java.util.Base64.getDecoder().decode(secret);
                if (keyBytes.length < 64) { // 512 bits / 8 = 64 bytes
                    System.err.println("Chave JWT muito curta, gerando uma nova segura");
                    this.secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS512);
                } else {
                    this.secretKey = Keys.hmacShaKeyFor(keyBytes);
                }
            } else {
                System.out.println("JWT secret não configurado. Gerando chave segura para desenvolvimento");
                this.secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS512);
            }
        } catch (Exception e) {
            System.err.println("Erro ao inicializar JWT secret: " + e.getMessage());
            System.err.println("Usando chave segura padrão");
            this.secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS512);
        }
    }

    public String gerarTokenAdm(Adm adm) {
        return Jwts.builder()
                .setSubject(adm.getEmail())
                .claim("id", adm.getId())
                .claim("nome", adm.getNome())
                .claim("email", adm.getEmail())
                .claim("role", "ADMIN")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(secretKey, SignatureAlgorithm.HS512)
                .compact();
    }

    public String gerarTokenMecanico(Mecanico mecanico) {
        return Jwts.builder()
                .setSubject(mecanico.getEmail())
                .claim("id", mecanico.getId())
                .claim("nome", mecanico.getNome())
                .claim("email", mecanico.getEmail())
                .claim("role", "MECANICO")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(secretKey, SignatureAlgorithm.HS512)
                .compact();
    }

    public Claims getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
