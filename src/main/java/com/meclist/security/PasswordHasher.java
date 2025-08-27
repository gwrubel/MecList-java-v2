package com.meclist.security;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Component;

import com.meclist.interfaces.PasswordEncrypter;


@Component
public class PasswordHasher  implements PasswordEncrypter { 
    
    @Override
    public String hash(String senhaPura) {
        return BCrypt.hashpw(senhaPura, BCrypt.gensalt());
    }

    @Override
    public boolean verificar(String senhaPura, String hash) {
        return BCrypt.checkpw(senhaPura, hash);
    }
}
