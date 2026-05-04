package com.meclist.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.meclist.exception.AcessoNegadoException;

@Component
public class AuthenticatedUserProvider {

    public AuthenticatedUser get() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !auth.isAuthenticated()
                || !(auth.getPrincipal() instanceof AuthenticatedUser user)) {
            throw new AcessoNegadoException("Usuário não autenticado.");
        }

        return user;
    }
}