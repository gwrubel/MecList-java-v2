package com.meclist.security;

public record AuthenticatedUser(
      Long id,
        String email,
        String role
) {

}
