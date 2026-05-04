package com.meclist.security;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    public JwtAuthenticationFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (StringUtils.hasText(header) && header.startsWith("Bearer ")) {
            String token = header.substring(7);
            try {
                Claims claims = jwtService.getClaims(token);
                String email = claims.getSubject();
                String role = claims.get("role", String.class);
                Number idNumber = claims.get("id", Number.class);
                Long id = idNumber != null ? idNumber.longValue() : null;
                Boolean approvalOnly = claims.get("approvalOnly", Boolean.class);
                Number checklistIdNumber = claims.get("checklistId", Number.class);
                Long checklistId = checklistIdNumber != null ? checklistIdNumber.longValue() : null;

                if (email != null && role != null && id != null) {
                    var authorities = List.of(new SimpleGrantedAuthority("ROLE_" + role));

                    AuthenticatedUser principal = new AuthenticatedUser(
                            id,
                            email,
                            role
                    );

                    var authentication = new UsernamePasswordAuthenticationToken(
                            principal,
                            null,
                            authorities
                    );

                        Map<String, Object> details = new HashMap<>();
                        details.put("approvalOnly", Boolean.TRUE.equals(approvalOnly));
                        details.put("checklistId", checklistId);
                        authentication.setDetails(details);

                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            } catch (Exception ignored) {
                // Token inválido/expirado: segue sem autenticação
            }
        }

        filterChain.doFilter(request, response);
    }
}


