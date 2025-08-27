package com.meclist.interfaces;

public interface PasswordEncrypter {
    String hash (String senhaPura);
    boolean verificar (String senhaPura, String senhaHash);
}
